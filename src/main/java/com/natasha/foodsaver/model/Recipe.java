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
    private String title;
    private String instructions;
    private List<String> foodItemIds; // Update if necessary
    private String userId; // Links recipe to a user
}
