package com.natasha.foodsaver.service;

import com.natasha.foodsaver.model.FoodItem;
import com.natasha.foodsaver.repository.FoodItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private FoodItemRepository foodItemRepository;

    // Number of days before expiration to notify the user
    private static final int NOTIFICATION_DAYS = 3;

    @Scheduled(cron = "0 0 12 * * ?") // Check daily at noon
    public void notifyUsersOfExpiringItems() {
        Date today = new Date();
        Date notificationDate = new Date(today.getTime() + (NOTIFICATION_DAYS * 24 * 60 * 60 * 1000));

        // Fetch food items that will expire within the notification window
        List<FoodItem> expiringItems = foodItemRepository.findByExpirationDateBetween(today, notificationDate);

        // Notify users
        for (FoodItem item : expiringItems) {
            // Here you would implement your notification logic (e.g., email, push notifications)
            System.out.println("Notification: The food item " + item.getName() + " is expiring on " + item.getExpirationDate());
        }
    }

    public List<FoodItem> getExpiringFoodItems() {
        Date today = new Date();
        Date notificationDate = new Date(today.getTime() + (NOTIFICATION_DAYS * 24 * 60 * 60 * 1000));

        return foodItemRepository.findByExpirationDateBetween(today, notificationDate);
    }
}
