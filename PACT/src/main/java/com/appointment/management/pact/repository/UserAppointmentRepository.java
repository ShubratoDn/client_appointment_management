package com.appointment.management.pact.repository;

import com.appointment.management.pact.entity.UserAppointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAppointmentRepository extends JpaRepository<UserAppointment, Integer> {
    // Add custom query methods if needed
}
