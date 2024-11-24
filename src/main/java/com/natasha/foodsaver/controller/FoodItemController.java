package com.natasha.foodsaver.controller;

import com.natasha.foodsaver.model.FoodItem;
import com.natasha.foodsaver.model.User;
import com.natasha.foodsaver.repository.UserRepository;
import com.natasha.foodsaver.service.FoodItemService;
import com.natasha.foodsaver.service.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/foodItems")
public class FoodItemController {
    private static final Logger logger = LoggerFactory.getLogger(FoodItemController.class);

    @Autowired
    private FoodItemService foodItemService;  // Tjänst för att hantera matvaror

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    // Metod för att skapa en ny matvara
    @PostMapping
    public ResponseEntity<?> createFoodItem(@RequestHeader("Authorization") String token, @RequestBody FoodItem foodItem) {
        // 1. Extrahera userId från token via JwtService
        String userId = jwtService.extractUserIdFromToken(token);  // Anropa metoden här

        // 2. Sätt userId i foodItem innan det sparas
        foodItem.setUserId(userId);  // Sätt userId till matvaran från token

        // 3. Spara matvaran via service
        FoodItem savedFoodItem = foodItemService.createFoodItem(foodItem);

        // Kontrollera om matvaran innehåller allergener som användaren är allergisk mot
        StringBuilder alertMessage = new StringBuilder();
        boolean hasAllergens = foodItem.checkAllergies(foodItemService.getUserAllergies(userId));
        if (hasAllergens) {
            alertMessage.append("Warning: The food item contains allergens you are allergic to. ");
        }

        // Kontrollera om utgångsdatumet är nära
        boolean isExpirationNear = foodItem.scheduleExpirationNotification();
        if (isExpirationNear) {
            alertMessage.append("Notice: The food item is nearing its expiration date.");
        }

        // Om några varningsmeddelanden finns, inkludera dem i svaret
        if (alertMessage.length() > 0) {
            return ResponseEntity.status(HttpStatus.CREATED).body(new AlertResponse(savedFoodItem, alertMessage.toString()));
        }

        // Om inga meddelanden, returnera den sparade matvaran utan varning
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFoodItem);
    }


    // Metod för att hämta alla matvaror för en specifik användare baserat på användar-ID
    @GetMapping("/user")
    public ResponseEntity<List<FoodItem>> getFoodItemsByUserId(@RequestHeader("Authorization") String token) {
        String userId = jwtService.extractUserIdFromToken(token);

        List<FoodItem> foodItems = foodItemService.getFoodItemsByUserId(userId);

        if (foodItems.isEmpty()) {
            System.out.println("No food items found for user: " + userId);  // Logga om inga matvaror hittades
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);  // Returnera NOT_FOUND om inga matvaror finns för användaren
        }

        System.out.println("Found food items: " + foodItems);  // Logga om matvaror hittades
        return ResponseEntity.ok(foodItems);  // Returnera listan av matvaror
    }

    // Metod för att uppdatera en matvara baserat på matvarans ID
    @PutMapping("/{id}")
    public ResponseEntity<?> updateFoodItem(@PathVariable String id, @RequestHeader("Authorization") String token, @RequestBody FoodItem foodItem) {
        // Extrahera userId från token via JwtService
        String userId = jwtService.extractUserIdFromToken(token);

        // Lägg till varningsmeddelanden för allergener och utgångsdatum
        StringBuilder alertMessage = new StringBuilder();
        boolean hasAllergens = foodItem.checkAllergies(foodItemService.getUserAllergies(userId));
        boolean isExpirationNear = foodItem.scheduleExpirationNotification();

        if (hasAllergens) {
            alertMessage.append("Warning: The food item contains allergens you are allergic to. ");
        }
        if (isExpirationNear) {
            alertMessage.append("Notice: The food item is nearing its expiration date.");
        }

        // Uppdatera matvaran och returnera resultatet
        FoodItem updatedFoodItem = foodItemService.updateFoodItem(id, foodItem);
        if (updatedFoodItem != null) {
            return ResponseEntity.ok(new AlertResponse(updatedFoodItem, alertMessage.toString()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Food item with ID: " + id + " was not found.");
        }
    }

    // Metod för att ta bort en matvara baserat på användar-ID och matvara-ID
    @DeleteMapping("/{foodItemId}")
    public ResponseEntity<String> deleteFoodItem(@RequestHeader("Authorization") String token, @PathVariable String foodItemId) {
        try {
            // Extrahera userId från token via JwtService
            String userId = jwtService.extractUserIdFromToken(token);

            logger.info("Received request to delete food item with ID: {} for user with ID: {}", foodItemId, userId);

            boolean deleted = foodItemService.deleteFoodItem(userId, foodItemId);

            if (deleted) {
                return ResponseEntity.ok("Food item deleted successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User is not authorized to delete this food item.");
            }
        } catch (Exception e) {
            logger.error("An error occurred while deleting food item {} for user {}: {}", foodItemId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An internal server error occurred while deleting the food item.");
        }
    }

    // Metod för att hämta alla matvaror från databasen
    @GetMapping
    public ResponseEntity<List<FoodItem>> getAllFoodItems() {
        List<FoodItem> foodItems = foodItemService.getAllFoodItems();
        return ResponseEntity.ok(foodItems);
    }
}