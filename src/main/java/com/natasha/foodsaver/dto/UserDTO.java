package com.natasha.foodsaver.dto;

import java.util.List;

public class UserDTO {
    private String id;
    private String fullName;
    private String email;
    private List<String> allergies;
    private List<String> dietaryPreferences;

    public UserDTO(String id, String fullName, String email, List<String> allergies, List<String> dietaryPreferences) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.allergies = allergies;
        this.dietaryPreferences = dietaryPreferences;
    }

    // Getters och Setters
}

