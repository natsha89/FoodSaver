package com.natasha.foodsaver.controller;

import com.natasha.foodsaver.model.FoodItem;
import com.natasha.foodsaver.model.User;
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

    @Autowired
    private FoodItemAlertService foodItemAlertService; // Inject the alert service


    // Metod för att skapa en ny matvara
    @PostMapping
    public ResponseEntity<?> createFoodItem(@RequestHeader("Authorization") String token, @RequestBody FoodItem foodItem) {
        // 1. Extrahera userId från token via JwtService
        String userId = jwtService.extractUserIdFromToken(token);  // Anropa metoden här

        // 2. Sätt userId i foodItem innan det sparas
        foodItem.setUserId(userId);  // Sätt userId till matvaran från token

        // 3. Spara matvaran via service
        FoodItem savedFoodItem = foodItemService.createFoodItem(foodItem);

        List<String> alerts = List.of(
                foodItemAlertService.getAllergyAlert(foodItem, foodItemService.getUserAllergies(userId)),
                foodItemAlertService.getExpirationAlert(foodItem)
        ).stream().filter(alert -> !alert.isEmpty()).toList();

        if (!alerts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(new AlertResponse(savedFoodItem, alerts));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(savedFoodItem);
    }


    @GetMapping("/user")
    public ResponseEntity<?> getFoodItemsByUserId(@RequestHeader("Authorization") String token) {
        // 1. Extrahera användar-ID från token
        String userId = jwtService.extractUserIdFromToken(token);

        // 2. Hämta alla matvaror för användaren
        List<FoodItem> foodItems = foodItemService.getFoodItemsByUserId(userId);

        if (foodItems.isEmpty()) {
            logger.info("No food items found for user: {}", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No food items found for this user.");
        }

        // 3. Generera notifikationer för varje matvara
        List<AlertResponse> alertResponses = foodItems.stream().map(foodItem -> {
            String allergyAlert = foodItemAlertService.getAllergyAlert(foodItem, foodItemService.getUserAllergies(userId));
            String expirationAlert = foodItemAlertService.getExpirationAlert(foodItem);

            // Skapa en AlertResponse för varje matvara
            return new AlertResponse(foodItem, allergyAlert, expirationAlert);
        }).toList();

        // 4. Returnera listan av AlertResponse-objekt
        logger.info("Found food items with alerts for user: {}", userId);
        return ResponseEntity.ok(alertResponses);
    }


    // Metod för att uppdatera en matvara baserat på matvarans ID
    @PutMapping("/{id}")
    public ResponseEntity<?> updateFoodItem(@PathVariable String id, @RequestHeader("Authorization") String token, @RequestBody FoodItem foodItem) {
        String userId = jwtService.extractUserIdFromToken(token);

        FoodItem updatedFoodItem = foodItemService.updateFoodItem(id, foodItem);
        if (updatedFoodItem == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Food item with ID: " + id + " was not found.");
        }

        // Generate alerts using FoodItemAlertService
        String allergyAlert = foodItemAlertService.getAllergyAlert(updatedFoodItem, foodItemService.getUserAllergies(userId));
        String expirationAlert = foodItemAlertService.getExpirationAlert(updatedFoodItem);
        String fullAlert = allergyAlert + expirationAlert;

        return ResponseEntity.ok(new AlertResponse(updatedFoodItem, fullAlert));
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
}