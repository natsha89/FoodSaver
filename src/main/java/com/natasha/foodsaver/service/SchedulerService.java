package com.natasha.foodsaver.service;

import com.natasha.foodsaver.model.FoodItem;
import com.natasha.foodsaver.repository.FoodItemRepository;
import com.natasha.foodsaver.repository.UserRepository;  // Importera UserRepository för att hämta användardata
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SchedulerService {

    @Autowired
    private FoodItemRepository foodItemRepository; // Injektera FoodItemRepository för att hämta matvaror från databasen

    @Autowired
    private EmailService emailService; // Injektera EmailService för att skicka e-postmeddelanden

    @Autowired
    private UserRepository userRepository;  // Injektera UserRepository för att hämta användardata

    // Denna metod körs varje dag klockan 08:00 för att kolla matvaror som snart går ut
    @Scheduled(cron = "0 0 8 * * ?")
    public void notifyExpiringItems() {
        // Hämta alla matvaror från databasen
        List<FoodItem> foodItems = foodItemRepository.findAll();

        // Loopar genom alla matvaror
        for (FoodItem foodItem : foodItems) {
            // Om matvaran har ett utgångsdatum som är inom 7 dagar från idag, skicka en notifiering
            if (foodItem.isExpirationNear(7)) {
                // Hämta användarens e-postadress baserat på userId
                String userEmail = getUserEmail(foodItem.getUserId());

                // Skicka en notifiering om utgångsdatum till användaren
                emailService.sendExpirationNotification(userEmail, foodItem.getName(), foodItem.getExpirationDate());

                // Uppdatera matvarans flagga för att visa att notifieringen har skickats
                foodItem.setExpirationNotificationSent(true);
                foodItemRepository.save(foodItem);  // Spara matvaran med den uppdaterade notifieringsflaggan
            }
        }
    }

    // Metod för att hämta användarens e-postadress baserat på deras userId
    private String getUserEmail(String userId) {
        // Försök att hämta användaren från UserRepository och returnera e-postadressen
        return userRepository.findById(userId)
                .map(user -> user.getEmail())  // Om användaren finns, hämta e-postadressen
                .orElseThrow(() -> new RuntimeException("User not found for ID: " + userId));  // Om användaren inte finns, kasta ett undantag
    }
}
