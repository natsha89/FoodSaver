package com.natasha.foodsaver.controller;

import com.natasha.foodsaver.model.FoodItem;
import com.natasha.foodsaver.model.ApiResponse;
import com.natasha.foodsaver.service.FoodItemService;
import com.natasha.foodsaver.service.EdamamService;
import com.natasha.foodsaver.service.NotificationService; // Import the NotificationService
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/foodItems")
public class FoodItemController {

    @Autowired
    private FoodItemService foodItemService;

    @Autowired
    private EdamamService edamamService;

    @Autowired
    private NotificationService notificationService; // Add the NotificationService

    @GetMapping
    public ResponseEntity<ApiResponse<List<FoodItem>>> getFoodItems() {
        List<FoodItem> foodItems = foodItemService.getAllFoodItems();
        return ResponseEntity.ok(new ApiResponse<>(true, "Food items retrieved successfully", foodItems));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<FoodItem>> saveFoodItem(@RequestBody FoodItem foodItem) {
        FoodItem savedFoodItem = foodItemService.saveFoodItem(foodItem);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Food item saved successfully", savedFoodItem));
    }

    @GetMapping("/allergies")
    public ResponseEntity<ApiResponse<List<String>>> getSupportedAllergies() {
        List<String> allergies = edamamService.getSupportedAllergies();
        return ResponseEntity.ok(new ApiResponse<>(true, "Supported allergies retrieved successfully", allergies));
    }

    @GetMapping("/notifications/expiring")
    public ResponseEntity<ApiResponse<List<FoodItem>>> getExpiringFoodItems() {
        List<FoodItem> expiringItems = notificationService.getExpiringFoodItems();
        return ResponseEntity.ok(new ApiResponse<>(true, "Expiring food items retrieved successfully", expiringItems));
    }
}
