package com.appointment.management.pact.repository;

import com.appointment.management.pact.entity.User;
import com.appointment.management.pact.entity.UserAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface UserAvailabilityRepository extends JpaRepository<UserAvailability, Long> {

    @Transactional
    void deleteByUser(User user);

    List<UserAvailability> findByUser(User user);

    @Query("FROM UserAvailability ua " +
            "WHERE ua.user.id = :userId AND ua.availableDate = :date")
    List<UserAvailability> isUserAvailableOnDate(@Param("userId") Long userId, @Param("date") LocalDate date);
}
