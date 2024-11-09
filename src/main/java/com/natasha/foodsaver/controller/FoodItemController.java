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
    public ResponseEntity<?> createFoodItem(@RequestBody FoodItem foodItem) {
        // Kontrollera om allergener eller utgångsdatum finns
        boolean hasAllergens = foodItem.checkAllergies(foodItemService.getUserAllergies(foodItem.getUserId()));
        boolean isExpirationNear = foodItem.scheduleExpirationNotification();

        // Om allergener eller utgångsdatum varningar finns, returnera en 400 Bad Request
        if (hasAllergens || isExpirationNear) {
            String errorMessage = "Matvaran kan inte skapas. ";
            if (hasAllergens) {
                errorMessage += "Den innehåller allergener som du är allergisk mot. ";
            }
            if (isExpirationNear) {
                errorMessage += "Utgångsdatumet är nära. ";
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(errorMessage);
        }

        // Annars skapa matvaran
        FoodItem createdFoodItem = foodItemService.createFoodItem(foodItem);
        if (createdFoodItem != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdFoodItem);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Matvaran kunde inte skapas på grund av allergener eller förfallodatum.");
        }
    }

    // Endpoint för att hämta alla matvaror
    @GetMapping
    public ResponseEntity<List<FoodItem>> getAllFoodItems() {
        List<FoodItem> foodItems = foodItemService.getAllFoodItems();
        return ResponseEntity.ok(foodItems);
    }

    // Endpoint för att uppdatera en matvara
    @PutMapping("/{id}")
    public ResponseEntity<?> updateFoodItem(@PathVariable String id, @RequestBody FoodItem foodItem) {
        // Kontrollera om allergener eller utgångsdatum finns
        boolean hasAllergens = foodItem.checkAllergies(foodItemService.getUserAllergies(foodItem.getUserId()));
        boolean isExpirationNear = foodItem.scheduleExpirationNotification();

        // Om allergener eller utgångsdatum varningar finns, returnera en 400 Bad Request
        if (hasAllergens || isExpirationNear) {
            String errorMessage = "Matvaran kan inte uppdateras. ";
            if (hasAllergens) {
                errorMessage += "Den innehåller allergener som du är allergisk mot. ";
            }
            if (isExpirationNear) {
                errorMessage += "Utgångsdatumet är nära. ";
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(errorMessage); // Returnera felmeddelande som en string
        }

        // Annars uppdatera matvaran
        FoodItem updatedFoodItem = foodItemService.updateFoodItem(id, foodItem);
        if (updatedFoodItem != null) {
            return ResponseEntity.ok(updatedFoodItem);  // Returnera den uppdaterade matvaran
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)  // Returnera "Not Found" om ingen matvara hittades
                    .body("Matvaran med ID: " + id + " hittades inte.");  // Felmeddelande i body
        }
    }


    // Endpoint för att ta bort en matvara
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFoodItem(@PathVariable String id) {
        foodItemService.deleteFoodItem(id);
        return ResponseEntity.noContent().build();
    }
}
