package com.natasha.foodsaver.model;

import com.natasha.foodsaver.service.AIService;
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
    private List<String> allergens = new ArrayList<>();
    private boolean expirationNotificationSent = false;
    private boolean allergenNotificationSent = false;


    // Getters och Setters
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

    public List<String> getAllergens() {
        return allergens;
    }

    public void setAllergens(List<String> allergens) {
        this.allergens = allergens;
    }

    public boolean isExpirationNotificationSent() {
        return expirationNotificationSent;
    }

    public void setExpirationNotificationSent(boolean expirationNotificationSent) {
        this.expirationNotificationSent = expirationNotificationSent;
    }

    public boolean isAllergenNotificationSent() {
        return allergenNotificationSent;
    }

    public void setAllergenNotificationSent(boolean allergenNotificationSent) {
        this.allergenNotificationSent = allergenNotificationSent;
    }

    // Kontrollera om matvaran innehåller allergener som användaren har
    public boolean checkAllergies(List<String> userAllergies) {
        for (String allergen : allergens) {
            if (userAllergies.contains(allergen) && !allergenNotificationSent) {
                sendAllergenNotification(allergen);
                allergenNotificationSent = true;
                return true; // Returnera true om allergen hittas
            }
        }
        return false; // Ingen allergen hittades
    }

    private void sendAllergenNotification(String allergen) {
        System.out.println("Alert: " + name + " contains " + allergen + ", which you're allergic to.");
    }

    // Schemalägg förfallovarsel
    public boolean scheduleExpirationNotification() {
        LocalDate notificationDate = expirationDate.minusDays(3);
        long daysUntilNotification = ChronoUnit.DAYS.between(LocalDate.now(), notificationDate);

        if (daysUntilNotification <= 3 && !expirationNotificationSent) {
            sendExpirationNotification();
            expirationNotificationSent = true;
            return true; // Returnera true om notifiering skickas
        }
        return false; // Inget behov av notifiering
    }

    private void sendExpirationNotification() {
        System.out.println(name + " will expire in 3 days. Consider using it soon!");
    }
}
