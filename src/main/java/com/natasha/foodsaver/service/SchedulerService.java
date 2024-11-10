package com.natasha.foodsaver.service;

import com.natasha.foodsaver.model.FoodItem;
import com.natasha.foodsaver.repository.FoodItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SchedulerService {

    @Autowired
    private FoodItemRepository foodItemRepository;

    @Autowired
    private EmailService emailService;

    // Runs every day at 8 AM to check for items close to expiration
    @Scheduled(cron = "0 0 8 * * ?")
    public void notifyExpiringItems() {
        List<FoodItem> foodItems = foodItemRepository.findAll();

        for (FoodItem foodItem : foodItems) {
            if (foodItem.isExpirationNear(7)) {
                String userEmail = getUserEmail(foodItem.getUserId());
                String message = "Your item " + foodItem.getName() + " will expire soon. Please use it within 7 days.";
                emailService.sendExpirationNotification(userEmail, message);

                // Update the notification flag
                foodItem.setExpirationNotificationSent(true);
                foodItemRepository.save(foodItem);
            }
        }
    }

    private String getUserEmail(String userId) {
        // Placeholder for getting the user email, replace with actual user retrieval
        return "user@example.com";
    }
}
