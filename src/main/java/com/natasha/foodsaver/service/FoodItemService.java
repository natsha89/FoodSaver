package com.natasha.foodsaver.service;

import com.natasha.foodsaver.model.FoodItem;
import com.natasha.foodsaver.repository.FoodItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodItemService {

    @Autowired
    private FoodItemRepository foodItemRepository; // Repository för att hantera FoodItem i databasen

    @Autowired
    private AIService aiService; // Injection av AIService för att få receptförslag

    // Hämta alla FoodItems från databasen
    public List<FoodItem> getAllFoodItems() {
        return foodItemRepository.findAll(); // Hämtar alla matvaror från databasen
    }

    // Hämta en FoodItem baserat på dess ID
    public FoodItem getFoodItemById(String id) {
        return foodItemRepository.findById(id).orElse(null); // Returnera FoodItem eller null om inte hittas
    }

    // Skapa en ny FoodItem
    public FoodItem createFoodItem(FoodItem foodItem) {
        // Hämta receptförslag från AI-tjänsten och associera med matvaran
        foodItem.fetchRecipeSuggestions(aiService);

        // Schemalägg en påminnelse om matvarans utgångsdatum
        foodItem.scheduleExpirationNotification();

        // Kontrollera om matvaran innehåller allergener baserat på användarens allergier
        foodItem.checkAllergies(getUserAllergies(foodItem.getUserId()));

        // Spara FoodItem till databasen och returnera den
        return foodItemRepository.save(foodItem);
    }

    // Uppdatera en befintlig FoodItem
    public FoodItem updateFoodItem(String id, FoodItem foodItem) {
        FoodItem existingFoodItem = foodItemRepository.findById(id).orElse(null); // Hämta den befintliga matvaran
        if (existingFoodItem != null) { // Om matvaran finns
            // Uppdatera egenskaper på den befintliga matvaran med nya värden
            existingFoodItem.setName(foodItem.getName());
            existingFoodItem.setQuantity(foodItem.getQuantity());
            existingFoodItem.setUnit(foodItem.getUnit());
            existingFoodItem.setExpirationDate(foodItem.getExpirationDate());
            existingFoodItem.setAllergens(foodItem.getAllergens());
            existingFoodItem.setRecipeSuggestions(foodItem.getRecipeSuggestions());

            // Hämta nya receptförslag om matvaran uppdateras
            existingFoodItem.fetchRecipeSuggestions(aiService);

            // Schemalägg påminnelse för utgångsdatum
            existingFoodItem.scheduleExpirationNotification();

            // Kontrollera allergier baserat på användarens allergier
            existingFoodItem.checkAllergies(getUserAllergies(existingFoodItem.getUserId()));

            // Spara den uppdaterade matvaran till databasen och returnera den
            return foodItemRepository.save(existingFoodItem);
        }
        return null; // Returnera null om matvaran inte hittades
    }

    // Ta bort en FoodItem baserat på dess ID
    public void deleteFoodItem(String id) {
        foodItemRepository.deleteById(id); // Ta bort matvaran från databasen
    }

    // Metod som simulerar hämtning av användarens allergier (detta bör ersättas med verklig logik)
    private List<String> getUserAllergies(String userId) {
        // För närvarande returneras en hårdkodad lista av allergier som exempel
        return List.of("Peanuts", "Dairy"); // Detta bör ersättas med verkliga användardata
    }
}
