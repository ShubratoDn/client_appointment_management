package com.appointment.management.pact.serviceimpl;

import com.appointment.management.pact.entity.Appointment;
import com.appointment.management.pact.entity.User;
import com.appointment.management.pact.entity.UserAppointment;
import com.appointment.management.pact.services.MailService;
import com.appointment.management.pact.services.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Component
public class MailServiceImple implements MailService {

    @Autowired
    private UserService userService;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private HttpServletRequest request;  // Inject HttpServletRequest to get the request URL

    @Override
    public boolean sendAccountVerificationMail(String fullname, String token, String otp, String to) {
        // Get the host URL dynamically
        String siteUrl = getSiteUrl();
        String verificationUrl = siteUrl + "/verification?token=" + token;

        // Create the email content with the verification link and OTP option
        String subject = "Account Verification - PACT";
        String htmlContent =
                "<html>"
                        + "<body style=\"font-family: Arial, sans-serif; color: #333;\">"
                        + "<h2>Welcome to PACT - Planning & Calendar Tool</h2>"
                        + "<p>Dear <b>" + fullname + ",</b></p>"
                        + "<p>Thank you for registering with us. To complete your registration, please verify your email address using one of the following options:</p>"

                        // Option 1: Verify with the Email Link
                        + "<p>If you'd prefer to verify your email address, simply click the link below:</p>"
                        + "<p><a href=\"" + verificationUrl + "\" style=\"background-color: #4CAF50; color: white; padding: 14px 20px; text-align: center; text-decoration: none; border-radius: 5px;\">Verify Email Address</a></p>"

                        // Option 2: Verify with OTP
                        + "<p>If you would rather use a one-time password (OTP) for verification, here is your OTP:</p>"
                        + "<p><b>" + otp + "</b></p>"
                        + "<p>Please enter the OTP on the verification page to complete your registration.</p>"

                        // Reminder in case the email wasn't requested
                        + "<p>If you didn't create an account with us, you can ignore this email. Your account will not be created.</p>"
                        + "<p>Best regards,<br>Pact Team</p>"
                        + "</body>"
                        + "</html>";

        // Send the email
        return sendEmail(to, subject, htmlContent);
    }



    @Override
    public boolean sendForgotPasswordMail(User user) {
        // Get the host URL dynamically
        String siteUrl = getSiteUrl();
        String resetPasswordUrl = siteUrl + "/verify-forget-password?token=" + user.getToken();
        String otp = user.getOtp();

        // Create the email content with the password reset link and OTP verification
        String subject = "Password Reset Request - PACT";
        String htmlContent =
                "<html>"
                        + "<body style=\"font-family: Arial, sans-serif; color: #333;\">"
                        + "<h2>Password Reset Request</h2>"
                        + "<p>Dear <b>" + user.getFullname() + ",</b></p>"
                        + "<p>We received a request to reset your password. You can choose one of the following options:</p>"

                        // Option 1: Password Reset Link
                        + "<p>If you prefer to reset your password immediately, click the button below:</p>"
                        + "<p><a href=\"" + resetPasswordUrl + "\" style=\"background-color: #4CAF50; color: white; padding: 14px 20px; text-align: center; text-decoration: none; border-radius: 5px;\">Reset Password</a></p>"

                        // Option 2: OTP for Verification
                        + "<p>Alternatively, if you wish to verify your identity using an OTP, use the following one-time password:</p>"
                        + "<p><b>" + otp + "</b></p>"
                        + "<p>Enter the OTP on the verification page to reset your password securely.</p>"

                        // Final reminder in case of no request
                        + "<p>If you did not request this password reset, please ignore this email. Your password will not be changed.</p>"
                        + "<p>Best regards,<br>PACT Team</p>"
                        + "</body>"
                        + "</html>";

        // Send the email
        return sendEmail(user.getEmail(), subject, htmlContent);
    }


    @Override
    public boolean notifyAuthorForNewAppointment(UserAppointment userAppointment) {
        // Get the author and requested user details
        User author = userService.getUserById(userAppointment.getAuthor().getUserId());
        User requestedUser = userAppointment.getRequested_user();
        Appointment appointment = userAppointment.getAppointment();

        // Prepare email content
        String subject = "New Appointment Request - PACT";
        String appointmentTime = formatAppointmentTime(appointment.getStartTime(), appointment.getEndTime());

        String htmlContent =
                "<html>"
                        + "<body style=\"font-family: Arial, sans-serif; color: #333;\">"
                        + "<h2>New Appointment Request</h2>"
                        + "<p>Dear <b>" + author.getFullname() + ",</b></p>"
                        + "<p><b>" + requestedUser.getFullname() + "</b> has requested a new appointment with you. Here are the details:</p>"

                        + "<p><b>Appointment Details:</b></p>"
                        + "<ul>"
                        + "<li><b>Location:</b> " + appointment.getLocation() + "</li>"
                        + "<li><b>Description:</b> " + appointment.getDescription() + "</li>"
                        + "<li><b>Date & Time:</b> " + appointmentTime + "</li>"
                        + "</ul>"

                        + "<p>Please log in to your dashboard to accept or decline this request.</p>"
                        + "<p>Best regards,<br>PACT Team</p>"
                        + "</body>"
                        + "</html>";

        // Send email
        return sendEmail(author.getEmail(), subject, htmlContent);
    }



    @Override
    public boolean notifyRequestedUserForAcceptedAppointment(UserAppointment userAppointment) {
        // Get the author and requested user details
        User author = userAppointment.getAuthor();
        User requestedUser = userAppointment.getRequested_user();
        Appointment appointment = userAppointment.getAppointment();

        // Prepare email content
        String subject = "Appointment Confirmed - PACT";
        String appointmentTime = formatAppointmentTime(appointment.getStartTime(), appointment.getEndTime());

        String htmlContent =
                "<html>"
                        + "<body style=\"font-family: Arial, sans-serif; color: #333;\">"
                        + "<h2>Appointment Confirmed</h2>"
                        + "<p>Dear <b>" + requestedUser.getFullname() + ",</b></p>"
                        + "<p>We are pleased to inform you that <b>" + author.getFullname() + "</b> has accepted your appointment request. Below are the details of your confirmed appointment:</p>"

                        + "<p><b>Appointment Details:</b></p>"
                        + "<ul>"
                        + "<li><b>Location:</b> " + appointment.getLocation() + "</li>"
                        + "<li><b>Description:</b> " + appointment.getDescription() + "</li>"
                        + "<li><b>Date & Time:</b> " + appointmentTime + "</li>"
                        + "</ul>"

                        + "<p>Please ensure that you are available during the scheduled time.</p>"
                        + "<p>We look forward to your successful meeting.</p>"
                        + "<p>Best regards,<br>PACT Team</p>"
                        + "</body>"
                        + "</html>";

        // Send email
        return sendEmail(requestedUser.getEmail(), subject, htmlContent);
    }



    /**
     * Helper method to format appointment start and end times for readability.
     */
    private String formatAppointmentTime(LocalDateTime startTime, LocalDateTime endTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");
        return startTime.format(formatter) + " to " + endTime.format(formatter);
    }





    // Helper method to get the base URL (host and port)
    private String getSiteUrl() {
        String scheme = request.getScheme(); // http or https
        String serverName = request.getServerName(); // domain name or localhost
        int serverPort = request.getServerPort(); // port number
        return scheme + "://" + serverName + (serverPort != 80 && serverPort != 443 ? ":" + serverPort : "");
    }



    // Method to send the email with the generated HTML content
    private boolean sendEmail(String to, String subject, String htmlContent) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(new InternetAddress("user@gmail.com", "PACT"));
            helper.setTo(to);                      // Recipient's email address
            helper.setSubject(subject);            // Email subject
            helper.setText(htmlContent, true);     // Set the HTML content

            // Send the email
            javaMailSender.send(message);
            return true;
        } catch (MailException | MessagingException e) {
            System.err.println("Error sending HTML email: " + e.getMessage());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
