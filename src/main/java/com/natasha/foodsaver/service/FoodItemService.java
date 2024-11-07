package com.natasha.foodsaver.service;

import com.natasha.foodsaver.model.FoodItem;
import com.natasha.foodsaver.model.Recipe;
import com.natasha.foodsaver.repository.FoodItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodItemService {

    @Autowired
    private FoodItemRepository foodItemRepository;

    @Autowired
    private AIService aiService; // Inject AIService

    public List<FoodItem> getAllFoodItems() {
        return foodItemRepository.findAll();
    }

    public FoodItem getFoodItemById(String id) {
        return foodItemRepository.findById(id).orElse(null);
    }

    public FoodItem createFoodItem(FoodItem foodItem) {
        // Fetch recipe suggestions from AI service
        foodItem.fetchRecipeSuggestions(aiService);
        // Other logic as before
        foodItem.scheduleExpirationNotification();
        foodItem.checkAllergies(getUserAllergies(foodItem.getUserId()));
        return foodItemRepository.save(foodItem);
    }

    public FoodItem updateFoodItem(String id, FoodItem foodItem) {
        FoodItem existingFoodItem = foodItemRepository.findById(id).orElse(null);
        if (existingFoodItem != null) {
            existingFoodItem.setName(foodItem.getName());
            existingFoodItem.setQuantity(foodItem.getQuantity());
            existingFoodItem.setUnit(foodItem.getUnit());
            existingFoodItem.setExpirationDate(foodItem.getExpirationDate());
            existingFoodItem.setAllergens(foodItem.getAllergens());
            existingFoodItem.setRecipeSuggestions(foodItem.getRecipeSuggestions());
            existingFoodItem.fetchRecipeSuggestions(aiService); // Re-fetch recipe suggestions if updated
            existingFoodItem.scheduleExpirationNotification();
            existingFoodItem.checkAllergies(getUserAllergies(existingFoodItem.getUserId()));
            return foodItemRepository.save(existingFoodItem);
        }
        return null;
    }

    public void deleteFoodItem(String id) {
        foodItemRepository.deleteById(id);
    }

    private List<String> getUserAllergies(String userId) {
        // Return a mock list of allergies (this should be replaced with actual logic)
        return List.of("Peanuts", "Dairy");
    }
}
