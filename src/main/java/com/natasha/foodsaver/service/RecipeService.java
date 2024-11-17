package com.natasha.foodsaver.service;

import com.natasha.foodsaver.model.Recipe;
import com.natasha.foodsaver.model.User;
import com.natasha.foodsaver.repository.RecipeRepository;
import com.natasha.foodsaver.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RecipeService {
    private static final Logger logger = LoggerFactory.getLogger(RecipeService.class);

    // Autowired för att injicera beroenden i denna klass
    @Autowired
    private RecipeRepository recipeRepository;  // Receptdatabas-repository för att interagera med recepten i databasen

    @Autowired
    private UserRepository userRepository;  // Användardatabas-repository för att hämta användarinformation

    @Autowired
    private AIService aiService; // Lägger till AIService för att generera recept via AI


    // Metod för att generera recept för en specifik användare baserat på ingredienser, allergener, kostpreferenser och portioner
    public List<Recipe> generateRecipes(String userId, String ingredients, List<String> allergens, String dietaryPreferences, int servings) {
        // Hämtar användaren baserat på userId
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found with ID: " + userId); // Om användaren inte hittas, kasta ett undantag
        }

        try {
            // Anropar AI-tjänsten för att generera recept med de angivna ingredienserna, allergenerna, kostpreferenserna och portionerna
            List<Recipe> generatedRecipes = aiService.generateAIRecipes(ingredients, allergens, dietaryPreferences, servings);

            // Hämtar alla befintliga receptnamn från databasen för att göra en effektiv uppslagning
            Set<String> existingRecipeNames = recipeRepository.findAll().stream()
                    .map(Recipe::getName)  // Extrahera alla receptnamn från databasen
                    .collect(Collectors.toSet());

            // Filtrera bort recept som redan finns i databasen baserat på namn
            List<Recipe> newRecipes = generatedRecipes.stream()
                    .filter(newRecipe -> !existingRecipeNames.contains(newRecipe.getName()))  // Bevara endast nya recept som inte redan finns
                    .collect(Collectors.toList());

            // Sätt användarens ID för varje nytt recept för att koppla det till rätt användare
            User user = userOptional.get();  // Hämtar användaren från Optional
            newRecipes.forEach(newRecipe -> newRecipe.setUserId(user.getId()));  // Kopplar användarens ID till recepten

            // Spara de nya recepten i databasen
            List<Recipe> savedRecipes = recipeRepository.saveAll(newRecipes);

            return savedRecipes; // Returnera listan av de nyligen skapade recepten
        } catch (Exception e) {
            logger.error("Error generating recipes for user {}: {}", userId, e.getMessage(), e);  // Logga eventuella fel som inträffar
            throw new RuntimeException("Error generating recipes for user " + userId, e); // Kasta ett undantag för att indikera att något gick fel
        }
    }


    // Metod för att hämta alla recept från databasen
    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();  // Hämtar alla recept från databasen
    }

    // Metod för att hämta ett specifikt recept baserat på ID
    public Recipe getRecipeById(String id) {
        return recipeRepository.findById(id).orElse(null);  // Hämtar receptet om det finns, annars returneras null
    }

    // Metod för att hämta alla recept som är kopplade till en specifik användare
    public List<Recipe> getRecipesForUser(String userId) {
        return recipeRepository.findByUserId(userId);  // Hämtar alla recept som tillhör användaren med det angivna userId
    }

    // Metod för att ta bort ett recept från databasen baserat på receptets ID
    public void deleteRecipe(String id) {
        recipeRepository.deleteById(id);  // Tar bort receptet från databasen med det angivna ID:t
    }
}
