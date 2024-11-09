package com.natasha.foodsaver.service;

import com.natasha.foodsaver.model.FoodItem;
import com.natasha.foodsaver.repository.FoodItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodItemService {

    @Autowired
    private FoodItemRepository foodItemRepository;

    @Autowired
    private AIService aiService;

    // Hämtar alla matvaror och kollar deras utgångsdatum
    public List<FoodItem> getAllFoodItems() {
        List<FoodItem> foodItems = foodItemRepository.findAll();

        // Kolla utgångsdatum och generera receptförslag
        for (FoodItem foodItem : foodItems) {
            // Kontrollera och skicka notifiering om förfallodatumet
            foodItem.scheduleExpirationNotification();
            // Kontrollera om allergener matchar användarens allergier
            foodItem.checkAllergies(getUserAllergies(foodItem.getUserId()));
        }

        return foodItems;
    }

    // Skapa en ny FoodItem
    public FoodItem createFoodItem(FoodItem foodItem) {
        // Kontrollera om allergener eller utgångsdatum finns
        boolean hasAllergens = foodItem.checkAllergies(getUserAllergies(foodItem.getUserId()));
        boolean isExpirationNear = foodItem.scheduleExpirationNotification();

        // Om ingen allergi eller utgångsdatum varning finns, generera recept
        if (!hasAllergens && !isExpirationNear) {
            foodItem.fetchRecipeSuggestions(aiService, "Any", 2);  // Exempel: Generera receptförslag för 2 portioner
        }

        return foodItemRepository.save(foodItem);
    }

    // Uppdatera en FoodItem
    public FoodItem updateFoodItem(String id, FoodItem foodItem) {
        FoodItem existingFoodItem = foodItemRepository.findById(id).orElse(null);
        if (existingFoodItem != null) {
            existingFoodItem.setName(foodItem.getName());
            existingFoodItem.setQuantity(foodItem.getQuantity());
            existingFoodItem.setUnit(foodItem.getUnit());
            existingFoodItem.setExpirationDate(foodItem.getExpirationDate());
            existingFoodItem.setAllergens(foodItem.getAllergens());
            existingFoodItem.setRecipeSuggestions(foodItem.getRecipeSuggestions());

            // Kontrollera allergener och utgångsdatum
            boolean hasAllergens = foodItem.checkAllergies(getUserAllergies(existingFoodItem.getUserId()));
            boolean isExpirationNear = foodItem.scheduleExpirationNotification();

            // Om ingen allergi eller utgångsdatum varning finns, generera recept
            if (!hasAllergens && !isExpirationNear) {
                existingFoodItem.fetchRecipeSuggestions(aiService, "Any", 2);  // Exempel: Generera receptförslag för 2 portioner
            }

            return foodItemRepository.save(existingFoodItem);
        }
        return null;
    }

    // Hämta användarens allergier
    public List<String> getUserAllergies(String userId) {
        // Detta kan ersättas med riktig användardata från din databas eller användargränssnitt
        return List.of("Peanuts", "Dairy"); // Detta är bara ett exempel
    }

    public void deleteFoodItem(String id) {
        foodItemRepository.deleteById(id);
    }
}
