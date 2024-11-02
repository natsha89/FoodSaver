package com.natasha.foodsaver.service;

import com.natasha.foodsaver.model.User;
import com.natasha.foodsaver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User register(User user) {
        // Check if the email already exists
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("Email is already in use."); // This will be caught later
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User login(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        throw new RuntimeException("Invalid email or password."); // This will be caught later
    }

    public void deleteAccount(String userId) {
        // Check if the user exists before trying to delete
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId); // Delete user by ID
        } else {
            throw new RuntimeException("User not found."); // Handle case where user doesn't exist
        }
    }

}
