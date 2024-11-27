package com.natasha.foodsaver.controller;

import com.natasha.foodsaver.model.FoodItem;

import java.util.List;

public class AlertResponse {
    private FoodItem foodItem;
    private List<String> alerts; // List of alerts

    public AlertResponse(FoodItem foodItem, String... alerts) {
        this.foodItem = foodItem;
        this.alerts = List.of(alerts);
    }

    public AlertResponse(FoodItem foodItem, List<String> alerts) {
        this.foodItem = foodItem;
        this.alerts = alerts;
    }

    // Getters and Setters
    public FoodItem getFoodItem() {
        return foodItem;
    }

    public void setFoodItem(FoodItem foodItem) {
        this.foodItem = foodItem;
    }

    public List<String> getAlerts() {
        return alerts;
    }

    public void setAlerts(List<String> alerts) {
        this.alerts = alerts;
    }
}

