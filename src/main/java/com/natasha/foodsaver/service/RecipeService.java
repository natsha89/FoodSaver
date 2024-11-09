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
    private RecipeRepository recipeRepository;  // Autowired RecipeRepository för att interagera med databasen

    @Autowired
    private AIService aiService; // Lägger till AIService för att generera recept via AI

    // Metod för att generera recept baserat på ingredienser, allergener, kostpreferenser och antal portioner
    public List<Recipe> generateRecipes(String ingredients, List<String> allergens, String dietaryPreferences, int servings) {
        try {
            // Generera recept via AIService genom att skicka med ingredienser, allergener, kostpreferenser och antal portioner
            List<Recipe> generatedRecipes = aiService.generateAIRecipes(ingredients, allergens, dietaryPreferences, servings);

            // Hämta alla existerande receptnamn från databasen
            List<String> existingRecipeNames = recipeRepository.findAll().stream()
                    .map(Recipe::getName)  // Extrahera namn på alla recept
                    .collect(Collectors.toList());

            // Filtrera bort recept som redan finns i databasen baserat på namn
            return generatedRecipes.stream()
                    .filter(recipe -> !existingRecipeNames.contains(recipe.getName()))  // Bevara endast de recept som inte redan finns i databasen
                    .collect(Collectors.toList());
        } catch (Exception e) {
            // Logga eventuella fel
            System.err.println("Error generating recipes: " + e.getMessage());
            throw new RuntimeException("Error generating recipes", e); // Skicka vidare undantaget så det kan hanteras av controller
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

    // Metod för att uppdatera ett befintligt recept i databasen
    public Recipe updateRecipe(String id, Recipe recipe) {
        Recipe existingRecipe = recipeRepository.findById(id).orElse(null);  // Hitta det befintliga receptet med angivet ID
        if (existingRecipe != null) {
            // Uppdatera det befintliga receptet med nya värden
            existingRecipe.setName(recipe.getName());
            existingRecipe.setInstructions(recipe.getInstructions());
            existingRecipe.setFoodItem(recipe.getFoodItem());
            existingRecipe.setUserId(recipe.getUserId());
            existingRecipe.setTitle(recipe.getTitle());
            existingRecipe.setDescription(recipe.getDescription());
            return recipeRepository.save(existingRecipe);  // Spara det uppdaterade receptet i databasen
        }
        return null;  // Om receptet inte finns, returnera null
    }

    // Metod för att ta bort ett recept från databasen baserat på ID
    public void deleteRecipe(String id) {
        recipeRepository.deleteById(id);  // Ta bort receptet med angivet ID från databasen
    }
}
