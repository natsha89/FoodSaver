package com.natasha.foodsaver.service;

import com.natasha.foodsaver.model.User;
import com.natasha.foodsaver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get user by ID
    public User getUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }

    // Delete user by ID
    public void deleteUserById(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        userRepository.delete(user);
    }

    // Save or update user
    public User saveUser(User user) {
        // Validate input
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        // Check if email is already in use by another user
        if (isEmailTaken(user)) {
            throw new RuntimeException("Email is already in use by another user");
        }

        // Save the user
        return userRepository.save(user);
    }

    // Helper method to check if email is already in use
    private boolean isEmailTaken(User user) {
        // Find if any other user exists with the same email
        return userRepository.findAll().stream()
                .anyMatch(existingUser ->
                        existingUser.getEmail().equalsIgnoreCase(user.getEmail()) &&
                                !existingUser.getId().equals(user.getId())
                );
    }

    // Update user method with partial update support
    public User updateUser(String userId, User updatedUser) {
        // Find existing user
        User existingUser = getUserById(userId);

        // Update fields that are not null in the updatedUser
        if (updatedUser.getFullName() != null) {
            existingUser.setFullName(updatedUser.getFullName());
        }

        if (updatedUser.getEmail() != null) {
            // Check if new email is already in use
            if (isEmailTaken(updatedUser)) {
                throw new RuntimeException("Email is already in use by another user");
            }
            existingUser.setEmail(updatedUser.getEmail());
        }

        if (updatedUser.getDietaryPreferences() != null) {
            existingUser.setDietaryPreferences(updatedUser.getDietaryPreferences());
        }

        if (updatedUser.getAllergies() != null) {
            existingUser.setAllergies(updatedUser.getAllergies());
        }

        // Save and return updated user
        return userRepository.save(existingUser);
    }
}