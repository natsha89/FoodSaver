package com.natasha.foodsaver.service;

import com.natasha.foodsaver.model.FoodItem;
import com.natasha.foodsaver.model.User;
import com.natasha.foodsaver.repository.FoodItemRepository;
import com.natasha.foodsaver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FoodItemService {

    @Autowired
    private FoodItemRepository foodItemRepository;

    @Autowired
    private UserRepository userRepository;

    public List<FoodItem> getAllFoodItems() {
        List<FoodItem> foodItems = foodItemRepository.findAll();

        for (FoodItem foodItem : foodItems) {
            foodItem.scheduleExpirationNotification();
            foodItem.checkAllergies(getUserAllergies(foodItem.getUserId()));
        }

        return foodItems;
    }

    // Method to create a new food item
    public FoodItem createFoodItem(FoodItem foodItem) {
        Optional<User> userOptional = userRepository.findById(foodItem.getUserId());
        if (!userOptional.isPresent()) {
            throw new IllegalArgumentException("User not found with ID: " + foodItem.getUserId());
        }

        boolean hasAllergens = foodItem.checkAllergies(getUserAllergies(foodItem.getUserId()));
        boolean isExpirationNear = foodItem.scheduleExpirationNotification();

        // If there are allergens or the expiration is near, return null
        if (hasAllergens || isExpirationNear) {
            throw new IllegalArgumentException("Food item contains allergens or expiration is near.");
        }

        // Set the user and save the food item
        User user = userOptional.get();
        foodItem.setUser(user);

        return foodItemRepository.save(foodItem);
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
