package com.natasha.foodsaver.service;

import com.natasha.foodsaver.model.FoodItem;
import com.natasha.foodsaver.model.User;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public String processNotifications(FoodItem foodItem, User user) {
        StringBuilder alertMessage = new StringBuilder();

        // Kontrollera allergier
        if (foodItem.checkAllergies(user.getAllergies())) {
            alertMessage.append("Varning: Matvaran innehåller allergener som du är allergisk mot. ");
        }

        // Kontrollera utgångsdatum
        if (foodItem.scheduleExpirationNotification()) {
            alertMessage.append("Notis: Matvarans utgångsdatum är nära.");
        }

        return alertMessage.toString();
    }
}
