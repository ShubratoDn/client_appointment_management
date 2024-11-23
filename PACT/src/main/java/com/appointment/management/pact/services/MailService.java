package com.appointment.management.pact.services;


import com.appointment.management.pact.entity.User;

public interface MailService {
    public boolean sendAccountVerificationMail(String fullname, String token, String otp, String to);
    public boolean sendForgotPasswordMail(User user) ;
}
