package com.natasha.foodsaver.controller;

import com.natasha.foodsaver.model.FoodItem;
import com.natasha.foodsaver.model.Recipe;
import com.natasha.foodsaver.service.FoodItemService;
import com.natasha.foodsaver.service.EdamamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/foodItems")
public class FoodItemController {

    @Autowired
    private FoodItemService foodItemService;

    @Autowired
    private EdamamService edamamService;

    @GetMapping("/{userId}")
    public List<FoodItem> getFoodItems(@PathVariable String userId) {
        List<FoodItem> foodItems = foodItemService.getFoodItemsByUserId(userId);
        List<String> userAllergies = foodItemService.getUserAllergies(userId);
        for (FoodItem item : foodItems) {
            for (String allergen : item.getAllergens()) {
                if (userAllergies.contains(allergen)) {
                    System.out.println("Warning: " + item.getName() + " contains " + allergen + " and may cause an allergic reaction.");
                }
            }
        }
        return foodItems;
    }

    @PostMapping
    public FoodItem saveFoodItem(@RequestBody FoodItem foodItem) {
        return foodItemService.saveFoodItem(foodItem);
    }

    @GetMapping("/recipes/{userId}")
    public List<Recipe> getRecipes(@PathVariable String userId) {
        List<FoodItem> foodItems = foodItemService.getFoodItemsByUserId(userId);
        String query = foodItems.stream()
                .map(FoodItem::getName)
                .reduce((a, b) -> a + "," + b)
                .orElse("");

        List<String> allergies = foodItemService.getUserAllergies(userId);
        return edamamService.searchRecipes(query, allergies);
    }

    @GetMapping("/expiring/{userId}")
    public List<FoodItem> getExpiringFoodItems(@PathVariable String userId) {
        return foodItemService.getExpiringFoodItemsByUserId(userId);
    }
}
