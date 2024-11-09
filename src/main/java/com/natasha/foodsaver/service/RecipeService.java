package com.natasha.foodsaver.service;

import com.natasha.foodsaver.model.Recipe;
import com.natasha.foodsaver.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private AIService aiService; // Lägger till AIService för att generera recept

    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    public Recipe getRecipeById(String id) {
        return recipeRepository.findById(id).orElse(null);
    }

    public List<Recipe> generateRecipes(String ingredients, List<String> allergens, String dietaryPreferences, int servings) {
        // Generera recept via AIService och skicka med kostpreferenser och antal portioner
        List<Recipe> generatedRecipes = aiService.generateAIRecipes(ingredients, allergens, dietaryPreferences, servings);

        // Filtrera bort recept som redan finns i databasen baserat på namn
        List<String> existingRecipeNames = recipeRepository.findAll().stream()
                .map(Recipe::getName)
                .collect(Collectors.toList());

        // Returnera endast nya recept som inte redan finns i databasen
        return generatedRecipes.stream()
                .filter(recipe -> !existingRecipeNames.contains(recipe.getName()))
                .collect(Collectors.toList());
    }


    public Recipe updateRecipe(String id, Recipe recipe) {
        Recipe existingRecipe = recipeRepository.findById(id).orElse(null);
        if (existingRecipe != null) {
            // Uppdatera det existerande receptet med nya värden
            existingRecipe.setName(recipe.getName());
            existingRecipe.setInstructions(recipe.getInstructions());
            existingRecipe.setFoodItem(recipe.getFoodItem());
            existingRecipe.setUserId(recipe.getUserId());
            existingRecipe.setTitle(recipe.getTitle());
            existingRecipe.setDescription(recipe.getDescription());
            return recipeRepository.save(existingRecipe);
        }
        return null; // Om receptet inte finns, returnera null
    }

    public void deleteRecipe(String id) {
        recipeRepository.deleteById(id);
    }

}
