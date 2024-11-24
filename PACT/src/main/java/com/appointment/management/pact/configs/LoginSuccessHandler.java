package com.appointment.management.pact.configs;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.appointment.management.pact.entity.User;
import com.appointment.management.pact.entity.UserLogin;
import com.appointment.management.pact.repository.UserLoginRepository;
import com.appointment.management.pact.repository.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserLoginRepository userLoginRepository;

    @Autowired
    private UserRepository userRepository; // Repository to fetch the User entity

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // Retrieve logged-in user's username
        String username = authentication.getName();

        // Fetch the user entity from the database
        User user = userRepository.findByUsername(username);

        // Get the user's IP address
        String ipAddress = getClientIp(request);

        // Create a log entry
        UserLogin log = new UserLogin();
        log.setUser(user); // Associate the user entity
        log.setLoginTime(LocalDateTime.now()); // Set the current login time
        log.setIpAddress(ipAddress); // Set the IP address

        // Save the log to the database
        userLoginRepository.save(log);
        
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        // Redirect to the default success URL
        response.sendRedirect("/user/dashboard");
    }

    // Utility method to fetch the client's IP address
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
