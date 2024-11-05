package com.natasha.foodsaver.model;

import java.util.List;

public class AIResponse {
    private List<Recipe> recipes; // List of recipes returned by the AI service

    // Standardkonstruktor
    public AIResponse() {
    }

    // Getter för recipes
    public List<Recipe> getRecipes() {
        return recipes;
    }

    // Setter för recipes
    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }
}
