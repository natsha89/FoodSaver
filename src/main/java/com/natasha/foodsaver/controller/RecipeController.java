package com.natasha.foodsaver.controller;

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

    // Endpoint för att hämta alla recept från databasen
    @GetMapping
    public ResponseEntity<List<Recipe>> getAllRecipes() {
        List<Recipe> recipes = recipeService.getAllRecipes();  // Hämta alla recept från RecipeService
        return ResponseEntity.ok(recipes);  // Returnera recepten som en OK-response
    }

    // Endpoint för att hämta ett specifikt recept baserat på ID
    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable String id) {
        Recipe recipe = recipeService.getRecipeById(id);  // Hämta receptet baserat på det angivna ID:t
        // Om receptet finns, returnera det som en OK-svar. Om inte, returnera 404 Not Found
        return recipe != null ? ResponseEntity.ok(recipe) : ResponseEntity.notFound().build();
    }

    // Endpoint för att ta bort ett recept baserat på ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable String id) {
        recipeService.deleteRecipe(id);  // Ta bort receptet från databasen
        return ResponseEntity.noContent().build();  // Returnera en "no content" response (HTTP 204) som bekräftelse på borttagningen
    }
}
