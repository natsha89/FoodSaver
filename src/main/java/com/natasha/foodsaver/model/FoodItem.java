package com.natasha.foodsaver.model;

import com.natasha.foodsaver.service.AIService;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Document(collection = "foodItems")
public class FoodItem {

    @Id
    private String id;
    private String userId;
    private String name;
    private double quantity;
    private String unit;
    private LocalDate expirationDate;
    private List<String> allergens = new ArrayList<>();
    private List<Recipe> recipeSuggestions = new ArrayList<>();

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public List<String> getAllergens() {
        return allergens;
    }

    public void setAllergens(List<String> allergens) {
        this.allergens = allergens;
    }

    public List<Recipe> getRecipeSuggestions() {
        return recipeSuggestions;
    }

    public void setRecipeSuggestions(List<Recipe> recipeSuggestions) {
        this.recipeSuggestions = recipeSuggestions;
    }

    // Allergy Check
    public void checkAllergies(List<String> userAllergies) {
        for (String allergen : allergens) {
            if (userAllergies.contains(allergen)) {
                sendAllergenNotification(allergen);
            }
        }
    }

    private void sendAllergenNotification(String allergen) {
        System.out.println("Alert: " + name + " contains " + allergen + ", which you're allergic to.");
    }

    // Fetch Recipe Suggestions
    public void fetchRecipeSuggestions(AIService aiService, String dietaryPreferences, int servings) {
        // Pass ingredients and allergens to AI service to generate recipes
        String ingredients = name; // Assuming the food item's name as the ingredient for simplicity
        this.recipeSuggestions = aiService.generateAIRecipes(ingredients, allergens, dietaryPreferences, servings);
    }

    // Expiration Notification
    public void scheduleExpirationNotification() {
        LocalDate notificationDate = expirationDate.minusDays(3);
        long daysUntilNotification = ChronoUnit.DAYS.between(LocalDate.now(), notificationDate);

        if (daysUntilNotification == 0) {
            System.out.println(name + " will expire in 3 days.");
            // Notification logic can be implemented here
        }
    }

    public void fetchRecipeSuggestions(AIService aiService) {
    }
}
