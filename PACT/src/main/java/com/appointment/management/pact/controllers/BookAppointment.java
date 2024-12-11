package com.appointment.management.pact.controllers;

import com.appointment.management.pact.entity.Appointment;
import com.appointment.management.pact.entity.User;
import com.appointment.management.pact.entity.UserAppointment;
import com.appointment.management.pact.payloads.AppointmentRequest;
import com.appointment.management.pact.repository.AppointmentRepository;
import com.appointment.management.pact.repository.UserAppointmentRepository;
import com.appointment.management.pact.repository.UserAvailabilityRepository;
import com.appointment.management.pact.services.HelperService;
import com.appointment.management.pact.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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

	@GetMapping("/book-appointment")
	public String bookAppointmentView() {
		return "book-appointment";
	}
	
	@GetMapping("/appointment-requests")
	public String appointmentRequestsView() {
		return "appointment-requests";
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



		// Success response
		return ResponseEntity.ok("Appointment created successfully!");
	}
}
