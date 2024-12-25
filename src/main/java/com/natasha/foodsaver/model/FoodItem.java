package com.natasha.foodsaver.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/*
 * MIT License
 * Copyright (c) [2024] [Natasha Shahran]
 *
 * Permission is granted under the MIT License to use, modify, and distribute
 * this software, provided credit is given to the original creator ([Natasha Shahran]).
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
 */


@Document(collection = "foodItems") // Dokument-annotering för att mappa till MongoDB-samlingen "foodItems"
public class FoodItem {

    @Id
    private String id; // Unikt ID för matvaran
    private String userId; // Användar-ID som matvaran tillhör
    private String name; // Namn på matvaran
    private double quantity; // Kvantitet av matvaran (t.ex. antal eller vikt)
    private String unit; // Enhet för kvantitet (t.ex. kg, liter, etc.)
    private LocalDate expirationDate; // Utgångsdatum för matvaran
    private String alertMessage; // Varningsmeddelande som kan skickas till användaren
    private List<String> allergens = new ArrayList<>(); // Lista över allergener som matvaran kan innehålla
    private boolean allergenNotificationSent = false; // Flagga för att indikera om allergivarning har skickats
    private boolean expiredNotified = false; // Nytt fält för att spåra om användaren har fått en notifiering om utgångsdatum
    private User user; // Användaren som matvaran tillhör

    // Getters och Setters för alla fält

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

    // Kontrollera om matvarans utgångsdatum är nära (3 dagar eller mindre)
    public boolean isExpirationNear() {
        long daysBetween = ChronoUnit.DAYS.between(LocalDate.now(), expirationDate);
        return daysBetween <= 3; // Returnerar true om utgångsdatumet är inom 3 dagar
    }

    // Kontrollera om matvaran innehåller några allergener som användaren är känslig för
    public boolean checkAllergies(List<String> userAllergies) {
        for (String allergen : allergens) {
            if (userAllergies.contains(allergen) && !allergenNotificationSent) {
                sendAllergenNotification(allergen); // Skicka allergivarning om inte redan skickad
                allergenNotificationSent = true; // Sätt flaggan till true för att indikera att varningen har skickats
                return true; // Returnera true om en allergen finns
            }
        }
        return false; // Returnera false om inga allergener hittades
    }

    // Skicka en notifiering för en allergen
    private void sendAllergenNotification(String allergen) {
        System.out.println("Varning: " + name + " innehåller " + allergen + ", som du är allergisk mot.");
    }

    // Schemalägg en notifiering för när matvarans utgångsdatum närmar sig
    public boolean scheduleExpirationNotification() {
        LocalDate notificationDate = expirationDate.minusDays(3); // Datum för notifiering sätts 3 dagar före utgångsdatumet
        long daysUntilNotification = ChronoUnit.DAYS.between(LocalDate.now(), notificationDate);

        // Om notifieringsdatumet är idag eller i framtiden och ingen notifiering skickats ännu, skicka notifiering
        if (daysUntilNotification <= 0 && !expiredNotified) {
            sendExpirationNotification();
            expiredNotified = true; // Sätt flaggan till true för att indikera att notifieringen har skickats
            return true; // Returnera true för att indikera att notifieringen skickades
        }
        return false; // Returnera false om notifieringen inte skickades
    }

    // Skicka en notifiering om utgångsdatumet är inom 3 dagar
    private void sendExpirationNotification() {
        System.out.println(name + " går ut om 3 dagar. Använd den snart!");
    }
}
