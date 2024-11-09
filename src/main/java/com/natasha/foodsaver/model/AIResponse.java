package com.natasha.foodsaver.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

// Klass som representerar svaret från Cohere AI API
public class AIResponse {

    // Listan av "choices" som returneras från Cohere API
    @JsonProperty("choices")
    private List<Choice> choices;

    // Getter för choices (val som returneras från API:t)
    public List<Choice> getChoices() {
        return choices;
    }

    // Setter för choices
    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    // Metod som extraherar recept från de val (choices) som returneras från API:t
    public List<Recipe> getRecipes() {
        // Kontrollera om det finns val från Cohere och hämta recepttext från det första valet
        if (choices != null && !choices.isEmpty()) {
            String recipeText = choices.get(0).getText(); // Extrahera texten från det första valet
            return parseRecipes(recipeText);  // Anropa metod för att parsa texten till Recipe objekt
        }
        return List.of();  // Returnera en tom lista om inga val finns
    }

    // Enkel metod för att parsa recepttexten och skapa en lista med Recipe objekt
    private List<Recipe> parseRecipes(String recipeText) {
        // Exempel: Dela upp recipeText i delar och skapa Recipe objekt (förväntar formatet: namn, ingredienser, instruktioner)
        String[] parts = recipeText.split("\n", 3); // Dela upp texten vid radbrytningar i tre delar
        if (parts.length == 3) {
            String name = parts[0].trim();  // Receptets namn
            String ingredients = parts[1].trim();  // Ingredienserna
            String instructions = parts[2].trim();  // Instruktionerna

            // Omvandla ingredienser till en lista och skapa ett Recipe objekt
            List<String> ingredientList = List.of(ingredients.split(",\\s*"));  // Dela ingredienserna vid kommatecken
            Recipe recipe = new Recipe(name, instructions, ingredientList);  // Skapa Recipe objektet
            return List.of(recipe);  // Returnera en lista med ett enda recept
        }
        return List.of();  // Om formatet inte är som förväntat, returnera en tom lista
    }

    // Inre klass som representerar ett val (choice) från Cohere:s API-svar
    public static class Choice {
        private String text;

        // Getter för text
        public String getText() {
            return text;
        }

        // Setter för text
        public void setText(String text) {
            this.text = text;
        }
    }
}
