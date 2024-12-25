package com.natasha.foodsaver.service;

import com.natasha.foodsaver.model.FoodItem;
import com.natasha.foodsaver.repository.FoodItemRepository;
import com.natasha.foodsaver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.natasha.foodsaver.model.User;


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


@Service
public class FoodItemService {

    @Autowired
    private FoodItemRepository foodItemRepository;  // Repository för att interagera med foodItem-databasen

    @Autowired
    private UserRepository userRepository;  // Repository för att interagera med användardatabasen

    // Metod för att skapa en ny matvara
    public FoodItem createFoodItem(FoodItem foodItem) {
        // Här kan du nu bara använda foodItem eftersom userId redan har satts
        System.out.println("Saves food items for users: " + foodItem.getUserId());

        return foodItemRepository.save(foodItem);  // Spara matvaran
    }

    // Metod för att hämta användarens allergier baserat på användar-ID (exempeldata)
    public List<String> getUserAllergies(String userId) {
        // Hämta riktiga allergier från användardatabasen istället för exempeldata
        return userRepository.findById(userId).map(User::getAllergies).orElse(List.of());
    }

    // Metod för att hämta alla matvaror för en specifik användare baserat på användar-ID
    public List<FoodItem> getFoodItemsByUserId(String userId) {
        return foodItemRepository.findByUserId(userId);  // Hämtar alla matvaror som tillhör användaren
    }

    // Metod för att radera en matvara baserat på användar-ID och matvara-ID
    public boolean deleteFoodItem(String userId, String foodItemId) {
        FoodItem foodItem = foodItemRepository.findById(foodItemId).orElse(null);

        if (foodItem == null || !foodItem.getUserId().equals(userId)) {
            return false;  // Om matvaran inte finns eller användaren inte äger den, returnera false
        }

        foodItemRepository.deleteById(foodItemId);
        return true;
    }

    // Metod för att uppdatera en matvara baserat på matvarans ID
    public FoodItem updateFoodItem(String id, FoodItem foodItem) {
        if (foodItemRepository.existsById(id)) {
            return foodItemRepository.save(foodItem);  // Uppdatera och spara matvaran om den finns
        }
        return null;  // Om matvaran inte finns, returnera null
    }

    // Metod för att hämta alla matvaror från databasen
    public List<FoodItem> getAllFoodItems() {
        return foodItemRepository.findAll();  // Returnerar alla matvaror
    }
}
