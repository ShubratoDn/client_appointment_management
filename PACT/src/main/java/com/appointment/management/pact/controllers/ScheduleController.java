package com.appointment.management.pact.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ScheduleController {

	@GetMapping("my-schedule")
	public String myScheduleView() {
		return "my-schedule";
	}
	
}
