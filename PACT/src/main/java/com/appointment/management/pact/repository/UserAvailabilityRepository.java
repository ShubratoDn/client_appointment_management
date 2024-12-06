package com.appointment.management.pact.repository;

import com.appointment.management.pact.entity.User;
import com.appointment.management.pact.entity.UserAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserAvailabilityRepository extends JpaRepository<UserAvailability, Long> {

    @Transactional
    void deleteByUser(User user);

    List<UserAvailability> findByUser(User user);
}
