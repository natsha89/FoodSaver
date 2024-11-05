
package com.natasha.foodsaver.service;

import com.natasha.foodsaver.model.FoodItem;
import com.natasha.foodsaver.model.User;
import com.natasha.foodsaver.repository.FoodItemRepository;
import com.natasha.foodsaver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FoodItemService {

    @Autowired
    private FoodItemRepository foodItemRepository;

    @Autowired
    private UserRepository userRepository;

    public List<FoodItem> getFoodItemsByUserId(String userId) {
        return foodItemRepository.findByUserId(userId);
    }

    public FoodItem saveFoodItem(FoodItem foodItem) {
        User user = userRepository.findById(foodItem.getUserId()).orElse(null);
        if (user != null) {
            for (String allergen : foodItem.getAllergens()) {
                if (user.getAllergies().contains(allergen)) {
                    System.out.println("Warning: " + foodItem.getName() + " contains allergens: " + allergen);
                }
            }
            return foodItemRepository.save(foodItem);
        }
        return null;
    }

    public List<FoodItem> getAllFoodItems() {
        return foodItemRepository.findAll();
    }
}
