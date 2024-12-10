package com.appointment.management.pact.controllers;

import com.appointment.management.pact.entity.Appointment;
import com.appointment.management.pact.entity.User;
import com.appointment.management.pact.repository.UserAvailabilityRepository;
import com.appointment.management.pact.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class BookAppointment {

	@Autowired
	private UserService	userService;

	@Autowired
	private UserAvailabilityRepository availabilityRepository;

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

		return "appointment-request-user";
	}



	@PostMapping("/appointment-request/{userId}")
	public ResponseEntity<?> createAppointment(
			@PathVariable("userId") Long userId,
			@RequestBody Appointment appointmentRequest) {

		// Debugging: Print received data
		System.out.println("User ID: " + userId);
		System.out.println("Start Time: " + appointmentRequest.getStartTime());
		System.out.println("End Time: " + appointmentRequest.getEndTime());
		System.out.println("Description: " + appointmentRequest.getDescription());
		System.out.println("Is All Day: " + appointmentRequest.getIsAllDay());
		System.out.println("Location: " + appointmentRequest.getLocation());

		// Validate the input (Optional, can also be done via annotations)
		if (appointmentRequest.getDescription() == null || appointmentRequest.getDescription().isEmpty()) {
			return ResponseEntity.badRequest().body("Description is required.");
		}
		if (appointmentRequest.getStartTime() == null || appointmentRequest.getEndTime() == null) {
			return ResponseEntity.badRequest().body("Start and end times are required.");
		}

		// Logic to save the appointment (Mocked here)
		// Example: appointmentService.save(userId, appointmentRequest);

		// Success response
		return ResponseEntity.ok("Appointment created successfully!");
	}
}
