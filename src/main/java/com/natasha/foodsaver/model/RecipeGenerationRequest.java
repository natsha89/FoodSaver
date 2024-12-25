package com.natasha.foodsaver.model;

import java.util.List;

/*
 * MIT License
 * Copyright (c) [2024] [Natasha Shahran]
 *
 * Permission is granted under the MIT License to use, modify, and distribute
 * this software, provided credit is given to the original creator ([Natasha Shahran]).
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
 */


    // Inre klass som representerar begäran för att generera recept
    // Denna klass används för att fånga in de data som skickas med POST-förfrågan
    public class RecipeGenerationRequest {
        private String userId;  // Användarens ID för att koppla recepten till en specifik användare
        private String ingredients;  // Ingredienser som användaren har
        private List<String> allergens;  // Allergener som ska undvikas
        private String dietaryPreferences;  // Kostpreferenser (t.ex. vegetarisk, glutenfri)
        private int servings;  // Antal portioner som recepten ska vara för

        // Getter och Setter för ingredienser
        public String getIngredients() {
            return ingredients;
        }

        public void setIngredients(String ingredients) {
            this.ingredients = ingredients;
        }

        // Getter och Setter för allergener
        public List<String> getAllergens() {
            return allergens;
        }

        public void setAllergens(List<String> allergens) {
            this.allergens = allergens;
        }

        // Getter och Setter för kostpreferenser
        public String getDietaryPreferences() {
            return dietaryPreferences;
        }

        public void setDietaryPreferences(String dietaryPreferences) {
            this.dietaryPreferences = dietaryPreferences;
        }

        // Getter och Setter för antal portioner
        public int getServings() {
            return servings;
        }

        public void setServings(int servings) {
            this.servings = servings;
        }

        // Getter och Setter för userId
        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
}