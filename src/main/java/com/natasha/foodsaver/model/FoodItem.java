package com.natasha.foodsaver.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "foodItems")
public class FoodItem {

    @Id
    private String id;
    private String userId;
    private String name;
    private double quantity;
    private String unit;
    private LocalDate expirationDate;
    private String alertMessage;
    private List<String> allergens = new ArrayList<>();
    private boolean allergenNotificationSent = false;
    private boolean expiredNotified = false; // Nytt fält för att spåra utgångsnotifieringar
    private User user;

    // Getters och Setters

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getAlertMessage() {
        return alertMessage;
    }

    public void setAlertMessage(String alertMessage) {
        this.alertMessage = alertMessage;
    }

    public List<String> getAllergens() {
        return allergens;
    }

    public void setAllergens(List<String> allergens) {
        this.allergens = allergens;
    }

    public boolean isAllergenNotificationSent() {
        return allergenNotificationSent;
    }

    public void setAllergenNotificationSent(boolean allergenNotificationSent) {
        this.allergenNotificationSent = allergenNotificationSent;
    }

    public boolean isExpiredNotified() {
        return expiredNotified;
    }

    public void setExpiredNotified(boolean expiredNotified) {
        this.expiredNotified = expiredNotified;
    }

    // Metoder

    public boolean isExpirationNear() {
        // Kontrollera om utgångsdatum är nära (3 dagar eller mindre)
        long daysBetween = ChronoUnit.DAYS.between(LocalDate.now(), expirationDate);
        return daysBetween <= 3;
    }

    public boolean checkAllergies(List<String> userAllergies) {
        for (String allergen : allergens) {
            if (userAllergies.contains(allergen) && !allergenNotificationSent) {
                sendAllergenNotification(allergen);
                allergenNotificationSent = true;
                return true;
            }
        }
        return false;
    }

    private void sendAllergenNotification(String allergen) {
        System.out.println("Varning: " + name + " innehåller " + allergen + ", som du är allergisk mot.");
    }

    public boolean scheduleExpirationNotification() {
        LocalDate notificationDate = expirationDate.minusDays(3);
        long daysUntilNotification = ChronoUnit.DAYS.between(LocalDate.now(), notificationDate);

        if (daysUntilNotification <= 0 && !expiredNotified) {
            sendExpirationNotification();
            expiredNotified = true;
            return true;
        }
        return false;
    }

    private void sendExpirationNotification() {
        System.out.println(name + " går ut om 3 dagar. Använd den snart!");
    }
}
