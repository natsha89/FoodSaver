package com.natasha.foodsaver.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Document(collection = "foodItems")
public class FoodItem {

    @Id
    private String id; // Identifier for food item
    private String userId; // Link to user
    private String name; // Name of food item
    private double quantity; // Quantity of food item
    private String unit; // Unit (grams, pieces, etc.)
    private LocalDate expirationDate; // Expiration date
    private List<String> allergens; // Allergens associated with food item
    private List<String> recipeSuggestions; // Recipe suggestions based on the food item

    // No-argument constructor
    public FoodItem() {
    }

    // All-arguments constructor
    public FoodItem(String id, String userId, String name, double quantity, String unit,
                    LocalDate expirationDate, List<String> allergens, List<String> recipeSuggestions) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.expirationDate = expirationDate;
        this.allergens = allergens;
        this.recipeSuggestions = recipeSuggestions;
    }

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

    public List<String> getRecipeSuggestions() {
        return recipeSuggestions;
    }

    public void setRecipeSuggestions(List<String> recipeSuggestions) {
        this.recipeSuggestions = recipeSuggestions;
    }

    // toString method
    @Override
    public String toString() {
        return "FoodItem{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", unit='" + unit + '\'' +
                ", expirationDate=" + expirationDate +
                ", allergens=" + allergens +
                ", recipeSuggestions=" + recipeSuggestions +
                '}';
    }
}
