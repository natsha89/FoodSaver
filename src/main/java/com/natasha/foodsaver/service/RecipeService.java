package com.natasha.foodsaver.service;

import com.natasha.foodsaver.model.Recipe;
import com.natasha.foodsaver.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private AIService aiService; // Lägger till AIService för att generera recept

    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    public Recipe getRecipeById(String id) {
        return recipeRepository.findById(id).orElse(null);
    }

    public Recipe createRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    public Recipe updateRecipe(String id, Recipe recipe) {
        Recipe existingRecipe = recipeRepository.findById(id).orElse(null);
        if (existingRecipe != null) {
            existingRecipe.setName(recipe.getName());
            existingRecipe.setInstructions(recipe.getInstructions());
            existingRecipe.setFoodItem(recipe.getFoodItem());
            existingRecipe.setUserId(recipe.getUserId());
            existingRecipe.setTitle(recipe.getTitle());
            existingRecipe.setDescription(recipe.getDescription());
            return recipeRepository.save(existingRecipe);
        }
        return null;
    }

    public void deleteRecipe(String id) {
        recipeRepository.deleteById(id);
    }

    public List<Recipe> generateRecipes(String ingredients, List<String> allergens) {
        return aiService.generateRecipes(ingredients, allergens);  // Generera recept via AIService
    }
}
