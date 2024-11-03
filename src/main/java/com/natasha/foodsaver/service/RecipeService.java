package com.natasha.foodsaver.service;

import com.natasha.foodsaver.model.Recipe;
import com.natasha.foodsaver.repository.RecipeRepository; // Assume a repository exists
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeService {

    @Autowired
    private AIService aiService;

    @Autowired
    private EdamamService edamamService;

    @Autowired
    private RecipeRepository recipeRepository;

    // Existing method for generating recipes from AI
    public List<Recipe> generateAIRecipes(String ingredients) {
        return aiService.generateRecipes(ingredients);
    }

    // Existing method for searching recipes
    public List<Recipe> searchRecipes(String query, List<String> allergies) {
        return edamamService.searchRecipes(query, allergies);
    }

    // Existing method for saving recipes
    public Recipe saveRecipe(Recipe recipe) {
        return recipeRepository.save(recipe); // Save recipe to database
    }

    // Existing method for deleting recipes
    public void deleteRecipe(String id) {
        recipeRepository.deleteById(id); // Delete recipe by ID
    }

    // New method to generate recipes based on ingredients, user allergies, and preferences
    public List<Recipe> generateAIRecipes(String ingredients, List<String> userAllergies, List<String> userPreferences) {
        // Generate the basic recipes from AI based on the ingredients
        List<Recipe> aiRecipes = aiService.generateRecipes(ingredients);

        // Filter the recipes based on user allergies and dietary preferences
        List<Recipe> filteredRecipes = aiRecipes.stream()
                .filter(recipe -> isRecipeSuitable(recipe, userAllergies, userPreferences))
                .toList();

        return filteredRecipes;
    }

    // Helper method to check if a recipe is suitable for the user
    private boolean isRecipeSuitable(Recipe recipe, List<String> userAllergies, List<String> userPreferences) {
        // Check for allergens
        for (String allergen : userAllergies) {
            if (recipeContainsIngredient(recipe, allergen)) {
                return false; // Recipe contains an allergen
            }
        }

        // Check for dietary preferences
        // This part needs to be customized based on how you define dietary preferences
        // For example, if userPreferences contains "vegan", ensure there are no animal products in the recipe.
        if (userPreferences != null) {
            for (String preference : userPreferences) {
                if (!isPreferenceMet(recipe, preference)) {
                    return false; // Recipe does not meet dietary preferences
                }
            }
        }

        return true; // Recipe is suitable
    }

    // Method to check if a recipe contains a specific allergen
    private boolean recipeContainsIngredient(Recipe recipe, String ingredient) {
        // Assuming recipe has a list of foodItemIds, you will need to get food items from a relevant service/repository
        // For demonstration, I'll just use a simple placeholder logic here.
        return recipe.getFoodItem().stream().anyMatch(foodItemId -> foodItemId.equalsIgnoreCase(ingredient));
    }

    // Placeholder method to check dietary preferences
    private boolean isPreferenceMet(Recipe recipe, String preference) {
        // This should be expanded based on your dietary restrictions logic
        switch (preference.toLowerCase()) {
            case "vegan":
                // Check if the recipe contains any non-vegan ingredients
                // Add your logic here
                return true; // Placeholder
            case "vegetarian":
                // Check if the recipe contains any non-vegetarian ingredients
                return true; // Placeholder
            case "keto":
                // Check for keto compliance
                return true; // Placeholder
            // Add more dietary checks as needed
            default:
                return true; // If preference is not recognized, allow the recipe
        }
    }
}
