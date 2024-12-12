package com.appointment.management.pact.controllers;

import com.appointment.management.pact.entity.Appointment;
import com.appointment.management.pact.entity.User;
import com.appointment.management.pact.entity.UserAppointment;
import com.appointment.management.pact.payloads.AppointmentRequest;
import com.appointment.management.pact.repository.AppointmentRepository;
import com.appointment.management.pact.repository.UserAppointmentRepository;
import com.appointment.management.pact.repository.UserAvailabilityRepository;
import com.appointment.management.pact.services.HelperService;
import com.appointment.management.pact.services.MailService;
import com.appointment.management.pact.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class BookAppointment {

	@Autowired
	private AppointmentRepository appointmentRepository;

	@Autowired
	private UserService	userService;

	@Autowired
	private UserAvailabilityRepository availabilityRepository;

    @Autowired
    private UserAppointmentRepository userAppointmentRepository;

	@Autowired
	private MailService mailService;

	@GetMapping("/book-appointment")
	public String bookAppointmentView() {
		return "book-appointment";
	}
	
	@GetMapping("/appointment-requests")
	public String appointmentRequestsView(Model model) {
		LocalDateTime now = LocalDateTime.now();
		List<UserAppointment> upcomingAppointments = userAppointmentRepository.findAuthorsUpcomingAppointmentsWithStatus( HelperService.getLoggedInUser().getUserId(), now, "PENDING");
		model.addAttribute("pendingAppointments", upcomingAppointments);
		return "appointment-requests";
	}

	@GetMapping("/change-appointment-status/{id}/{status}")
	public String changeStatus(@PathVariable Integer id, @PathVariable String status, @RequestParam(name = "page", required = false) String page, Model model, RedirectAttributes redirectAttributes) {
		UserAppointment appointment = userAppointmentRepository.findById(id).orElse(null);
		if (appointment!= null) {
			List<UserAppointment> overlappingAppointments = userAppointmentRepository.findOverlappingAppointments(appointment.getAuthor().getUserId(), appointment.getAppointment().getStartTime(), appointment.getAppointment().getEndTime());

			if (status.equals("APPROVED") && !overlappingAppointments.isEmpty()) {
				redirectAttributes.addFlashAttribute("errorMessage", "Author or Requested user is not available within "+ appointment.getAppointment().getStartTime() + " and "+ appointment.getAppointment().getEndTime());
				return "redirect:/appointment-requests";
			}

			appointment.setStatus(status);
			userAppointmentRepository.save(appointment);

			if(status.equals("APPROVED")){
				mailService.notifyRequestedUserForAcceptedAppointment(appointment);
			}

			if(page != null && !page.isEmpty()){
				return "redirect:/"+page;
			}

        }
		return "redirect:/appointment-requests";
	}


	@GetMapping("/requested-appointment")
	public String requestedAppointmentView(Model model) {
		LocalDateTime now = LocalDateTime.now();
		List<UserAppointment> upcomingAppointments = userAppointmentRepository.findRequestedUsersUpcomingAppointmentsWithStatus( HelperService.getLoggedInUser().getUserId(), now, "PENDING");
		model.addAttribute("pendingAppointments", upcomingAppointments);
		return "requested-appointment";
	}


	@GetMapping("/appointment-request/{userid}")
	public String appointmentRequestView(@PathVariable int userid, Model model) {
		User user = userService.getUserById(userid);
		if(user == null){
			return "redirect:/";
		}

		model.addAttribute("user", user);
		// Fetch user's available dates
		List<String> availableDates = availabilityRepository.findByUser(user).stream()
				.map(availability -> availability.getAvailableDate().toString())
				.collect(Collectors.toList());

		// Add available dates to the model
		model.addAttribute("availableDates", availableDates);

		List<UserAppointment> allByAuthor = userAppointmentRepository.findAllByAuthor(userid);
		model.addAttribute("allAppointment", allByAuthor);

		return "appointment-request-user";
	}



	@PostMapping("/appointment-request/{userId}")
	public ResponseEntity<?> createAppointment(
			@PathVariable("userId") Integer userId,
			@RequestBody AppointmentRequest appointmentRequest) {

		// Debugging: Print received data
		System.out.println("User ID: " + userId);
		System.out.println("Booking Date: " + appointmentRequest.getBookingDate());
		System.out.println("Start Time: " + appointmentRequest.getStartTime());
		System.out.println("End Time: " + appointmentRequest.getEndTime());
		System.out.println("Description: " + appointmentRequest.getDescription());
		System.out.println("Is All Day: " + appointmentRequest.getAllDay());
		System.out.println("Location: " + appointmentRequest.getLocation());

		LocalDate date ;
		try{
			// LocalDate
			ZonedDateTime zonedDateTime = ZonedDateTime.parse(appointmentRequest.getBookingDate());
			// Adjust to the system default time zone
			date = zonedDateTime.withZoneSameInstant(ZoneId.systemDefault()).toLocalDate();

		}catch (Exception e){
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy");
			date = LocalDate.parse(appointmentRequest.getBookingDate(), formatter); // Parse the date using the formatter
		}



		LocalTime startTime = LocalTime.parse(appointmentRequest.getStartTime());
		LocalTime endTime = LocalTime.parse(appointmentRequest.getEndTime());

		// Merge into LocalDateTime
		LocalDateTime startDateTime = LocalDateTime.of(date, startTime);
		LocalDateTime endDateTime = LocalDateTime.of(date, endTime);

		// Output the result
		System.out.println("Merged Start Date Time: " + startDateTime);
		System.out.println("Merged End Date Time: " + endDateTime);

		// Generate start and end of the day
		LocalDateTime startOfDay = date.atStartOfDay();
		LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

		// Debugging: Print start and end of the day
		System.out.println("Start of Day: " + startOfDay);
		System.out.println("End of Day: " + endOfDay);


		if(appointmentRequest.getAllDay()){
			List<UserAppointment> bookingsByAuthorAndDay = userAppointmentRepository.findBookingsByAuthorAndDay(userId, startOfDay, endOfDay);

			if(!bookingsByAuthorAndDay.isEmpty()){
				System.out.println("User is not available whole day!");
				return ResponseEntity.status(400).body("User is not available whole day!");
			}else{
				System.out.println("User is available for whole day!");
			}
		}


		List<UserAppointment> overlappingAppointments = userAppointmentRepository.findOverlappingAppointments(userId, startDateTime, endDateTime);

		if (!overlappingAppointments.isEmpty()) {
			return ResponseEntity.status(400).body("Author is not available within "+ appointmentRequest.getStartTime() + " and "+ appointmentRequest.getEndTime());
		}


		// Create Appointment entity
		Appointment appointment = new Appointment();
		appointment.setStartTime(startDateTime);
		appointment.setEndTime(endDateTime);
		appointment.setDescription(appointmentRequest.getDescription());
		appointment.setIsAllDay(appointmentRequest.getAllDay());
		appointment.setLocation(appointmentRequest.getLocation());
		appointment.setCreatedAt(LocalDateTime.now());



		// Create UserAppointment entity
		UserAppointment userAppointment = new UserAppointment();
//		userAppointment.setAppointment(appointment);
		userAppointment.setRole("CREATOR");
		userAppointment.setStatus("PENDING");
		User loggedInUser = HelperService.getLoggedInUser();
		loggedInUser.setUserAppointments(null);
		userAppointment.setRequested_user(loggedInUser);

		// Mocked User association
		User user = new User(); // Fetch an actual User object from the DB
		user.setUserId(userId);
		userAppointment.setAuthor(user);


		appointment = appointmentRepository.save(appointment);
		userAppointment.setAppointment(appointment);
		userAppointment = userAppointmentRepository.save(userAppointment);

		// Debugging: Print converted objects
		System.out.println("Created Appointment: " + appointment);
		System.out.println("Created UserAppointment: " + userAppointment);

		mailService.notifyAuthorForNewAppointment(userAppointment);

		// Success response
		return ResponseEntity.ok("Appointment created successfully!");
	}
}
