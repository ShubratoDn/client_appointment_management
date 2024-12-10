package com.appointment.management.pact.controllers;

import com.appointment.management.pact.entity.UserAppointment;
import com.appointment.management.pact.repository.UserAppointmentRepository;
import com.appointment.management.pact.services.HelperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private UserAppointmentRepository userAppointmentRepository;

    @GetMapping(value = {"/home", "/", ""})
    public String home() {
        return "redirect:/user/dashboard";
    }

    @GetMapping("/user/dashboard")
    public String dashboard(Model model) {
        LocalDateTime now = LocalDateTime.now();
        List<UserAppointment> upcomingAppointments = userAppointmentRepository.findUpcomingAppointments(HelperService.getLoggedInUser().getUserId(), HelperService.getLoggedInUser().getUserId(), now);
        model.addAttribute("upcomingAppointments", upcomingAppointments);
        return "index";
    }
}
