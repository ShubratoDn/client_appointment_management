
package com.appointment.management.pact.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user_appointments")
@Data
public class UserAppointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_appointment_id")
    private Integer userAppointmentId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;

    @Column(name = "role", length = 50)
    private String role;

    @Column(name = "status", length = 20)
    private String status;

    // Getters and Setters
}
