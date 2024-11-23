package com.appointment.management.pact.serviceimpl;

import com.appointment.management.pact.entity.User;
import com.appointment.management.pact.repository.UserRepository;
import com.appointment.management.pact.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User createUser(User user) {
        user.setToken(generateToken());
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Integer userId, User user) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        existingUser.setPhoneNumber(user.getPhoneNumber());
        // Update other fields
        return userRepository.save(existingUser);
    }

    @Override
    public User getUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }

    @Override
    public User findUserByToken(String token) {
        return userRepository.findByToken(token);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(Integer userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    @Override
    public User findUserByUsernameOrEmail(String username, String mail){
        return userRepository.findByUsernameOrEmail(username, mail);
    }




    // Define the characters that can be used in the token
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int TOKEN_LENGTH = 50; // Desired length of the token

    private final SecureRandom random = new SecureRandom();

    // Method to generate a random token of a specified length
    public String generateToken() {
        StringBuilder token = new StringBuilder(TOKEN_LENGTH);

        // Generate a random token using the defined alphabet
        for (int i = 0; i < TOKEN_LENGTH; i++) {
            int index = random.nextInt(ALPHABET.length());
            token.append(ALPHABET.charAt(index));
        }

        return token.toString();
    }
}
