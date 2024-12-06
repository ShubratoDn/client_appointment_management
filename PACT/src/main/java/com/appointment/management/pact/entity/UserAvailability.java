package com.appointment.management.pact.entity;

import jakarta.persistence.*;
import java.time.LocalTime;
import java.time.LocalDate;

@Entity
@Table(name = "user_availability")
public class UserAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "available_date", nullable = false)
    private LocalDate availableDate;

    @Column(name = "working_hour_start", nullable = false)
    private LocalTime workingHourStart;

    @Column(name = "working_hour_end", nullable = false)
    private LocalTime workingHourEnd;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getAvailableDate() {
        return availableDate;
    }

    public void setAvailableDate(LocalDate availableDate) {
        this.availableDate = availableDate;
    }

    public LocalTime getWorkingHourStart() {
        return workingHourStart;
    }

    public void setWorkingHourStart(LocalTime workingHourStart) {
        this.workingHourStart = workingHourStart;
    }

    public LocalTime getWorkingHourEnd() {
        return workingHourEnd;
    }

    public void setWorkingHourEnd(LocalTime workingHourEnd) {
        this.workingHourEnd = workingHourEnd;
    }
}
