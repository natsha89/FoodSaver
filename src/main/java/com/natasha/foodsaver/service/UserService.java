package com.natasha.foodsaver.service;

import com.natasha.foodsaver.model.User;
import com.natasha.foodsaver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service  // Märker denna klass som en tjänstkomponent för Spring, så att den kan injiceras i andra klasser
public class UserService {

    @Autowired
    private UserRepository userRepository;  // Beroendeinjektion av UserRepository för att hantera användardata i databasen

    // Hämta alla användare
    public List<User> getAllUsers() {
        return userRepository.findAll();  // Anropar repository för att hämta alla användare från databasen
    }

    // Hämta användare genom ID
    public User getUserById(String userId) {
        // Om användaren inte hittas, kasta ett undantag
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }

    // Ta bort användare genom ID
    public void deleteUserById(String userId) {
        // Hitta användaren
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        userRepository.delete(user);  // Ta bort användaren från databasen
    }

    // Spara eller uppdatera användare
    public User saveUser(User user) {
        // Validera input
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");  // Om användaren är null, kasta ett undantag
        }

        // Kontrollera om e-posten redan används av en annan användare
        if (isEmailTaken(user)) {
            throw new RuntimeException("Email is already in use by another user");  // Om e-posten är tagen, kasta ett undantag
        }

        // Spara användaren i databasen
        return userRepository.save(user);
    }

    // Hjälpmetod för att kontrollera om e-posten redan är tagen av en annan användare
    private boolean isEmailTaken(User user) {
        // Hitta om någon annan användare har samma e-postadress
        return userRepository.findAll().stream()
                .anyMatch(existingUser ->
                        existingUser.getEmail().equalsIgnoreCase(user.getEmail()) &&
                                !existingUser.getId().equals(user.getId())
                );
    }

    // Uppdatera användare (delvis uppdatering är möjligt)
    public User updateUser(String userId, User updatedUser) {
        // Hitta den befintliga användaren
        User existingUser = getUserById(userId);

        // Uppdatera fält som inte är null i updatedUser
        if (updatedUser.getFullName() != null) {
            existingUser.setFullName(updatedUser.getFullName());  // Uppdatera namn om det är angivet
        }

        if (updatedUser.getEmail() != null) {
            // Kontrollera om den nya e-posten redan används
            if (isEmailTaken(updatedUser)) {
                throw new RuntimeException("Email is already in use by another user");  // Om e-posten redan är tagen, kasta ett undantag
            }
            existingUser.setEmail(updatedUser.getEmail());  // Uppdatera e-post om det är angivet
        }

        if (updatedUser.getDietaryPreferences() != null) {
            existingUser.setDietaryPreferences(updatedUser.getDietaryPreferences());  // Uppdatera kost preferenser om det är angivet
        }

        if (updatedUser.getAllergies() != null) {
            existingUser.setAllergies(updatedUser.getAllergies());  // Uppdatera allergier om det är angivet
        }

        // Spara och returnera den uppdaterade användaren
        return userRepository.save(existingUser);
    }
}
