package com.natasha.foodsaver.model;

import com.natasha.foodsaver.service.AIService;
import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.CompletionResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

@Document(collection = "foodItems")
public class FoodItem {

    @Id
    private String id;
    private String userId;
    private String name;
    private double quantity;
    private String unit;
    private LocalDate expirationDate;
    private List<String> allergens;
    private List<Recipe> recipeSuggestions;  // Change to hold Recipe objects

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
    public void fetchRecipeSuggestions(AIService aiService) {
        // Pass ingredients and allergens to AI service to generate recipes
        String ingredients = String.join(", ", Arrays.asList(name)); // Assuming the food item's name as the ingredient for simplicity
        recipeSuggestions = aiService.generateRecipes(ingredients, allergens);
    }

    // Expiration Notification
    public void scheduleExpirationNotification() {
        LocalDate notificationDate = expirationDate.minusDays(3);
        long daysUntilNotification = Duration.between(LocalDate.now(), notificationDate).toDays();

        if (daysUntilNotification > 0) {
            // Notification logic here
            System.out.println(name + " will expire in 3 days.");
        }
    }
}
