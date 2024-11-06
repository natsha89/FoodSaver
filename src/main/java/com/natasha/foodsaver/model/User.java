package com.natasha.foodsaver.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "users")
public class User {

    @Id
    private String id;

    @NotBlank(message = "Full Name is required")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    private List<String> allergies;
    private List<String> dietaryPreferences;
    private List<String> savedRecipes; // Add this field

    private String verificationToken; // Token for email verification
    private LocalDateTime verificationTokenExpiration; // Expiration time for the verification token
    private boolean emailVerified = false; // Whether the email is verified or not

    // No-argument constructor
    public User() {
    }

    // All-arguments constructor
    public User(String id, String fullName, String email, String password, List<String> allergies,
                List<String> dietaryPreferences, List<String> savedRecipes, String verificationToken,
                LocalDateTime verificationTokenExpiration, boolean emailVerified) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.allergies = allergies;
        this.dietaryPreferences = dietaryPreferences;
        this.savedRecipes = savedRecipes;
        this.verificationToken = verificationToken;
        this.verificationTokenExpiration = verificationTokenExpiration;
        this.emailVerified = emailVerified;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getAllergies() {
        return allergies;
    }

    public void setAllergies(List<String> allergies) {
        this.allergies = allergies;
    }

    public List<String> getDietaryPreferences() {
        return dietaryPreferences;
    }

    public void setDietaryPreferences(List<String> dietaryPreferences) {
        this.dietaryPreferences = dietaryPreferences;
    }

    public List<String> getSavedRecipes() {
        return savedRecipes;
    }

    public void setSavedRecipes(List<String> savedRecipes) {
        this.savedRecipes = savedRecipes;
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }

    public LocalDateTime getVerificationTokenExpiration() {
        return verificationTokenExpiration;
    }

    public void setVerificationTokenExpiration(LocalDateTime verificationTokenExpiration) {
        this.verificationTokenExpiration = verificationTokenExpiration;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    // toString method
    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", allergies=" + allergies +
                ", dietaryPreferences=" + dietaryPreferences +
                ", savedRecipes=" + savedRecipes +
                ", verificationToken='" + verificationToken + '\'' +
                ", verificationTokenExpiration=" + verificationTokenExpiration +
                ", emailVerified=" + emailVerified +
                '}';
    }
}

