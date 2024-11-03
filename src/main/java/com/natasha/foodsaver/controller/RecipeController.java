package com.natasha.foodsaver.controller;

import com.natasha.foodsaver.model.Recipe;
import com.natasha.foodsaver.model.User;
import com.natasha.foodsaver.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @GetMapping("/generate")
    public List<Recipe> generateRecipes(@RequestParam String ingredients, @AuthenticationPrincipal User user) {
        List<String> userAllergies = user.getAllergies();
        List<String> userPreferences = user.getDietaryPreferences();

        List<Recipe> aiRecipes = recipeService.generateAIRecipes(ingredients, userAllergies, userPreferences);
        List<Recipe> edamamRecipes = recipeService.searchRecipes(ingredients, userAllergies);

        aiRecipes.addAll(edamamRecipes);
        return aiRecipes;
    }


    @PostMapping("/save")
    public ResponseEntity<Recipe> saveGeneratedRecipe(@RequestBody Recipe recipe, @AuthenticationPrincipal User user) {
        recipe.setUserId(user.getId()); // Set the user ID
        Recipe savedRecipe = recipeService.saveRecipe(recipe);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRecipe);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable String id) {
        recipeService.deleteRecipe(id);
        return ResponseEntity.noContent().build();
    }
}
