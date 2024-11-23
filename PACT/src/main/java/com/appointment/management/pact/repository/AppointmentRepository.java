package com.appointment.management.pact.repository;

import com.appointment.management.pact.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    // Add custom query methods if needed, e.g., findByCalendarId
}
