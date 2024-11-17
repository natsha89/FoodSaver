package com.natasha.foodsaver.service;

import com.natasha.foodsaver.model.Recipe;
import com.natasha.foodsaver.model.User;
import com.natasha.foodsaver.repository.RecipeRepository;
import com.natasha.foodsaver.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RecipeService {
    private static final Logger logger = LoggerFactory.getLogger(RecipeService.class);


    @Autowired
    private RecipeRepository recipeRepository;  // Autowired RecipeRepository för att interagera med databasen

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AIService aiService; // Lägger till AIService för att generera recept via AI

        // Method for generating recipes for a specific user based on ingredients, allergens, dietary preferences, and servings
        public List<Recipe> generateRecipes(String userId, String ingredients, List<String> allergens, String dietaryPreferences, int servings) {
            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isEmpty()) {
                throw new IllegalArgumentException("User not found with ID: " + userId);
            }

            try {
                // Generate recipes using AI service with provided ingredients, allergens, dietary preferences, and servings
                List<Recipe> generatedRecipes = aiService.generateAIRecipes(ingredients, allergens, dietaryPreferences, servings);

                // Fetch all existing recipe names from the database and store them in a Set for efficient lookups
                Set<String> existingRecipeNames = recipeRepository.findAll().stream()
                        .map(Recipe::getName)  // Extract names of all recipes
                        .collect(Collectors.toSet());

                // Filter out recipes that already exist in the database by name
                List<Recipe> newRecipes = generatedRecipes.stream()
                        .filter(newRecipe -> !existingRecipeNames.contains(newRecipe.getName()))  // Keep only non-existing recipes
                        .collect(Collectors.toList());

                // Set the userId for each new recipe to associate it with the correct user
                User user = userOptional.get();
                newRecipes.forEach(newRecipe -> newRecipe.setUserId(user.getId()));

                // Save new recipes to the database
                List<Recipe> savedRecipes = recipeRepository.saveAll(newRecipes);

                return savedRecipes; // Return the list of newly created recipes
            } catch (Exception e) {
                logger.error("Error generating recipes for user {}: {}", userId, e.getMessage(), e);
                throw new RuntimeException("Error generating recipes for user " + userId, e); // Propagate the exception
            }
        }


    // Metod för att hämta alla recept från databasen
    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();  // Hämta alla recept från databasen
    }

    // Metod för att hämta ett specifikt recept baserat på ID
    public Recipe getRecipeById(String id) {
        return recipeRepository.findById(id).orElse(null);  // Hämta receptet om det finns, annars returnera null
    }

    // Method to get all saved recipes for a specific user
    public List<Recipe> getRecipesForUser(String userId) {
        return recipeRepository.findByUserId(userId);  // Fetch recipes by userId
    }

    // Metod för att ta bort ett recept från databasen baserat på ID
    public void deleteRecipe(String id) {
        recipeRepository.deleteById(id);  // Ta bort receptet med angivet ID från databasen
    }
}