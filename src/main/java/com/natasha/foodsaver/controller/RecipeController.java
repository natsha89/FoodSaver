package com.natasha.foodsaver.controller;

import com.natasha.foodsaver.model.Recipe;
import com.natasha.foodsaver.service.RecipeService;
import com.natasha.foodsaver.service.EdamamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private EdamamService edamamService;

    @GetMapping("/{userId}")
    public List<Recipe> getRecipes(@PathVariable String userId) {
        return recipeService.getRecipesByUserId(userId);
    }

    @PostMapping
    public Recipe saveRecipe(@RequestBody Recipe recipe) {
        return recipeService.saveRecipe(recipe);
    }

    @DeleteMapping("/{recipeId}")  // New endpoint for deleting a recipe
    public void deleteRecipe(@PathVariable String recipeId) {
        recipeService.deleteRecipe(recipeId);
    }

    @GetMapping("/search")
    public List<Recipe> searchRecipes(@RequestParam String query, @RequestParam(required = false) List<String> allergies) {
        return edamamService.searchRecipes(query, allergies);
    }
}
