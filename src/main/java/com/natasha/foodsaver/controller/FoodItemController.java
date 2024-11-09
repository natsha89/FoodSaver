package com.natasha.foodsaver.controller;

import com.natasha.foodsaver.model.FoodItem;
import com.natasha.foodsaver.service.FoodItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/foodItems")
public class FoodItemController {

    @Autowired
    private FoodItemService foodItemService;

    // Endpoint för att skapa en ny matvara
    @PostMapping
    public ResponseEntity<FoodItem> createFoodItem(@RequestBody FoodItem foodItem) {
        // Kontrollera om allergener eller utgångsdatum finns
        boolean hasAllergens = foodItem.checkAllergies(foodItemService.getUserAllergies(foodItem.getUserId()));
        boolean isExpirationNear = foodItem.scheduleExpirationNotification();

        // Om allergener eller utgångsdatum varningar finns, returnera en 400 Bad Request
        if (hasAllergens || isExpirationNear) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(foodItem); // Eller skapa ett mer detaljerat felmeddelande här
        }

        // Annars skapa matvaran
        FoodItem createdFoodItem = foodItemService.createFoodItem(foodItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFoodItem);
    }

    // Endpoint för att hämta alla matvaror
    @GetMapping
    public ResponseEntity<List<FoodItem>> getAllFoodItems() {
        List<FoodItem> foodItems = foodItemService.getAllFoodItems();
        return ResponseEntity.ok(foodItems);
    }

    // Endpoint för att uppdatera en matvara
    @PutMapping("/{id}")
    public ResponseEntity<FoodItem> updateFoodItem(@PathVariable String id, @RequestBody FoodItem foodItem) {
        // Kontrollera om allergener eller utgångsdatum finns
        boolean hasAllergens = foodItem.checkAllergies(foodItemService.getUserAllergies(foodItem.getUserId()));
        boolean isExpirationNear = foodItem.scheduleExpirationNotification();

        // Om allergener eller utgångsdatum varningar finns, returnera en 400 Bad Request
        if (hasAllergens || isExpirationNear) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(foodItem); // Eller skapa ett mer detaljerat felmeddelande här
        }

        // Annars uppdatera matvaran
        FoodItem updatedFoodItem = foodItemService.updateFoodItem(id, foodItem);
        return updatedFoodItem != null ? ResponseEntity.ok(updatedFoodItem) : ResponseEntity.notFound().build();
    }

    // Endpoint för att ta bort en matvara
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFoodItem(@PathVariable String id) {
        foodItemService.deleteFoodItem(id);
        return ResponseEntity.noContent().build();
    }
}
