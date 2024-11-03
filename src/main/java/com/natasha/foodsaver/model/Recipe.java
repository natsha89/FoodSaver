package com.natasha.foodsaver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "recipes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Recipe {
    @Id
    private String id;
    private String name;
    private String instructions;
    private List<String> foodItem;
    private String userId;
    private String title;
    private String description;


    public Recipe(String name, String instructions, List<String> foodItems) {
        this.name = name;
        this.instructions = instructions;
        this.foodItem = foodItems; // Allow setting food items if necessary
    }
}