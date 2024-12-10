package com.appointment.management.pact.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "appointments")
@Data
@ToString
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id")
    private Integer appointmentId;

    @ManyToOne
    @JoinColumn(name = "calendar_id", nullable = true)
    private Calendar calendar;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "location", length = 100)
    private String location;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "is_all_day")
    private Boolean isAllDay;
    // Getters and Setters
}
