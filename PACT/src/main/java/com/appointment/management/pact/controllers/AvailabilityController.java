package com.appointment.management.pact.controllers;

import com.appointment.management.pact.configs.security.CustomUserDetails;
import com.appointment.management.pact.entity.UserAvailability;
import com.appointment.management.pact.payloads.AvailabilityRequest;
import com.appointment.management.pact.repository.UserAvailabilityRepository;
import com.appointment.management.pact.services.HelperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AvailabilityController {

    @Autowired
    private UserAvailabilityRepository availabilityRepository;

    @GetMapping("/set-availability")
    public String setAvailabilityView(Model model) {

        // Fetch user's available dates
        List<String> availableDates = availabilityRepository.findByUser(HelperService.getLoggedInUser()).stream()
                .map(availability -> availability.getAvailableDate().toString()) // Convert to ISO-8601 date strings
                .collect(Collectors.toList());

        // Add available dates to the model
        model.addAttribute("availableDates", availableDates);
        return "set-availability";
    }

    @PostMapping("/set-availability")
    @ResponseBody
    public ResponseEntity<String> setAvailability(@RequestBody AvailabilityRequest request) {

        // Get the current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        // Remove existing availability for the user
        availabilityRepository.deleteByUser(userDetails.getUser());

        // Save new availability data
        for (String date : request.getAvailableDates()) {
            // Parse the date string to ZonedDateTime, adjust the time zone, and get the
            // LocalDate
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(date);

            // Adjust to the system default time zone
            LocalDate availableDate = zonedDateTime.withZoneSameInstant(ZoneId.systemDefault()).toLocalDate();

            // Now save the availability data
            UserAvailability availability = new UserAvailability();
            availability.setUser(userDetails.getUser());
            availability.setAvailableDate(availableDate);
            availability.setWorkingHourStart(LocalTime.parse(request.getWorkingHourStarts()));
            availability.setWorkingHourEnd(LocalTime.parse(request.getWorkingHourEnds()));
            availabilityRepository.save(availability);
            System.out.println("Parsed date: " + availableDate);
        }

        return ResponseEntity.ok("Availability set successfully!");
    }

    

}
