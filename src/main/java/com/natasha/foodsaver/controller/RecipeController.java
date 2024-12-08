package com.natasha.foodsaver.controller;

import com.natasha.foodsaver.model.Recipe;
import com.natasha.foodsaver.model.RecipeGenerationRequest;
import com.natasha.foodsaver.service.JwtService;
import com.natasha.foodsaver.service.RecipeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private static final Logger logger = LoggerFactory.getLogger(RecipeController.class);

    @Autowired
    private RecipeService recipeService;  // Inject RecipeService to handle the logic for creating and fetching recipes

    @Autowired
    private JwtService jwtService;  // Inject JwtService to handle JWT token operations

    // Endpoint to generate recipes based on ingredients, allergens, dietary preferences, and servings
    @PostMapping("/generate")
    public ResponseEntity<List<Recipe>> generateRecipes(@RequestHeader("Authorization") String token, @RequestBody RecipeGenerationRequest request) {
        logger.info("Received request to generate recipes for user with ingredients: {}", request.getIngredients());

        try {
            // Extract user ID from the token
            String userId = jwtService.extractUserIdFromToken(token);
            logger.info("Generating recipes for userId: {}", userId);

            // Call RecipeService to generate recipes based on the provided parameters
            List<Recipe> generatedRecipes = recipeService.generateRecipes(
                    userId,
                    request.getIngredients(),
                    request.getAllergens(),
                    request.getDietaryPreferences(),
                    request.getServings()
            );

            // If no recipes are generated, return a 204 No Content response
            if (generatedRecipes.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            // Return the generated recipes with a 201 Created response
            return ResponseEntity.status(HttpStatus.CREATED).body(generatedRecipes);

        } catch (IllegalArgumentException e) {
            // Log invalid input and return a bad request response with an error message
            logger.error("Invalid input: {}", e.getMessage());
            return ResponseEntity.badRequest().body(List.of(new Recipe("Error", "Invalid input", List.of())));
        } catch (Exception e) {
            // Log other errors and return an internal server error response (HTTP 500)
            logger.error("An error occurred while generating recipes: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(List.of(new Recipe("Error", "An internal server error occurred", List.of())));
        }
    }

    // Endpoint to fetch recipes for a user based on their ID from the token
    @GetMapping("/user")
    public ResponseEntity<?> getRecipesForUser(@RequestHeader("Authorization") String token) {
        // Extract user ID from JWT token
        String userId = jwtService.extractUserIdFromToken(token);

        logger.info("Fetching recipes for user with ID: {}", userId);

        try {
            // Fetch the recipes for the user from RecipeService
            List<Recipe> recipes = recipeService.getRecipesForUser(userId);

            // If no recipes are found, return No Content (HTTP 204)
            if (recipes.isEmpty()) {
                logger.info("No recipes found for user with ID: {}", userId);
                return ResponseEntity.noContent().build();
            }

            // Return the recipes as an OK (HTTP 200) response
            return ResponseEntity.ok(recipes); // Spring automatically serializes the objects as JSON

        } catch (Exception e) {
            // Log and handle errors when fetching the recipes
            logger.error("Error fetching recipes for user {}: {}", userId, e.getMessage(), e);

            // Return an internal server error (HTTP 500) response with an error message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An internal server error occurred while fetching recipes.");
        }
    }

    // Endpoint to fetch a specific recipe by its ID
    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable String id) {
        // Fetch the recipe based on the provided ID
        Recipe recipe = recipeService.getRecipeById(id);

        // If the recipe exists, return it as an OK response. If not, return 404 Not Found
        return recipe != null ? ResponseEntity.ok(recipe) : ResponseEntity.notFound().build();
    }

    // Endpoint to delete a specific recipe based on user ID and recipe ID
    @DeleteMapping("/{recipeId}")
    public ResponseEntity<String> deleteRecipe(@RequestHeader("Authorization") String token, @PathVariable String recipeId) {
        try {
            // Extract user ID from the token
            String userId = jwtService.extractUserIdFromToken(token);

            // Log the request to delete the recipe
            logger.info("Received request to delete recipe with ID: {} for user with ID: {}", recipeId, userId);

            // Call RecipeService to check if the user owns the recipe and delete it
            boolean deleted = recipeService.deleteRecipe(userId, recipeId);

            if (deleted) {
                // If the recipe is deleted, return a 200 OK response
                return ResponseEntity.ok("Recipe deleted successfully.");
            } else {
                // If the user is not authorized to delete the recipe, return a 403 Forbidden response
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("User is not authorized to delete this recipe.");
            }

        } catch (Exception e) {
            // Log other errors and return an internal server error response (HTTP 500)
            logger.error("An error occurred while deleting recipe {} for user {}: {}", recipeId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An internal server error occurred while deleting the recipe.");
        }
    }

    // Endpoint to fetch all recipes for a specific user
    @GetMapping
    public ResponseEntity<List<Recipe>> getAllRecipes(@RequestHeader("Authorization") String token) {
        // Extract user ID from the token
        String userId = jwtService.extractUserIdFromToken(token);

        // Fetch recipes for the specific user
        List<Recipe> recipes = recipeService.getRecipesForUser(userId);

        // If the recipe list is empty, return a 404 Not Found response
        if (recipes.isEmpty()) {
            logger.info("No recipes found for user with ID: {}", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // Return the found recipes as an OK response (HTTP 200)
        logger.info("Found {} recipes for user with ID: {}", recipes.size(), userId);
        return ResponseEntity.ok(recipes);
    }
}
