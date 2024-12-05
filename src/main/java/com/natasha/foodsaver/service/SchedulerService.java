package com.natasha.foodsaver.service;

import com.natasha.foodsaver.model.FoodItem;
import com.natasha.foodsaver.model.Notification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class SchedulerService {

    @Autowired
    private FoodItemService foodItemService;

    @Autowired
    private NotificationService notificationService; // Lägg till notifieringstjänst

    // Körs varje dag kl. 08:00
    @Scheduled(cron = "0 0 8 * * *")
    public void checkFoodItemsExpiration() {
        List<FoodItem> foodItems = foodItemService.getAllFoodItems();
        for (FoodItem foodItem : foodItems) {
            // Kontrollera om utgångsdatumet är nära
            if (foodItem.isExpirationNear() && !foodItem.isExpirationNotificationSent()) {
                String expirationMessage = "Warning: " + foodItem.getName() + " will expire in 3 days.";
                notificationService.addNotification(expirationMessage);
                foodItem.setExpirationNotificationSent(true); // Markera att notifieringen är skickad
                foodItemService.updateFoodItem(foodItem.getId(), foodItem);
            }

            // Kontrollera om matvaran har passerat sitt utgångsdatum
            if (foodItem.getExpirationDate().isBefore(java.time.LocalDate.now()) && !foodItem.isExpirationNotificationSent()) {
                String expiredMessage = "Warning: " + foodItem.getName() + " has expired.";
                notificationService.addNotification(expiredMessage);
                foodItem.setExpirationNotificationSent(true); // Markera att notifieringen är skickad
                foodItemService.updateFoodItem(foodItem.getId(), foodItem);
            }

            // Kontrollera allergener
            List<String> userAllergies = getUserAllergies(foodItem.getUserId()); // Hämta användarens allergier
            if (foodItem.checkAllergies(userAllergies)) {
                foodItem.setAllergenNotificationSent(true); // Markera att allergen-notifiering är skickad
                foodItemService.updateFoodItem(foodItem.getId(), foodItem);
            }
        }
    }

    private List<String> getUserAllergies(String userId) {
        // Simulera att hämta användarens allergier från databasen
        return List.of("Gluten", "Laktos");
    }
}
