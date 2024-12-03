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
    private RecipeService recipeService;  // Injektar RecipeService för att hantera logiken för att skapa och hämta recept

    @Autowired
    private JwtService jwtService;

    // Endpoint för att generera nya recept baserat på ingredienser, allergener, kostpreferenser och antal portioner
    @PostMapping("/generate")
    public ResponseEntity<List<Recipe>> generateRecipes(@RequestHeader("Authorization") String token, @RequestBody RecipeGenerationRequest request) {
        logger.info("Received request to generate recipes for user with ingredients: {}", request.getIngredients());

        try {
            // 1. Extrahera användar-ID från token
            String userId = jwtService.extractUserIdFromToken(token);
            logger.info("Generating recipes for userId: {}", userId);

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
            return ResponseEntity.status(HttpStatus.CREATED).body(generatedRecipes);

        } catch (IllegalArgumentException e) {
            // Logga ogiltiga indata och returnera ett bad request-svar med felmeddelande
            logger.error("Invalid input: {}", e.getMessage());
            return ResponseEntity.badRequest().body(List.of(new Recipe("Error", "Invalid input", List.of())));
        } catch (Exception e) {
            // Logga andra fel och returnera ett internal server error-svar (HTTP 500)
            logger.error("An error occurred while generating recipes: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(List.of(new Recipe("Error", "An internal server error occurred", List.of())));
        }
    }

    @GetMapping("/user")
    public ResponseEntity<?> getRecipesForUser(@RequestHeader("Authorization") String token) {
        // Extrahera användar-ID från JWT-token
        String userId = jwtService.extractUserIdFromToken(token);

        logger.info("Fetching recipes for user with ID: {}", userId);

        try {
            // Hämta recepten för användaren från RecipeService
            List<Recipe> recipes = recipeService.getRecipesForUser(userId);

            // Om inga recept hittas, returnera No Content (HTTP 204)
            if (recipes.isEmpty()) {
                logger.info("No recipes found for user with ID: {}", userId);
                return ResponseEntity.noContent().build();
            }

            // Om recept hittades, returnera dem som OK (HTTP 200)
            return ResponseEntity.ok(recipes); // Spring serialiserar objekten som JSON automatiskt

        } catch (Exception e) {
            // Logga och hantera fel vid hämtning av recepten
            logger.error("Error fetching recipes for user {}: {}", userId, e.getMessage(), e);

            // Returnera ett internt serverfel (HTTP 500) med ett felmeddelande
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An internal server error occurred while fetching recipes.");
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
    @DeleteMapping("/{recipeId}")
    public ResponseEntity<String> deleteRecipe(@RequestHeader("Authorization") String token, @PathVariable String recipeId) {
        try {
            String userId = jwtService.extractUserIdFromToken(token);

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
            logger.error("An error occurred while deleting recipe {} for user {}: {}", recipeId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An internal server error occurred while deleting the recipe.");
        }
    }
    @GetMapping
    public ResponseEntity<List<Recipe>> getAllRecipes(@RequestHeader("Authorization") String token) {
        // Hämta userId från token
        String userId = jwtService.extractUserIdFromToken(token);

        // Hämta recept för den specifika användaren
        List<Recipe> recipes = recipeService.getRecipesForUser(userId);

        // Kontrollera om listan är tom och returnera lämpligt svar
        if (recipes.isEmpty()) {
            logger.info("No recipes found for user with ID: {}", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        logger.info("Found {} recipes for user with ID: {}", recipes.size(), userId);
        return ResponseEntity.ok(recipes);
    }
}