package com.appointment.management.pact.controllers;

import com.appointment.management.pact.entity.User;
import com.appointment.management.pact.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users/search")
    public ResponseEntity<?> getUsersByInfo(@RequestParam(required = false, defaultValue = "") String searchQuery){
        List<User> users = userService.searchUser(searchQuery);
        return ResponseEntity.ok(users);
    }
}
