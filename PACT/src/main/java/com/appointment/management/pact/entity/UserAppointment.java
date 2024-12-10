
package com.appointment.management.pact.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "user_appointments")
@Data
@ToString
public class UserAppointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_appointment_id")
    private Integer userAppointmentId;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne
    @JoinColumn(name = "requested_user_id", nullable = false)
    private User requested_user;

    @ManyToOne
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;

    @Column(name = "role", length = 50)
    private String role;

    @Column(name = "status", length = 20)
    private String status;

    public User getRequested_user() {
        requested_user.setUserLogins(null);
        requested_user.setCalendars(null);
        return requested_user;
    }

    public void setRequested_user(User requested_user) {
        this.requested_user = requested_user;
    }

    public User getAuthor() {
        author.setCalendars(null);
        author.setUserLogins(null);
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    // Getters and Setters
}
