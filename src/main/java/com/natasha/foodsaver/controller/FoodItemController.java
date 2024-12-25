package com.natasha.foodsaver.controller;

import com.natasha.foodsaver.model.FoodItem;
import com.natasha.foodsaver.repository.UserRepository;
import com.natasha.foodsaver.service.FoodItemAlertService;
import com.natasha.foodsaver.service.FoodItemService;
import com.natasha.foodsaver.service.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


@RestController  // Markerar denna klass som en REST-controller
@RequestMapping("/api/foodItems")  // Grundvägen för alla matvaru-API-anrop
public class FoodItemController {
    private static final Logger logger = LoggerFactory.getLogger(FoodItemController.class);  // Logger för att logga viktig information

    @Autowired
    private FoodItemService foodItemService;  // Tjänst för hantering av matvaror

    @Autowired
    private JwtService jwtService;  // Tjänst för att hantera JWT-token

    @Autowired
    private UserRepository userRepository;  // Repository för användardata

    @Autowired
    private FoodItemAlertService foodItemAlertService;  // Tjänst för att generera varningar för matvaror


    // Metod för att skapa en ny matvara
    @PostMapping
    public ResponseEntity<?> createFoodItem(@RequestHeader("Authorization") String token, @RequestBody FoodItem foodItem) {
        // 1. Extrahera användarens ID från JWT-token
        String userId = jwtService.extractUserIdFromToken(token);

        // 2. Sätt användar-ID i foodItem innan det sparas
        foodItem.setUserId(userId);

        // 3. Spara matvaran via service
        FoodItem savedFoodItem = foodItemService.createFoodItem(foodItem);

        // 4. Skapa varningar för allergier och utgångsdatum
        List<String> alerts = List.of(
                foodItemAlertService.getAllergyAlert(foodItem, foodItemService.getUserAllergies(userId)),
                foodItemAlertService.getExpirationAlert(foodItem)
        ).stream().filter(alert -> !alert.isEmpty()).toList();  // Filtrera bort tomma varningar

        // 5. Om det finns varningar, returnera matvaran med varningar
        if (!alerts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(new AlertResponse(savedFoodItem, alerts));
        }

        // Om inga varningar, returnera bara den sparade matvaran
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFoodItem);
    }


    // Metod för att hämta alla matvaror för en användare
    @GetMapping("/user")
    public ResponseEntity<String> getFoodItemsByUserId(@RequestHeader("Authorization") String token) {
        // Logga inkommande token
        logger.info("Received token: {}", token);

        // Extrahera användar-ID från token
        String userId = jwtService.extractUserIdFromToken(token);
        if (userId == null) {
            logger.error("Token extraction failed for token: {}", token);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token.");
        }

        // Hämta matvaror för användaren
        List<FoodItem> foodItems = foodItemService.getFoodItemsByUserId(userId);

        // Om inga matvaror finns, returnera 404
        if (foodItems.isEmpty()) {
            logger.info("No food items found for user: {}", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        logger.info("Found food items for user: {}", userId);
        return ResponseEntity.ok(foodItems.toString());
    }

    // Metod för att uppdatera en matvara baserat på matvarans ID
    @PutMapping("/{id}")
    public ResponseEntity<?> updateFoodItem(@PathVariable String id, @RequestHeader("Authorization") String token, @RequestBody FoodItem foodItem) {
        // Extrahera användar-ID från token
        String userId = jwtService.extractUserIdFromToken(token);

        // Uppdatera matvaran via service
        FoodItem updatedFoodItem = foodItemService.updateFoodItem(id, foodItem);
        if (updatedFoodItem == null) {
            // Om matvaran inte finns, returnera 404
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Food item with ID: " + id + " was not found.");
        }

        // Generera varningar för allergier och utgångsdatum
        String allergyAlert = foodItemAlertService.getAllergyAlert(updatedFoodItem, foodItemService.getUserAllergies(userId));
        String expirationAlert = foodItemAlertService.getExpirationAlert(updatedFoodItem);
        String fullAlert = allergyAlert + expirationAlert;

        // Returnera den uppdaterade matvaran med varningar
        return ResponseEntity.ok(new AlertResponse(updatedFoodItem, fullAlert));
    }


    // Metod för att ta bort en matvara baserat på användar-ID och matvara-ID
    @DeleteMapping("/{foodItemId}")
    public ResponseEntity<String> deleteFoodItem(@RequestHeader("Authorization") String token, @PathVariable String foodItemId) {
        try {
            // Extrahera användar-ID från token
            String userId = jwtService.extractUserIdFromToken(token);

            logger.info("Received request to delete food item with ID: {} for user with ID: {}", foodItemId, userId);

            // Försök att ta bort matvaran via service
            boolean deleted = foodItemService.deleteFoodItem(userId, foodItemId);

            if (deleted) {
                // Om borttagning lyckas, returnera framgångsmeddelande
                return ResponseEntity.ok("Food item deleted successfully.");
            } else {
                // Om användaren inte har behörighet att ta bort matvaran, returnera 403
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User is not authorized to delete this food item.");
            }
        } catch (Exception e) {
            // Logga och hantera eventuella fel vid borttagning
            logger.error("An error occurred while deleting food item {} for user {}: {}", foodItemId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An internal server error occurred while deleting the food item.");
        }
    }

    // Metod för att hämta alla matvaror för en användare
    @GetMapping
    public ResponseEntity<List<FoodItem>> getAllFoodItems(@RequestHeader("Authorization") String token) {
        // Extrahera användar-ID från token
        String userId = jwtService.extractUserIdFromToken(token);

        // Hämta matvaror för användaren
        List<FoodItem> foodItems = foodItemService.getFoodItemsByUserId(userId);

        if (foodItems.isEmpty()) {
            // Om inga matvaror hittas, returnera 404
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }

        // Returnera listan med matvaror
        return ResponseEntity.ok(foodItems);
    }
}
