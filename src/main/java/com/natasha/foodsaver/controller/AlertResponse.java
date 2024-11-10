package com.natasha.foodsaver.controller;

import com.natasha.foodsaver.model.FoodItem;

// A helper class to wrap FoodItem responses with alert messages
public class AlertResponse {
    private FoodItem foodItem;
    private String alertMessage;

    // Constructor to initialize with FoodItem and alert message
    public AlertResponse(FoodItem foodItem, String alertMessage) {
        this.foodItem = foodItem;
        this.alertMessage = alertMessage;
    }

    // Getter for foodItem
    public FoodItem getFoodItem() {
        return foodItem;
    }

    // Getter for alertMessage
    public String getAlertMessage() {
        return alertMessage;
    }
}
