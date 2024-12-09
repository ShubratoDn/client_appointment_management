package com.appointment.management.pact.controllers;

import com.appointment.management.pact.entity.User;
import com.appointment.management.pact.repository.UserAvailabilityRepository;
import com.appointment.management.pact.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
	
}
