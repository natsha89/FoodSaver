package com.natasha.foodsaver.service;

import com.natasha.foodsaver.model.FoodItem;
import com.natasha.foodsaver.model.Notification;
import com.natasha.foodsaver.model.User;
import com.natasha.foodsaver.repository.FoodItemRepository;
import com.natasha.foodsaver.repository.NotificationRepository;
import com.natasha.foodsaver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private FoodItemRepository foodItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Scheduled(cron = "0 0 8 * * ?") // Körs varje dag kl. 08:00
    public void checkFoodItemExpirations() {
        List<FoodItem> allFoodItems = foodItemRepository.findAll();
        LocalDate today = LocalDate.now();

        for (FoodItem foodItem : allFoodItems) {
            User user = userRepository.findById(foodItem.getUserId()).orElse(null);
            if (user == null) continue;

            LocalDate expirationDate = foodItem.getExpirationDate();

            // Om matvaran har gått ut och ingen notifikation skickats
            if (expirationDate.isBefore(today) && !foodItem.isExpiredNotified()) {
                createNotification(user.getId(), "Food Expired: " + foodItem.getName(),
                        "The food item '" + foodItem.getName() + "' has expired.");
                foodItem.setExpiredNotified(true);
                foodItemRepository.save(foodItem);
            }

            // Om matvaran går ut imorgon
            if (expirationDate.isEqual(today.plusDays(1))) {
                createNotification(user.getId(), "Food Expiration Reminder: " + foodItem.getName(),
                        "The food item '" + foodItem.getName() + "' is expiring tomorrow.");
            }
        }
    }

    private void createNotification(String userId, String title, String message) {
        Notification notification = new Notification(userId, title, message, LocalDateTime.now());
        notificationRepository.save(notification);
    }

    public List<Notification> getNotificationByUserId(String userId) {
        return notificationRepository.findByUserId(userId);  // Hämtar alla matvaror som tillhör användaren

    }
}
