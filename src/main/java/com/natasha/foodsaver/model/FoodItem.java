package com.natasha.foodsaver.model;

import com.natasha.foodsaver.service.AIService;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "foodItems")  // Anger att denna klass ska mappar till en MongoDB-samling (collection) med namnet "foodItems"
public class FoodItem {

    @Id  // Anger att detta är primärnyckeln för objektet i MongoDB
    private String id;
    private String userId;  // Användar-ID för att koppla matvaran till en specifik användare
    private String name;  // Namnet på matvaran (t.ex. "Mjölk", "Tomat")
    private double quantity;  // Mängd av matvaran (t.ex. 2.5 för 2.5 liter mjölk)
    private String unit;  // Enheten för mängden (t.ex. "liter", "gram")
    private LocalDate expirationDate;  // Förfallodatum för matvaran
    private List<String> allergens = new ArrayList<>();  // Lista över allergener som matvaran innehåller
    private List<Recipe> recipeSuggestions = new ArrayList<>();  // Lista över receptförslag baserat på matvaran

    // Getters och Setters (metoder för att hämta och sätta värden)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public List<String> getAllergens() {
        return allergens;
    }

    public void setAllergens(List<String> allergens) {
        this.allergens = allergens;
    }

    public List<Recipe> getRecipeSuggestions() {
        return recipeSuggestions;
    }

    public void setRecipeSuggestions(List<Recipe> recipeSuggestions) {
        this.recipeSuggestions = recipeSuggestions;
    }

    // Allergikontroll
    // Denna metod kontrollerar om matvaran innehåller några allergener som användaren är känslig för
    public void checkAllergies(List<String> userAllergies) {
        for (String allergen : allergens) {
            if (userAllergies.contains(allergen)) {
                sendAllergenNotification(allergen);  // Om allergen finns, skicka varning
            }
        }
    }

    // Skickar en varning om ett allergen finns i matvaran
    private void sendAllergenNotification(String allergen) {
        System.out.println("Alert: " + name + " contains " + allergen + ", which you're allergic to.");
    }

    // Hämtar receptförslag från AI-tjänsten
    // Skickar med ingredienser och allergener för att generera recept
    public void fetchRecipeSuggestions(AIService aiService, String dietaryPreferences, int servings) {
        // Använder matvarans namn som ingrediens för förenkling
        String ingredients = name;
        this.recipeSuggestions = aiService.generateAIRecipes(ingredients, allergens, dietaryPreferences, servings);
    }

    // Planera en förfallovarsel (skickar en påminnelse om förfallodatumet)
    public void scheduleExpirationNotification() {
        LocalDate notificationDate = expirationDate.minusDays(3);  // Sätter varsel 3 dagar innan förfallodatumet
        long daysUntilNotification = ChronoUnit.DAYS.between(LocalDate.now(), notificationDate);  // Beräknar antal dagar kvar

        // Om det är dags att skicka påminnelse (0 dagar kvar)
        if (daysUntilNotification == 0) {
            System.out.println(name + " will expire in 3 days.");  // Skriver ut varning i konsolen
            // Logik för att skicka ett verkligt meddelande kan implementeras här
        }
    }

    // En extra metod för att hämta receptförslag, men den är tom i denna version
    public void fetchRecipeSuggestions(AIService aiService) {
    }
}
