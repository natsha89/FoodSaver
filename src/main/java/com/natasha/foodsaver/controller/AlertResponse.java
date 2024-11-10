package com.natasha.foodsaver.controller;

import com.natasha.foodsaver.model.FoodItem;

// En hjälparklass som används för att slå in matvaror (FoodItem) och varningsmeddelanden (alert messages) i ett objekt
public class AlertResponse {
    private FoodItem foodItem;  // Matvaran som är associerad med varningen
    private String alertMessage;  // Varningsmeddelandet (t.ex. om förfallodatum eller allergen)

    // Konstruktor som initialiserar AlertResponse med en FoodItem och ett alertMessage
    public AlertResponse(FoodItem foodItem, String alertMessage) {
        this.foodItem = foodItem;  // Sätt matvaran
        this.alertMessage = alertMessage;  // Sätt varningsmeddelandet
    }

    // Getter för att hämta matvaran (FoodItem)
    public FoodItem getFoodItem() {
        return foodItem;  // Returnera FoodItem objektet
    }

    // Getter för att hämta varningsmeddelandet (alert message)
    public String getAlertMessage() {
        return alertMessage;  // Returnera varningsmeddelandet som en sträng
    }
}
