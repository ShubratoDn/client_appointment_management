package com.appointment.management.pact.services;


import com.appointment.management.pact.entity.User;
import com.appointment.management.pact.entity.UserAppointment;

public interface MailService {
    public boolean sendAccountVerificationMail(String fullname, String token, String otp, String to);
    public boolean sendForgotPasswordMail(User user) ;

    public boolean notifyAuthorForNewAppointment(UserAppointment userAppointment);

    public boolean notifyRequestedUserForAcceptedAppointment(UserAppointment userAppointment);
}
