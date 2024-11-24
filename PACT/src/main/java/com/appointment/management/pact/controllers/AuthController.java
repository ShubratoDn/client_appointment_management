package com.appointment.management.pact.controllers;

import com.appointment.management.pact.entity.User;
import com.appointment.management.pact.services.MailService;
import com.appointment.management.pact.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder   passwordEncoder;

    @Autowired
    private MailService mailService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }


    @PostMapping("/register")
    public String registerPost(
            @RequestParam("fullName") String fullName,
            @RequestParam("username") String username,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword,
            Model model
    ) {
        // Create a user object to retain values
        User user = new User();
        user.setFullname(fullName);
        user.setUsername(username);
        user.setEmail(email);

        // Validation messages
        if (fullName.length() < 3) {
            model.addAttribute("message", "Full name must contain at least 3 characters.");
            model.addAttribute("user", user);
            return "register";
        }
        if (username.isEmpty()) {
            model.addAttribute("message", "Username cannot be empty.");
            model.addAttribute("user", user);
            return "register";
        }
        if (email.isEmpty()) {
            model.addAttribute("message", "Email cannot be empty.");
            model.addAttribute("user", user);
            return "register";
        }

        if (password.isBlank()) {
            model.addAttribute("message", "Insert password");
            model.addAttribute("user", user);
            return "register";
        }

        if (!password.equals(confirmPassword)) {
            model.addAttribute("message", "Passwords do not match.");
            model.addAttribute("user", user);
            return "register";
        }

        User userByUsernameOrEmail = userService.findUserByUsernameOrEmail(user.getUsername(), user.getEmail());

        if (userByUsernameOrEmail != null) {
            model.addAttribute("message", "User already exists");
            model.addAttribute("user", user);
            return "register";
        }

        // Save the user (dummy logic for example purposes)
        user.setPasswordHash(passwordEncoder.encode(password)); // Assume password is hashed
        user.setIsActive(false);
        user.setIsVerified(false);
        user.setRole("ROLE_USER");
        user.setCreatedAt(LocalDateTime.now());
        user.setOtp(generateOTP());
        user.setOtpGenerateTime(LocalDateTime.now());
        User savedUser = userService.createUser(user);

        model.addAttribute("user", user);
        model.addAttribute("verificationFor", "REGISTRATION_VERIFICATION");

        boolean mailSent = mailService.sendAccountVerificationMail(fullName, savedUser.getToken(), savedUser.getOtp(), savedUser.getEmail());
//        boolean mailSent = true;

        if(mailSent){
            model.addAttribute("successMessage", "User registered successfully! Kindly check your email to verify your account.");
            return "otp-verification";
        }else {
            model.addAttribute("successMessage", "User registered successfully! Email sent failed. Kindly communicate with support team.");
        }

        model.addAttribute("user", null); // Clear user after successful registration
        return "register";
    }


    @GetMapping("/verification")
    public String home(@RequestParam(value = "token", required = false) String token,
                       RedirectAttributes redirectAttributes) {
        if(token == null || token.isBlank()){
            return "404";
        }
        User userByToken = userService.findUserByToken(token);
        if(userByToken == null){
            return "404";
        }

        userByToken.setIsActive(true);
        userByToken.setIsVerified(true);
        userService.createUser(userByToken);


        redirectAttributes.addFlashAttribute("successMessage", "Your account has been successfully verified. You can now login.");
        return "redirect:/login";
    }



    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "forgot-password";
    }


    @GetMapping("/verify-otp")
    public String verifyotp(){
        return "redirect:/login";
    }

    @PostMapping("/verify-otp")
    public String verifyotp(@RequestParam String username,
                            @RequestParam String verificationFor,
                            @RequestParam String otp,
                            RedirectAttributes redirectAttributes,
                            Model model) {
        User userByUsername = userService.findUserByUsername(username);

        redirectAttributes.addFlashAttribute("user", userByUsername);
        redirectAttributes.addFlashAttribute("verificationFor", "FORGOT_PASSWORD");


        if (userByUsername == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "No account found with the provided email address. Please check and try again.");

            if(verificationFor.equals("FORGOT_PASSWORD")){
                return "redirect:/forgot-password";
            }else if(verificationFor.equals("REGISTRATION_VERIFICATION")){
                return "redirect:/login";
            }
            return "redirect:/forgot-password";
        }



        model.addAttribute("user", userByUsername);
        model.addAttribute("verificationFor", verificationFor);

        if(userByUsername.getOtp().equals(otp)){
            if(verificationFor.equals("FORGOT_PASSWORD")){
                return "redirect:/verify-forget-password?token=" + userByUsername.getToken();
            }else if(verificationFor.equals("REGISTRATION_VERIFICATION")){
                return "redirect:/verification?token=" + userByUsername.getToken();
            }
        }else{
            model.addAttribute("errorMessage", "Invalid OTP. Please try again.");
            return "otp-verification";
        }
        return "otp-verification";
    }

    @PostMapping("/forgot-password")
    public String forgotPasswordPost(@RequestParam("email") String email, Model model) {
        User userByEmail = userService.findUserByUsernameOrEmail(email, email);

        if (userByEmail == null) {
            model.addAttribute("errorMessage", "No account found with the provided email address. Please check and try again.");
            return "forgot-password";
        }


        //generate OTP
        userByEmail.setOtp(generateOTP());
        userByEmail.setOtpGenerateTime(LocalDateTime.now());
        userService.createUser(userByEmail);

        boolean mailSent = mailService.sendForgotPasswordMail(userByEmail);
//        boolean mailSent = true ;

        model.addAttribute("user", userByEmail);
        model.addAttribute("verificationFor", "FORGOT_PASSWORD");

        if (mailSent) {
            model.addAttribute("successMessage", "A password reset link and OTP has been sent to your email. Please check your inbox.");
        } else {
            model.addAttribute("errorMessage", "There was an issue sending the email. Please try again or contact support for assistance.");
        }
        return "otp-verification";
    }


    @GetMapping("/verify-forget-password")
    public String verifyForgotPassword(@RequestParam(value = "token", required = false) String token, RedirectAttributes redirectAttributes) {
        if (token == null || token.isEmpty()) {
            return "404";
        }
        User userByToken = userService.findUserByToken(token);

        if (userByToken == null) {
            return "404";
        }

        redirectAttributes.addFlashAttribute("user", userByToken);
        return "redirect:/reset-password";
    }

    @GetMapping("/reset-password")
    public String resetPassword(Model model, HttpSession session) {
        User user = (User) model.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        session.setAttribute("user", user);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String handleResetPassword(
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Session expired. Please try again.");
            return "redirect:/login";
        }

        redirectAttributes.addFlashAttribute("user", user);

        if (password == null || password.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Password cannot be empty.");
            return "redirect:/reset-password";
        }

        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Passwords do not match.");
            return "redirect:/reset-password";
        }

        // Update user password (assuming a password hashing utility is used)
        user.setPasswordHash(passwordEncoder.encode(password));
        userService.createUser(user);
        session.removeAttribute("user"); // Remove the user from the session after resetting password

        redirectAttributes.addFlashAttribute("successMessage", "Password reset successfully. Please log in.");
        return "redirect:/login";
    }



    public String generateOTP() {
        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000); // Generates a number between 100000 and 999999
        return String.valueOf(otp);
    }

}
