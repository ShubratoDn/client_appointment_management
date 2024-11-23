package com.appointment.management.pact.services;

import com.appointment.management.pact.entity.User;

import java.util.List;

public interface UserService {
    User createUser(User user);
    User updateUser(Integer userId, User user);
    User getUserById(Integer userId);
    List<User> getAllUsers();
    void deleteUser(Integer userId);
    User findUserByUsername(String username);
    User findUserByToken(String token);
    User findUserByUsernameOrEmail(String username, String mail);
}
