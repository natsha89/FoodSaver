package com.natasha.foodsaver.service;

import com.natasha.foodsaver.model.FoodItem;
import com.natasha.foodsaver.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodItemAlertService {

    public String getAllergyAlert(FoodItem foodItem, List<String> userAllergies) {
        return foodItem.checkAllergies(userAllergies) ?
                "Warning: The food item contains allergens you are allergic to. " : "";
    }

    public String getExpirationAlert(FoodItem foodItem) {
        return foodItem.scheduleExpirationNotification() ?
                "Notice: The food item is nearing its expiration date." : "";
    }
}
