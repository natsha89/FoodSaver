package com.natasha.foodsaver.service;

import com.natasha.foodsaver.model.FoodItem;
import com.natasha.foodsaver.model.User;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public String processNotifications(FoodItem foodItem, User user) {
        StringBuilder notificationMessage = new StringBuilder();

        checkAllergies(foodItem, user, notificationMessage);
        checkExpiration(foodItem, notificationMessage);

        return notificationMessage.toString();
    }

    private void checkAllergies(FoodItem foodItem, User user, StringBuilder notificationMessage) {
        if (foodItem.checkAllergies(user.getAllergies())) {
            notificationMessage.append("Warning: The food item contains allergens you are allergic to. ");
        }
    }

    private void checkExpiration(FoodItem foodItem, StringBuilder notificationMessage) {
        if (foodItem.scheduleExpirationNotification()) {
            notificationMessage.append("Notice: The food item is nearing its expiration date.");
        }
    }
}
