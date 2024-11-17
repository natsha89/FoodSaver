package com.natasha.foodsaver.controller;

import com.natasha.foodsaver.model.Recipe;
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
    private RecipeService recipeService;  // Autowired RecipeService för att hantera receptlogik

    // Genererar nya recept baserat på angivna ingredienser, allergener, kostpreferenser och antal portioner
    @PostMapping("/generate")
    public ResponseEntity<List<Recipe>> generateRecipes(@RequestBody RecipeGenerationRequest request) {
        logger.info("Received request to generate recipes for user with ingredients: {}", request.getIngredients());

        try {
            // Assume the userId is included in the request body or can be extracted from a session or token
            String userId = request.getUserId();  // You may get this from the request, e.g. via an authenticated session

            // Call the RecipeService to generate recipes for the specific user
            List<Recipe> generatedRecipes = recipeService.generateRecipes(
                    userId,
                    request.getIngredients(),
                    request.getAllergens(),
                    request.getDietaryPreferences(),
                    request.getServings()
            );

            // Return generated recipes or a 204 No Content response if no recipes were generated
            if (generatedRecipes.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(generatedRecipes);

        } catch (IllegalArgumentException e) {
            logger.error("Invalid input: {}", e.getMessage());
            return ResponseEntity.badRequest().body(List.of(new Recipe("Error", "Invalid input")));
        } catch (Exception e) {
            logger.error("An error occurred while generating recipes: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(List.of(new Recipe("Error", "An internal server error occurred")));
        }
    }



    // Hämtar alla recept från databasen
    @GetMapping
    public ResponseEntity<List<Recipe>> getAllRecipes() {
        List<Recipe> recipes = recipeService.getAllRecipes();  // Anropa RecipeService för att hämta alla recept
        return ResponseEntity.ok(recipes);  // Returnera recepten som en OK-response
    }

    // Hämtar ett specifikt recept baserat på ID
    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable String id) {
        Recipe recipe = recipeService.getRecipeById(id);  // Hämta receptet baserat på ID
        return recipe != null ? ResponseEntity.ok(recipe) : ResponseEntity.notFound().build();  // Returnera receptet om det finns, annars 404 Not Found
    }

    // Tar bort ett recept baserat på ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable String id) {
        recipeService.deleteRecipe(id);  // Ta bort receptet från databasen
        return ResponseEntity.noContent().build();  // Returnera en "no content" response efter borttagning
    }

    // Request body-klass för att generera recept
    // Denna klass används för att fånga in data som skickas med POST-förfrågan för att generera recept
    public static class RecipeGenerationRequest {
        private String userId;  // User ID to associate the recipes with
        private String ingredients;  // Ingredienser för receptgenerering
        private List<String> allergens;  // Listan med allergener
        private String dietaryPreferences;  // Kostpreferenser (t.ex. vegetarisk, glutenfri)
        private int servings;  // Antal portioner

        // Getters och setters
        public String getIngredients() {
            return ingredients;
        }

        public void setIngredients(String ingredients) {
            this.ingredients = ingredients;
        }

        public List<String> getAllergens() {
            return allergens;
        }

        public void setAllergens(List<String> allergens) {
            this.allergens = allergens;
        }

        public String getDietaryPreferences() {
            return dietaryPreferences;
        }

        public void setDietaryPreferences(String dietaryPreferences) {
            this.dietaryPreferences = dietaryPreferences;
        }

        public int getServings() {
            return servings;
        }

        public void setServings(int servings) {
            this.servings = servings;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}