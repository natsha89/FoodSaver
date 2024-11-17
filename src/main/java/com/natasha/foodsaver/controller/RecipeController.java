package com.natasha.foodsaver.controller;

import com.natasha.foodsaver.model.FoodItem;
import com.natasha.foodsaver.model.Recipe;
import com.natasha.foodsaver.model.RecipeGenerationRequest;
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
    private RecipeService recipeService;  // Injektar RecipeService för att hantera logiken för att skapa och hämta recept

    // Endpoint för att generera nya recept baserat på ingredienser, allergener, kostpreferenser och antal portioner
    @PostMapping("/generate")
    public ResponseEntity<List<Recipe>> generateRecipes(@RequestBody RecipeGenerationRequest request) {
        logger.info("Received request to generate recipes for user with ingredients: {}", request.getIngredients());

        try {
            // Hämta userId från request (kan också komma från session eller token)
            String userId = request.getUserId();

            // Anropa RecipeService för att generera recept baserat på de angivna parametrarna
            List<Recipe> generatedRecipes = recipeService.generateRecipes(
                    userId,
                    request.getIngredients(),
                    request.getAllergens(),
                    request.getDietaryPreferences(),
                    request.getServings()
            );

            // Om inga recept genererades, returnera en 204 No Content-svar
            if (generatedRecipes.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            // Returnera de genererade recepten som en OK-svar (HTTP 200)
            return ResponseEntity.ok(generatedRecipes);

        } catch (IllegalArgumentException e) {
            // Logga ogiltiga indata och returnera ett bad request-svar med felmeddelande
            logger.error("Invalid input: {}", e.getMessage());
            return ResponseEntity.badRequest().body(List.of(new Recipe("Error", "Invalid input")));
        } catch (Exception e) {
            // Logga andra fel och returnera ett internal server error-svar (HTTP 500)
            logger.error("An error occurred while generating recipes: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(List.of(new Recipe("Error", "An internal server error occurred")));
        }
    }

    // Ny metod för att hämta alla recept för en specifik användare
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Recipe>> getRecipesForUser(@PathVariable String userId) {
        try {
            // Anropa RecipeService för att hämta alla recept för en specifik användare
            List<Recipe> recipes = recipeService.getRecipesForUser(userId);

            // Om inga recept hittades för användaren, returnera en 204 No Content-svar
            if (recipes.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            // Returnera recepten som en OK-svar (HTTP 200)
            return ResponseEntity.ok(recipes);

        } catch (Exception e) {
            // Logga eventuella fel och returnera ett internal server error-svar (HTTP 500)
            logger.error("Error occurred while fetching recipes for user {}: {}", userId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(List.of(new Recipe("Error", "An internal server error occurred")));
        }
    }

    // Endpoint för att hämta ett specifikt recept baserat på ID
    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable String id) {
        // Hämta receptet baserat på det angivna ID:t
        Recipe recipe = recipeService.getRecipeById(id);

        // Om receptet finns, returnera det som en OK-svar. Om inte, returnera 404 Not Found
        return recipe != null ? ResponseEntity.ok(recipe) : ResponseEntity.notFound().build();
    }

    // Ny metod för att radera ett specifikt recept baserat på användar-ID och recept-ID
    @DeleteMapping("/user/{userId}/{recipeId}")
    public ResponseEntity<String> deleteRecipe(@PathVariable String userId, @PathVariable String recipeId) {
        try {
            // Logga begäran om att radera receptet
            logger.info("Received request to delete recipe with ID: {} for user with ID: {}", recipeId, userId);

            // Anropa RecipeService för att kontrollera om användaren äger receptet
            boolean deleted = recipeService.deleteRecipe(userId, recipeId);

            if (deleted) {
                // Om receptet raderas, returnera en 200 OK-svar
                return ResponseEntity.ok("Recipe deleted successfully.");
            } else {
                // Om användaren inte kan radera receptet (t.ex. om de inte är ägare), returnera 403 Forbidden
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("User is not authorized to delete this recipe.");
            }

        } catch (Exception e) {
            // Logga andra fel och returnera ett internal server error-svar (HTTP 500)
            logger.error("An error occurred while deleting recipe {} for user {}: {}", recipeId, userId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An internal server error occurred while deleting the recipe.");
        }
    }

    // Endpoint för att hämta alla recept från databasen
    @GetMapping
    public ResponseEntity<List<Recipe>> getAllRecipes() {
        // Hämta alla recept från RecipeService
        List<Recipe> recipes = recipeService.getAllRecipes();

        // Returnera recepten som en OK-response (HTTP 200)
        return ResponseEntity.ok(recipes);
    }
}
