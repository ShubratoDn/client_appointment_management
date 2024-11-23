package com.appointment.management.pact.repository;

import com.appointment.management.pact.entity.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLoginRepository extends JpaRepository<UserLogin, Integer> {
    // Add custom query methods if needed
}
