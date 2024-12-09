package com.appointment.management.pact.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping(value = {"/home", "/", ""})
    public String home() {
        return "redirect:/user/dashboard";
    }

    @GetMapping("/user/dashboard")
    public String dashboard() {
        return "index";
    }
}
