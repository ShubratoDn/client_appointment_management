package com.appointment.management.pact.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "fullname", length = 100)
    private String fullname;

    @Column(name = "username", length = 50)
    private String username;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @Column(name = "password_hash", length = 255)
    private String passwordHash;

    @Column(name = "role", length = 12)
    private String role;

    @Column(name = "token", length = 255)
    private String token;

    @Column(name = "otp", length = 6)
    private String otp;

    @Column(name = "otp_generate_time")
    private LocalDateTime otpGenerateTime;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_verified")
    private Boolean isVerified;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserLogin> userLogins;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Calendar> calendars;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<UserAppointment> userAppointments;

    @JsonIgnore
    public List<UserAppointment> getUserAppointments() {
        return userAppointments;
    }

    @JsonProperty
    public void setUserAppointments(List<UserAppointment> userAppointments) {
        this.userAppointments = userAppointments;
    }
}
