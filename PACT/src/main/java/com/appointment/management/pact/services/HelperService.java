package com.appointment.management.pact.services;

import com.appointment.management.pact.configs.security.CustomUserDetails;
import com.appointment.management.pact.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class  HelperService {
    public static User getLoggedInUser(){
        // Get the current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        if(userDetails.getUser() != null){
            return userDetails.getUser();
        }else{
            return null;
        }
    }
}
