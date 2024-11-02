package com.natasha.foodsaver.service;

import com.natasha.foodsaver.model.FoodItem;
import com.natasha.foodsaver.model.User;
import com.natasha.foodsaver.repository.FoodItemRepository;
import com.natasha.foodsaver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
                    System.out.println("Warning: " + foodItem.getName() + " contains " + allergen + " and may cause an allergic reaction.");
                }
            }
        }
        return foodItemRepository.save(foodItem);
    }

    public List<String> getUserAllergies(String userId) {
        User user = userRepository.findById(userId).orElse(null);
        return user != null ? user.getAllergies() : new ArrayList<>();
    }

    public List<FoodItem> getExpiringFoodItemsByUserId(String userId) {
        List<FoodItem> foodItems = getFoodItemsByUserId(userId);
        List<FoodItem> expiringItems = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 7);
        Date warningDate = calendar.getTime();

        for (FoodItem item : foodItems) {
            if (item.getExpirationDate() != null && item.getExpirationDate().before(warningDate)) {
                expiringItems.add(item);
            }
        }
        return expiringItems;
    }
}
