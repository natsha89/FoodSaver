package com.natasha.foodsaver.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "recipes")
public class Recipe {

    @Id
    private String id;
    private String name;
    private String instructions;
    private List<String> foodItem; // Note: Changed from foodItems to foodItem for consistency with field name
    private String userId;
    private String title;
    private String description;

    // No-argument constructor
    public Recipe() {
    }

    // All-arguments constructor
    public Recipe(String id, String name, String instructions, List<String> foodItem,
                  String userId, String title, String description) {
        this.id = id;
        this.name = name;
        this.instructions = instructions;
        this.foodItem = foodItem;
        this.userId = userId;
        this.title = title;
        this.description = description;
    }

    // Constructor for name, instructions, and foodItems only
    public Recipe(String name, String instructions, List<String> foodItems) {
        this.name = name;
        this.instructions = instructions;
        this.foodItem = foodItems; // Allow setting food items if necessary
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public List<String> getFoodItem() {
        return foodItem;
    }

    public void setFoodItem(List<String> foodItem) {
        this.foodItem = foodItem;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // toString method
    @Override
    public String toString() {
        return "Recipe{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", instructions='" + instructions + '\'' +
                ", foodItem=" + foodItem +
                ", userId='" + userId + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
