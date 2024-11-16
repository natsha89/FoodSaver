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

    @PostMapping
    public ResponseEntity<?> createFoodItem(@RequestBody FoodItem foodItem) {
        StringBuilder alertMessage = new StringBuilder();

        // Kontrollera om allergener finns och om utgångsdatumet är nära
        boolean hasAllergens = foodItem.checkAllergies(foodItemService.getUserAllergies(foodItem.getUserId()));
        boolean isExpirationNear = foodItem.scheduleExpirationNotification();

        if (hasAllergens) {
            alertMessage.append("Varning: Matvaran innehåller allergener som du är allergisk mot. ");
        }

        if (isExpirationNear) {
            alertMessage.append("Notis: Matvarans utgångsdatum är nära.");
        }

        // Spara matvaran och skapa AlertResponse med varningsmeddelande
        FoodItem createdFoodItem = foodItemService.createFoodItem(foodItem);
        if (createdFoodItem != null) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new AlertResponse(createdFoodItem, alertMessage.toString()));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Matvaran kunde inte sparas.");
        }
    }

    @GetMapping
    public ResponseEntity<List<FoodItem>> getAllFoodItems() {
        List<FoodItem> foodItems = foodItemService.getAllFoodItems();
        return ResponseEntity.ok(foodItems);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FoodItem>> getFoodItemsByUserId(@PathVariable String userId) {
        List<FoodItem> foodItems = foodItemService.getFoodItemsByUserId(userId);
        if (foodItems.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null); // Returnera NOT_FOUND om inga matvaror finns för användaren
        }
        return ResponseEntity.ok(foodItems);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateFoodItem(@PathVariable String id, @RequestBody FoodItem foodItem) {
        StringBuilder alertMessage = new StringBuilder();

        // Kontrollera om allergener finns och om utgångsdatumet är nära
        boolean hasAllergens = foodItem.checkAllergies(foodItemService.getUserAllergies(foodItem.getUserId()));
        boolean isExpirationNear = foodItem.scheduleExpirationNotification();

        if (hasAllergens) {
            alertMessage.append("Varning: Matvaran innehåller allergener som du är allergisk mot. ");
        }
        if (isExpirationNear) {
            alertMessage.append("Notis: Matvarans utgångsdatum är nära.");
        }

        FoodItem updatedFoodItem = foodItemService.updateFoodItem(id, foodItem);
        if (updatedFoodItem != null) {
            return ResponseEntity.ok(new AlertResponse(updatedFoodItem, alertMessage.toString()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Matvaran med ID: " + id + " hittades inte.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFoodItem(@PathVariable String id) {
        foodItemService.deleteFoodItem(id);
        return ResponseEntity.noContent().build();
    }
}
