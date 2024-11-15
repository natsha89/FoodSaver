
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

    // Fält för att lagra matobjektets data i databasen
    @Id
    private String userId;
    private String name;
    private double quantity;
    private String unit;
    private LocalDate expirationDate;
    private List<String> allergens = new ArrayList<>();
    private boolean expirationNotificationSent = false;
    private boolean allergenNotificationSent = false;

    // Getters och Setters

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

    // Kontrollera om matvarans utgångsdatum är nära (inom ett visst antal dagar)
    public boolean isExpirationNear(int daysBefore) {
        LocalDate notificationDate = expirationDate.minusDays(daysBefore);
        return !expirationNotificationSent && LocalDate.now().isAfter(notificationDate.minusDays(1));
    }

    // Kontrollera om matvaran innehåller allergener som användaren är allergisk mot
    public boolean checkAllergies(List<String> userAllergies) {
        for (String allergen : allergens) {
            for (String userAllergy : userAllergies) {
                // Om användarens allergi matchar en allergen i matvaran och notifikation ej skickats
                if (userAllergy.equalsIgnoreCase(allergen) && !allergenNotificationSent) {
                    sendAllergenNotification(allergen); // Skicka allergenvarning
                    allergenNotificationSent = true;
                    return true; // Returnera true om allergen hittas
                }
            }
        }
        return false; // Ingen allergen hittades
    }

    // Skicka notifiering om allergen finns
    private void sendAllergenNotification(String allergen) {
        System.out.println("Varning: " + name + " innehåller " + allergen + ", som du är allergisk mot.");
    }

    // Schemalägg utgångsdatum-notifiering
    public boolean scheduleExpirationNotification() {
        LocalDate notificationDate = expirationDate.minusDays(3);
        long daysUntilNotification = ChronoUnit.DAYS.between(LocalDate.now(), notificationDate);

        // Om det är tre dagar eller färre till utgångsdatum och notifiering ej skickats
        if (daysUntilNotification <= 3 && !expirationNotificationSent) {
            sendExpirationNotification();
            expirationNotificationSent = true;
            return true; // Returnera true om notifiering skickas
        }
        return false; // Ingen notifiering behövs
    }

    // Skicka notifiering om att matvaran snart går ut
    private void sendExpirationNotification() {
        System.out.println(name + " går ut om 3 dagar. Använd den snart!");
    }
}
