package com.natasha.foodsaver.service;

import com.natasha.foodsaver.model.FoodItem;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class SchedulerService {

    @Autowired
    private FoodItemService foodItemService; // Din tjänst för att hantera matvaror

    // Den här metoden kommer att köras varje dag kl. 08:00
    @Scheduled(cron = "0 0 8 * * *")
    public void checkFoodItemsExpiration() {
        List<FoodItem> foodItems = foodItemService.getAllFoodItems(); // Hämta alla matvaror
        for (FoodItem foodItem : foodItems) {
            // Kontrollera om utgångsdatumet är nära
            if (foodItem.isExpirationNear()) {
                String alertMessage = "Matvarans utgångsdatum närmar sig: " + foodItem.getExpirationDate();
                // Skapa en notifiering (det här kan anpassas för att spara notifieringar i en databas eller skicka meddelanden)
                foodItem.setAlertMessage(alertMessage);
                foodItemService.updateFoodItem(foodItem.getId(), foodItem); // Uppdatera matvaran med notifieringen
            }
        }
    }
}
