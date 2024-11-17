package com.natasha.foodsaver.controller;

import com.natasha.foodsaver.model.FoodItem;
import com.natasha.foodsaver.service.FoodItemService;
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

    // Metod för att skapa en ny matvara
    @PostMapping
    public ResponseEntity<?> createFoodItem(@RequestBody FoodItem foodItem) {
        StringBuilder alertMessage = new StringBuilder();

        // Kontrollera om allergener finns och om utgångsdatumet är nära
        boolean hasAllergens = foodItem.checkAllergies(foodItemService.getUserAllergies(foodItem.getUserId()));
        boolean isExpirationNear = foodItem.scheduleExpirationNotification();

        // Lägg till varnings- och notismeddelanden
        if (hasAllergens) {
            alertMessage.append("Varning: Matvaran innehåller allergener som du är allergisk mot. ");
        }

        if (isExpirationNear) {
            alertMessage.append("Notis: Matvarans utgångsdatum är nära.");
        }

        // Spara matvaran och skapa ett AlertResponse med varningsmeddelande
        FoodItem createdFoodItem = foodItemService.createFoodItem(foodItem);
        if (createdFoodItem != null) {
            // Skicka tillbaka skapad matvara med varningsmeddelande
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new AlertResponse(createdFoodItem, alertMessage.toString()));
        } else {
            // Returnera felmeddelande om matvaran inte kunde sparas
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Matvaran kunde inte sparas.");
        }
    }

    // Metod för att hämta alla matvaror för en specifik användare baserat på användar-ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FoodItem>> getFoodItemsByUserId(@PathVariable String userId) {
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
    public ResponseEntity<?> updateFoodItem(@PathVariable String id, @RequestBody FoodItem foodItem) {
        StringBuilder alertMessage = new StringBuilder();

        // Kontrollera om allergener finns och om utgångsdatumet är nära
        boolean hasAllergens = foodItem.checkAllergies(foodItemService.getUserAllergies(foodItem.getUserId()));
        boolean isExpirationNear = foodItem.scheduleExpirationNotification();

        // Lägg till varnings- och notismeddelanden
        if (hasAllergens) {
            alertMessage.append("Varning: Matvaran innehåller allergener som du är allergisk mot. ");
        }
        if (isExpirationNear) {
            alertMessage.append("Notis: Matvarans utgångsdatum är nära.");
        }

        // Uppdatera matvaran och returnera resultatet
        FoodItem updatedFoodItem = foodItemService.updateFoodItem(id, foodItem);
        if (updatedFoodItem != null) {
            // Om uppdateringen lyckades, returnera den uppdaterade matvaran med varningsmeddelande
            return ResponseEntity.ok(new AlertResponse(updatedFoodItem, alertMessage.toString()));
        } else {
            // Returnera NOT_FOUND om matvaran med det angivna ID:t inte hittades
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Matvaran med ID: " + id + " hittades inte.");
        }
    }

    // Metod för att ta bort en matvara baserat på användar-ID och matvara-ID
    @DeleteMapping("/user/{userId}/{foodItemId}")
    public ResponseEntity<String> deleteFoodItem(@PathVariable String userId, @PathVariable String foodItemId) {
        try {
            // Logga borttagningsbegäran
            logger.info("Received request to delete food item with ID: {} for user with ID: {}", foodItemId, userId);

            // Anropa FoodItemService för att kontrollera om användaren äger matvaran
            boolean deleted = foodItemService.deleteFoodItem(userId, foodItemId);

            if (deleted) {
                // Om matvaran raderas, returnera en 200 OK-svar
                return ResponseEntity.ok("Food item deleted successfully.");
            } else {
                // Om användaren inte har rätt att ta bort matvaran, returnera 403 Forbidden
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("User is not authorized to delete this food item.");
            }

        } catch (Exception e) {
            // Logga fel och returnera ett internal server error-svar (HTTP 500)
            logger.error("An error occurred while deleting food item {} for user {}: {}", foodItemId, userId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An internal server error occurred while deleting the food item.");
        }
    }

    // Metod för att hämta alla matvaror från databasen
    @GetMapping
    public ResponseEntity<List<FoodItem>> getAllFoodItems() {
        List<FoodItem> foodItems = foodItemService.getAllFoodItems();
        return ResponseEntity.ok(foodItems);  // Returnera alla matvaror
    }
}
