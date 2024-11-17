package com.natasha.foodsaver.service;

import com.natasha.foodsaver.model.FoodItem;
import com.natasha.foodsaver.model.User;
import com.natasha.foodsaver.repository.FoodItemRepository;
import com.natasha.foodsaver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FoodItemService {

    @Autowired
    private FoodItemRepository foodItemRepository;  // Repository för att interagera med foodItem-databasen

    @Autowired
    private UserRepository userRepository;  // Repository för att interagera med användardatabasen

    // Metod för att skapa en ny matvara
    public FoodItem createFoodItem(FoodItem foodItem) {
        // Hämtar användaren baserat på användarens ID
        Optional<User> userOptional = userRepository.findById(foodItem.getUserId());
        if (!userOptional.isPresent()) {
            throw new IllegalArgumentException("User not found with ID: " + foodItem.getUserId());  // Om användaren inte hittas, kasta ett undantag
        }

        // Kontrollera om matvaran innehåller allergener eller om utgångsdatumet är nära
        boolean hasAllergens = foodItem.checkAllergies(getUserAllergies(foodItem.getUserId()));
        boolean isExpirationNear = foodItem.scheduleExpirationNotification();

        // Om matvaran innehåller allergener eller har ett utgångsdatum nära, kasta ett undantag
        if (hasAllergens || isExpirationNear) {
            throw new IllegalArgumentException("Food item contains allergens or expiration is near.");
        }

        // Sätt användaren och spara matvaran
        User user = userOptional.get();
        foodItem.setUser(user);

        return foodItemRepository.save(foodItem);  // Spara matvaran i databasen
    }

    // Metod för att hämta användarens allergier baserat på användar-ID (exempeldata)
    public List<String> getUserAllergies(String userId) {
        // Exempel på användarallergier (kan ersättas med riktig användardata)
        return List.of("Peanuts", "Dairy");  // Exempel på allergier
    }

    // Metod för att hämta alla matvaror för en specifik användare baserat på användar-ID
    public List<FoodItem> getFoodItemsByUserId(String userId) {
        return foodItemRepository.findByUserId(userId);  // Hämtar alla matvaror som tillhör användaren
    }

    // Metod för att radera en matvara baserat på användar-ID och matvara-ID
    public boolean deleteFoodItem(String userId, String foodItemId) {
        // Hämta matvaran från databasen baserat på foodItemId
        FoodItem foodItem = foodItemRepository.findById(foodItemId).orElse(null);

        // Om matvaran inte finns, returnera false
        if (foodItem == null) {
            return false;
        }

        // Kontrollera om användaren är ägaren av matvaran
        if (!foodItem.getUserId().equals(userId)) {
            return false;  // Användaren får inte radera matvaror som inte tillhör dem
        }

        // Radera matvaran från databasen
        foodItemRepository.deleteById(foodItemId);
        return true;  // Returnera true om matvaran raderas framgångsrikt
    }

    // Metod för att uppdatera en matvara baserat på matvarans ID
    public FoodItem updateFoodItem(String id, FoodItem foodItem) {
        if (foodItemRepository.existsById(id)) {
            return foodItemRepository.save(foodItem);  // Uppdatera och spara matvaran om den finns
        }
        return null;  // Om matvaran inte finns, returnera null
    }

    // Metod för att hämta alla matvaror från databasen
    public List<FoodItem> getAllFoodItems() {
        List<FoodItem> foodItems = foodItemRepository.findAll();  // Hämtar alla matvaror från databasen

        // Gå igenom alla matvaror och utför kontroll för utgångsdatum och allergener
        for (FoodItem foodItem : foodItems) {
            foodItem.scheduleExpirationNotification();  // Kontrollera om utgångsdatumet är nära
            foodItem.checkAllergies(getUserAllergies(foodItem.getUserId()));  // Kontrollera om matvaran innehåller allergener
        }

        return foodItems;  // Returnera alla matvaror
    }
}
