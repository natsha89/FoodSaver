package com.natasha.foodsaver.controller;

import com.natasha.foodsaver.model.FoodItem;

public class AlertResponse {

    private FoodItem foodItem;
    private String alertMessage;

    public AlertResponse(FoodItem foodItem, String alertMessage) {
        this.foodItem = foodItem;
        this.alertMessage = alertMessage;
    }

    // Getters och Setters
    public FoodItem getFoodItem() {
        return foodItem;
    }

    public void setFoodItem(FoodItem foodItem) {
        this.foodItem = foodItem;
    }

    public String getAlertMessage() {
        return alertMessage;
    }

    public void setAlertMessage(String alertMessage) {
        this.alertMessage = alertMessage;
    }
}
