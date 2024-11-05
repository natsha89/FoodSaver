package com.natasha.foodsaver.controller;

import com.natasha.foodsaver.model.FoodItem;
import com.natasha.foodsaver.model.ApiResponse;
import com.natasha.foodsaver.service.FoodItemService;
import com.natasha.foodsaver.service.EdamamService;
import com.natasha.foodsaver.service.NotificationService;
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

    @Autowired
    private EdamamService edamamService;

    @Autowired
    private NotificationService notificationService;

    // Endpoint för att hämta alla food items
    @GetMapping
    public ResponseEntity<ApiResponse<List<FoodItem>>> getFoodItems() {
        List<FoodItem> foodItems = foodItemService.getAllFoodItems();
        System.out.println("Food Items: " + foodItems);
        return ResponseEntity.ok(new ApiResponse<List<FoodItem>>(true, "Food items retrieved successfully", foodItems));
    }

    // Endpoint för att hämta alla allergier som stöds
    @GetMapping("/allergies")
    public ResponseEntity<ApiResponse<List<String>>> getSupportedAllergies() {
        List<String> allergies = edamamService.getSupportedAllergies();
        System.out.println("Supported Allergies: " + allergies);
        return ResponseEntity.ok(new ApiResponse<List<String>>(true, "Supported allergies retrieved successfully", allergies));
    }

    // Endpoint för att hämta utgångna matvaror
    @GetMapping("/notifications/expiring")
    public ResponseEntity<ApiResponse<List<FoodItem>>> getExpiringFoodItems() {
        List<FoodItem> expiringItems = notificationService.getExpiringFoodItems();
        return ResponseEntity.ok(new ApiResponse<List<FoodItem>>(true, "Expiring food items retrieved successfully", expiringItems));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<FoodItem>> saveFoodItem(@RequestBody FoodItem foodItem) {
        FoodItem savedFoodItem = foodItemService.saveFoodItem(foodItem);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<FoodItem>(true, "Food item saved successfully", savedFoodItem));
    }
}
