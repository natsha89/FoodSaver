package com.natasha.foodsaver.controller;

import com.natasha.foodsaver.model.FoodItem;
import com.natasha.foodsaver.service.FoodItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController  // Denna klass hanterar REST API-anrop och är en controller för matvaror
@RequestMapping("/api/foodItems")  // Definierar basvägen för alla endpoints i denna controller
public class FoodItemController {

    @Autowired
    private FoodItemService foodItemService;  // Injektionspunkt för tjänsten som hanterar matvaror

    // Endpoint för att hämta alla matvaror
    @GetMapping
    public ResponseEntity<List<FoodItem>> getAllFoodItems() {
        List<FoodItem> foodItems = foodItemService.getAllFoodItems();  // Hämta alla matvaror via service
        return ResponseEntity.ok(foodItems);  // Returnera listan med matvaror med HTTP-status 200 OK
    }

    // Endpoint för att hämta en matvara baserat på dess ID
    @GetMapping("/{id}")
    public ResponseEntity<FoodItem> getFoodItemById(@PathVariable String id) {
        FoodItem foodItem = foodItemService.getFoodItemById(id);  // Hämta matvaran med det specifika ID:t
        return foodItem != null  // Om matvaran hittades
                ? ResponseEntity.ok(foodItem)  // Returnera den hittade matvaran med HTTP-status 200 OK
                : ResponseEntity.notFound().build();  // Om matvaran inte finns, returnera HTTP-status 404 Not Found
    }

    // Endpoint för att skapa en ny matvara
    @PostMapping
    public ResponseEntity<FoodItem> createFoodItem(@RequestBody FoodItem foodItem) {
        // Skapa en ny matvara via FoodItemService
        FoodItem createdFoodItem = foodItemService.createFoodItem(foodItem);
        return ResponseEntity.status(HttpStatus.CREATED)  // Returnera den skapade matvaran med HTTP-status 201 Created
                .body(createdFoodItem);
    }

    // Endpoint för att uppdatera en existerande matvara
    @PutMapping("/{id}")
    public ResponseEntity<FoodItem> updateFoodItem(@PathVariable String id, @RequestBody FoodItem foodItem) {
        // Uppdatera matvaran baserat på ID och de nya data som skickas i request body
        FoodItem updatedFoodItem = foodItemService.updateFoodItem(id, foodItem);
        return updatedFoodItem != null  // Om uppdateringen lyckades
                ? ResponseEntity.ok(updatedFoodItem)  // Returnera den uppdaterade matvaran med HTTP-status 200 OK
                : ResponseEntity.notFound().build();  // Om matvaran inte finns, returnera HTTP-status 404 Not Found
    }

    // Endpoint för att ta bort en matvara baserat på ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFoodItem(@PathVariable String id) {
        // Ta bort matvaran baserat på ID via FoodItemService
        foodItemService.deleteFoodItem(id);
        return ResponseEntity.noContent().build();  // Returnera HTTP-status 204 No Content (inga data att returnera)
    }
}
