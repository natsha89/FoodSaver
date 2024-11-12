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
            List<Recipe> newRecipes = generatedRecipes.stream()
                    .filter(recipe -> !existingRecipeNames.contains(recipe.getName()))  // Bevara endast de recept som inte redan finns i databasen
                    .collect(Collectors.toList());

            // Spara nya recept i databasen
            recipeRepository.saveAll(newRecipes);

            // Returnera de sparade nya recepten
            return newRecipes;

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

    // Metod för att spara ett nytt recept till databasen
    public Recipe saveRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);  // Spara det nya receptet via repository
    }

    // Metod för att ta bort ett recept från databasen baserat på ID
    public void deleteRecipe(String id) {
        recipeRepository.deleteById(id);  // Ta bort receptet med angivet ID från databasen
    }
}
