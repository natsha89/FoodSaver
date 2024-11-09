package com.natasha.foodsaver.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class AIResponse {

    // Choices returned by OpenAI API
    @JsonProperty("choices")
    private List<Choice> choices;

    // Getter for choices
    public List<Choice> getChoices() {
        return choices;
    }

    // Setter for choices
    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    // Extracting recipes from the choices (simplified to demonstrate parsing)
    public List<Recipe> getRecipes() {
        if (choices != null && !choices.isEmpty()) {
            String recipeText = choices.get(0).getText(); // Extracting the text from the first choice
            return parseRecipes(recipeText);  // Parsing the text into Recipe objects
        }
        return List.of();  // Return empty list if no choices available
    }

    // A simple method to parse recipe text (split by lines for demonstration)
    private List<Recipe> parseRecipes(String recipeText) {
        // You can customize the logic here to handle more complex recipe structures
        // Example: splitting the recipeText into parts and creating Recipe objects

        // For demonstration, we'll split by new lines and assume a basic format
        String[] parts = recipeText.split("\n", 3); // Assuming format: name, ingredients, instructions
        if (parts.length == 3) {
            String name = parts[0].trim();
            String ingredients = parts[1].trim();
            String instructions = parts[2].trim();

            List<String> ingredientList = List.of(ingredients.split(",\\s*"));
            Recipe recipe = new Recipe(name, instructions, ingredientList);  // Create the Recipe object
            return List.of(recipe);
        }
        return List.of();  // If the format is unexpected, return empty list
    }

    // Inner class representing the choice from the OpenAI response
    public static class Choice {
        private String text;

        // Getter for text
        public String getText() {
            return text;
        }

        // Setter for text
        public void setText(String text) {
            this.text = text;
        }
    }
}
