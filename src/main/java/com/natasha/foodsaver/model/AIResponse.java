package com.natasha.foodsaver.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

// Klass som representerar svaret från Cohere AI API
public class AIResponse {

    // Listan av "generations" som returneras från Cohere API
    @JsonProperty("generations")  // Jackson annotation för att mappa JSON-egenskapen "generations" till fältet
    private List<Generation> generations;

    // Getter för generations (recept genererade av API:t)
    public List<Generation> getGenerations() {
        return generations;
    }

    // Setter för generations
    public void setGenerations(List<Generation> generations) {
        this.generations = generations;
    }

    // Metod som extraherar recept från de generationer som returneras från API:t
    public List<Recipe> getRecipes() {
        // Kontrollera om det finns generationer från Cohere och hämta recepttext från den första generationen
        if (generations != null && !generations.isEmpty()) {
            String recipeText = generations.get(0).getText(); // Extrahera texten från den första generationen
            return parseRecipes(recipeText);  // Anropa metod för att parsa texten till Recipe objekt
        }
        return List.of();  // Returnera en tom lista om inga generationer finns
    }

    // Enkel metod för att parsa recepttexten och skapa en lista med Recipe objekt
    private List<Recipe> parseRecipes(String recipeText) {
        String[] lines = recipeText.split("\n");
        StringBuilder nameBuilder = new StringBuilder();
        StringBuilder ingredientsBuilder = new StringBuilder();
        StringBuilder instructionsBuilder = new StringBuilder();

        boolean isIngredients = false;
        boolean isInstructions = false;

        for (String line : lines) {
            if (line.trim().toLowerCase().startsWith("ingredients:")) {
                isIngredients = true;
                isInstructions = false;
                continue;
            } else if (line.trim().toLowerCase().startsWith("instructions:")) {
                isInstructions = true;
                isIngredients = false;
                continue;
            }

            if (isIngredients) {
                ingredientsBuilder.append(line.trim()).append(", ");
            } else if (isInstructions) {
                instructionsBuilder.append(line.trim()).append("\n");
            } else {
                nameBuilder.append(line.trim()).append(" ");
            }
        }

        // Skapa receptet
        String name = nameBuilder.toString().trim();
        List<String> ingredients = List.of(ingredientsBuilder.toString().split(",\\s*"));
        String instructions = instructionsBuilder.toString().trim();

        Recipe recipe = new Recipe(name, instructions, ingredients);
        return List.of(recipe);
    }


    // Inre klass som representerar en generation (recept) från Cohere:s API-svar
    public static class Generation {
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
