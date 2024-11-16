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

    public List<FoodItem> getAllFoodItems() {
        List<FoodItem> foodItems = foodItemRepository.findAll();

        for (FoodItem foodItem : foodItems) {
            foodItem.scheduleExpirationNotification();
            foodItem.checkAllergies(getUserAllergies(foodItem.getUserId()));
        }

        return foodItems;
    }

    public FoodItem createFoodItem(FoodItem foodItem) {
        boolean hasAllergens = foodItem.checkAllergies(getUserAllergies(foodItem.getUserId()));
        boolean isExpirationNear = foodItem.scheduleExpirationNotification();

        if (!hasAllergens && !isExpirationNear) {
            return foodItemRepository.save(foodItem);
        } else {
            return null;  // Om allergener eller utgångsdatum är ett problem, returnera null
        }
    }

    public List<String> getUserAllergies(String userId) {
        // Exempel på användarallergier (kan ersättas med riktig användardata)
        return List.of("Peanuts", "Dairy");  // Exempel
    }

    public void deleteFoodItem(String id) {
        foodItemRepository.deleteById(id);
    }

    public FoodItem updateFoodItem(String id, FoodItem foodItem) {
        if (foodItemRepository.existsById(id)) {
            return foodItemRepository.save(foodItem);
        }
        return null;
    }
}
