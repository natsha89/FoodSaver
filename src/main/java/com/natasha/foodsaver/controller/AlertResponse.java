package com.natasha.foodsaver.controller;

import com.natasha.foodsaver.model.FoodItem;

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


public class AlertResponse {

    private FoodItem foodItem;  // Ett objekt som representerar en matvara
    private List<String> alerts; // En lista som innehåller varningar relaterade till matvaran

    // Konstruktor som tar ett FoodItem och ett eller flera alert-meddelanden
    public AlertResponse(FoodItem foodItem, String... alerts) {
        this.foodItem = foodItem;  // Sätter matvaran
        this.alerts = List.of(alerts);  // Sätter listan med varningar
    }

    // Konstruktor som tar ett FoodItem och en redan existerande lista med alerts
    public AlertResponse(FoodItem foodItem, List<String> alerts) {
        this.foodItem = foodItem;  // Sätter matvaran
        this.alerts = alerts;  // Sätter listan med varningar
    }

    // Getters och Setters för FoodItem och Alerts

    // Hämtar matvaran
    public FoodItem getFoodItem() {
        return foodItem;
    }

    // Sätter matvaran
    public void setFoodItem(FoodItem foodItem) {
        this.foodItem = foodItem;
    }

    // Hämtar listan med varningar
    public List<String> getAlerts() {
        return alerts;
    }

    // Sätter listan med varningar
    public void setAlerts(List<String> alerts) {
        this.alerts = alerts;
    }
}
