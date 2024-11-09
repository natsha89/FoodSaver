package com.natasha.foodsaver.service;

import com.natasha.foodsaver.model.FoodItem;
import com.natasha.foodsaver.repository.FoodItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodItemService {

    @Autowired
    private FoodItemRepository foodItemRepository;

    // Hämtar alla matvaror och kollar deras utgångsdatum
    public List<FoodItem> getAllFoodItems() {
        List<FoodItem> foodItems = foodItemRepository.findAll();

        // Kolla utgångsdatum och generera notifieringar
        for (FoodItem foodItem : foodItems) {
            // Kontrollera och skicka notifiering om förfallodatumet
            foodItem.scheduleExpirationNotification();
            // Kontrollera om allergener matchar användarens allergier
            foodItem.checkAllergies(getUserAllergies(foodItem.getUserId()));
        }

        return foodItems;
    }

    // Skapa en ny FoodItem
    public FoodItem createFoodItem(FoodItem foodItem) {
        // Kontrollera om allergener eller utgångsdatum finns
        boolean hasAllergens = foodItem.checkAllergies(getUserAllergies(foodItem.getUserId()));
        boolean isExpirationNear = foodItem.scheduleExpirationNotification();

        // Om ingen allergi eller utgångsdatum varning finns, spara matvaran
        if (!hasAllergens && !isExpirationNear) {
            return foodItemRepository.save(foodItem);
        } else {
            // Om det finns allergener eller utgångsdatum varning, returnera null eller hantera fel
            return null;
        }
    }

    // Hämta användarens allergier
    public List<String> getUserAllergies(String userId) {
        // Detta kan ersättas med riktig användardata från din databas eller användargränssnitt
        return List.of("Peanuts", "Dairy"); // Detta är bara ett exempel
    }

    public void deleteFoodItem(String id) {
        foodItemRepository.deleteById(id);
    }

    // Uppdatera en matvara
    public FoodItem updateFoodItem(String id, FoodItem foodItem) {
        if (foodItemRepository.existsById(id)) {
            foodItem.setId(id);  // Se till att vi inte överskriver ID:t
            return foodItemRepository.save(foodItem);
        }
        return null;
    }
}
