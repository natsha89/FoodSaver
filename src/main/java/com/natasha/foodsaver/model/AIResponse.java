package com.natasha.foodsaver.model;

import lombok.Data;

import java.util.List;

@Data
public class AIResponse {
    private List<Recipe> recipes; // List of recipes returned by the AI service

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }
}
