package com.appointment.management.pact.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

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
