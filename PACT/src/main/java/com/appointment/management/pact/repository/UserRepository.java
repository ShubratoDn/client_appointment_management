package com.appointment.management.pact.repository;

import com.appointment.management.pact.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsername(String username);
    User findByUsernameOrEmail(String username, String email);

    User findByToken(String token);

    List<User> findByUsernameContainingOrEmailContainingOrFullnameContaining(String username, String email, String fullname);
}
