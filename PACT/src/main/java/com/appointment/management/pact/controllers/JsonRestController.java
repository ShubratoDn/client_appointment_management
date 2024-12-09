package com.appointment.management.pact.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.appointment.management.pact.entity.UserAvailability;
import com.appointment.management.pact.repository.UserAvailabilityRepository;

@RestController
public class JsonRestController {

    @Autowired
    private UserAvailabilityRepository availabilityRepository;

    @GetMapping("/check-availability/userID/{userId}/{date}")
    // @ResponseBody
    public ResponseEntity<?> checkAvailability(
            @PathVariable("userId") Long userId,
            @PathVariable("date") String date) {
        try {
            // Define a formatter matching the incoming date format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy");
            LocalDate parsedDate = LocalDate.parse(date, formatter); // Parse the date using the formatter

            // boolean isAvailable = availabilityRepository.isUserAvailableOnDate(userId,
            // parsedDate);
            List<UserAvailability> userAvailabilities = availabilityRepository.isUserAvailableOnDate(userId,
                    parsedDate);
            if (userAvailabilities.size() > 0) {
                UserAvailability userAvailability = userAvailabilities.get(0);
                // System.out.println(userAvailability);
                // System.out.println(userAvailability.getId());
                // System.out.println(userAvailability.getWorkingHourStart());
                // System.out.println(userAvailability.getWorkingHourEnd());

                return ResponseEntity.ok(userAvailability);
            } else {
                return ResponseEntity.status(404).body("User is not available on this day");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("An error occurred while checking availability.");
        }
    }

}