package com.natasha.foodsaver.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class AIResponse {
    @JsonProperty("choices")
    private List<Choice> choices;

    public List<Recipe> getRecipes() {
        // Här ska du bearbeta `choices` för att skapa en lista av Recipe-objekt
        // För enkelhetens skull antas det här att du bara hämtar den första promptens text
        // och delar upp den i individuella recept.
        // Du kan justera detta beroende på OpenAI:s faktiska svar.
        if (choices != null && !choices.isEmpty()) {
            String recipeText = choices.get(0).getText(); // Anta att första valet innehåller recepttext
            return parseRecipes(recipeText);
        }
        return List.of();
    }

    private List<Recipe> parseRecipes(String recipeText) {
        // Här kan du implementera en metod som delar upp texten till faktiska Recipe-objekt
        // För enkelhetens skull returnerar vi en lista med ett enkelt exempel
        return List.of(new Recipe(recipeText));
    }

    public static class Choice {
        private String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
