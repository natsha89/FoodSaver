package com.natasha.foodsaver.controller;

import com.natasha.foodsaver.model.Recipe;
import com.natasha.foodsaver.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;  // Autowired RecipeService för att hantera receptlogik

    // Genererar nya recept baserat på angivna ingredienser, allergener, kostpreferenser och antal portioner
    @PostMapping("/generate")
    public ResponseEntity<List<Recipe>> generateRecipes(@RequestBody RecipeGenerationRequest request) {
        try {
            // Logga för att kontrollera mottagna ingredienser
            System.out.println("Received request with ingredients: " + request.getIngredients());
            // Generera recept via RecipeService
            List<Recipe> generatedRecipes = recipeService.generateRecipes(
                    request.getIngredients(),
                    request.getAllergens(),
                    request.getDietaryPreferences(),
                    request.getServings()
            );
            // Returnera de genererade recepten, eller en "no content" response om inga recept hittades
            return generatedRecipes.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(generatedRecipes);
        } catch (Exception e) {
            // Logga eventuella fel och returnera en serverfel-respons
            System.err.println("An error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Skapar och sparar ett nytt recept
    @PostMapping
    public ResponseEntity<Recipe> saveRecipe(@RequestBody Recipe recipe) {
        try {
            // Spara receptet via RecipeService
            Recipe savedRecipe = recipeService.saveRecipe(recipe);
            // Returnera det sparade receptet med en OK-respons
            return ResponseEntity.status(HttpStatus.CREATED).body(savedRecipe);
        } catch (Exception e) {
            // Logga eventuella fel och returnera en serverfel-respons
            System.err.println("An error occurred while saving the recipe: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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
    }
}
