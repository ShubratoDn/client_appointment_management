package com.appointment.management.pact.repository;

import com.appointment.management.pact.entity.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendarRepository extends JpaRepository<Calendar, Integer> {
    // Add custom query methods if needed
}
