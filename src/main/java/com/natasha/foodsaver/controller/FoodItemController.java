
package com.natasha.foodsaver.controller;

import com.natasha.foodsaver.model.FoodItem;
import com.natasha.foodsaver.service.FoodItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController  // Anger att detta är en REST-controller
@RequestMapping("/api/foodItems")  // Bas-URL för alla endpoints i denna controller
public class FoodItemController {

    @Autowired
    private FoodItemService foodItemService;  // Service-klass som hanterar matvarurelaterad logik

    // Endpoint för att skapa en ny matvara
    @PostMapping  // POST-anrop för att skapa en ny matvara
    public ResponseEntity<?> createFoodItem(@RequestBody FoodItem foodItem) {
        StringBuilder alertMessage = new StringBuilder();  // Skapa en StringBuilder för att hålla varningsmeddelandet

        // Kontrollera om allergener finns och om utgångsdatumet är nära
        boolean hasAllergens = foodItem.checkAllergies(foodItemService.getUserAllergies(foodItem.getUserId()));
        boolean isExpirationNear = foodItem.scheduleExpirationNotification();

        // Om allergener finns, lägg till en varning i alertMessage
        if (hasAllergens) {
            alertMessage.append("Varning: Matvaran innehåller allergener som du är allergisk mot. ");
        }

        // Om utgångsdatumet är nära, lägg till en varning i alertMessage
        if (isExpirationNear) {
            alertMessage.append("Notis: Matvarans utgångsdatum är nära.");
        }

        // Spara matvaran och skapa AlertResponse med varningsmeddelande
        FoodItem createdFoodItem = foodItemService.createFoodItem(foodItem);
        if (createdFoodItem != null) {
            // Om matvaran sparades framgångsrikt, returnera en CREATED-status och varningsmeddelandet
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new AlertResponse(createdFoodItem, alertMessage.toString()));
        } else {
            // Om matvaran inte kunde sparas (p.g.a. allergener eller utgångsdatum), returnera BAD_REQUEST
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Matvaran kunde inte sparas.");
        }
    }

    // Endpoint för att hämta alla matvaror
    @GetMapping  // GET-anrop för att hämta alla matvaror
    public ResponseEntity<List<FoodItem>> getAllFoodItems() {
        List<FoodItem> foodItems = foodItemService.getAllFoodItems();  // Hämta alla matvaror
        return ResponseEntity.ok(foodItems);  // Returnera listan som svar
    }

    // Endpoint för att uppdatera en matvara
    @PutMapping("/{id}")  // PUT-anrop för att uppdatera en matvara
    public ResponseEntity<?> updateFoodItem(@PathVariable String id, @RequestBody FoodItem foodItem) {
        StringBuilder alertMessage = new StringBuilder();  // Skapa en StringBuilder för att hålla varningsmeddelandet

        // Kontrollera om allergener finns och om utgångsdatumet är nära
        boolean hasAllergens = foodItem.checkAllergies(foodItemService.getUserAllergies(foodItem.getUserId()));
        boolean isExpirationNear = foodItem.scheduleExpirationNotification();

        // Lägg till varningsmeddelanden för allergener och utgångsdatum om dessa finns
        if (hasAllergens) {
            alertMessage.append("Varning: Matvaran innehåller allergener som du är allergisk mot. ");
        }
        if (isExpirationNear) {
            alertMessage.append("Notis: Matvarans utgångsdatum är nära.");
        }

        // Uppdatera matvaran och skapa AlertResponse med varningsmeddelande
        FoodItem updatedFoodItem = foodItemService.updateFoodItem(id, foodItem);
        if (updatedFoodItem != null) {
            // Om uppdateringen lyckas, returnera den uppdaterade matvaran med varningsmeddelandet
            return ResponseEntity.ok(new AlertResponse(updatedFoodItem, alertMessage.toString()));
        } else {
            // Om matvaran inte hittades, returnera NOT_FOUND-status
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Matvaran med ID: " + id + " hittades inte.");
        }
    }

    // Endpoint för att ta bort en matvara
    @DeleteMapping("/{id}")  // DELETE-anrop för att ta bort en matvara
    public ResponseEntity<Void> deleteFoodItem(@PathVariable String id) {
        foodItemService.deleteFoodItem(id);  // Ta bort matvaran från databasen
        return ResponseEntity.noContent().build();  // Returstatus: NO_CONTENT, eftersom matvaran är borttagen
    }
}
