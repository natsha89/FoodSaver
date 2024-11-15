
package com.natasha.foodsaver.service;

import com.natasha.foodsaver.model.FoodItem;
import com.natasha.foodsaver.repository.FoodItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodItemService {

    // Injektera FoodItemRepository för att kunna interagera med databasen
    @Autowired
    private FoodItemRepository foodItemRepository;

    // Hämtar alla matvaror och kontrollerar deras utgångsdatum samt allergener
    public List<FoodItem> getAllFoodItems() {
        // Hämta alla matvaror från databasen
        List<FoodItem> foodItems = foodItemRepository.findAll();

        // Gå igenom alla matvaror och kolla deras utgångsdatum samt allergener
        for (FoodItem foodItem : foodItems) {
            // Kontrollera och skicka notifiering om utgångsdatumet är nära
            foodItem.scheduleExpirationNotification();
            // Kontrollera om matvaran innehåller allergener som användaren är allergisk mot
            foodItem.checkAllergies(getUserAllergies(foodItem.getUserId()));
        }

        return foodItems;
    }

    // Skapa en ny FoodItem (matvara) och kolla om allergener eller utgångsdatum varningar finns
    public FoodItem createFoodItem(FoodItem foodItem) {
        // Kontrollera om matvaran innehåller allergener som användaren är allergisk mot
        boolean hasAllergens = foodItem.checkAllergies(getUserAllergies(foodItem.getUserId()));
        // Kontrollera om utgångsdatumet är nära
        boolean isExpirationNear = foodItem.scheduleExpirationNotification();

        // Om inga allergener eller utgångsdatum-varningar finns, spara matvaran i databasen
        if (!hasAllergens && !isExpirationNear) {
            return foodItemRepository.save(foodItem);
        } else {
            // Om allergener eller utgångsdatum varning finns, returnera null eller hantera fel
            return null;
        }
    }

    // Hämta användarens allergier (denna metod kan ersättas med riktig användardata från databasen)
    public List<String> getUserAllergies(String userId) {
        // Exempel på allergier för en användare (kan hämtas från användardatabasen)
        return List.of("Peanuts", "Dairy"); // Detta är bara ett exempel
    }

    // Ta bort en matvara från databasen baserat på dess ID
    public void deleteFoodItem(String id) {
        foodItemRepository.deleteById(id);  // Radera matvaran från databasen
    }

    // Uppdatera en befintlig matvara baserat på ID
    public FoodItem updateFoodItem(String id, FoodItem foodItem) {
        // Kontrollera om matvaran finns i databasen
        if (foodItemRepository.existsById(id)) {
            return foodItemRepository.save(foodItem);  // Spara uppdateringen i databasen
        }
        return null;  // Returnera null om matvaran inte finns i databasen
    }
}
