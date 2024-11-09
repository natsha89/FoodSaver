package com.natasha.foodsaver.service;

import com.natasha.foodsaver.model.Recipe;
import com.natasha.foodsaver.model.AIResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AIService {

    @Value("${openai.api.key}")
    private String openAiApiKey;

    @Value("${openai.api.url}")
    private String openAiApiUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public AIService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    // Ny metod för att generera recept från OpenAI
    public List<Recipe> generateAIRecipes(String ingredients, List<String> allergens, String dietaryPreferences, int servings) {
        String dietPrompt = dietaryPreferences != null && !dietaryPreferences.isEmpty() ? dietaryPreferences : "no specific dietary preferences";

        // Skapa AI-prompt med alla parametrar
        String prompt = String.format(
                "Create a recipe using the following ingredients: %s. Exclude any ingredients that contain: %s. Make sure the recipe is %s, and prepare it in less than 30 minutes. The recipe should be suitable for %d servings.",
                ingredients,
                String.join(", ", allergens),
                dietPrompt, // Hanterar fallet där kostpreferenser saknas
                servings
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + openAiApiKey);
        headers.set("Content-Type", "application/json");

        String jsonBody = String.format("{\"model\":\"text-davinci-003\",\"prompt\":\"%s\",\"max_tokens\":300,\"temperature\":0.7}", prompt);
        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(openAiApiUrl, HttpMethod.POST, entity, String.class);

        AIResponse aiResponse = parseResponse(response.getBody());

        // Omvandla AIResponse till en lista av Recipe
        return mapAIResponseToRecipes(aiResponse);
    }

    private AIResponse parseResponse(String responseBody) {
        try {
            return objectMapper.readValue(responseBody, AIResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing OpenAI response", e);
        }
    }

    // Omvandla AIResponse till en lista av Recipe
    private List<Recipe> mapAIResponseToRecipes(AIResponse aiResponse) {
        List<Recipe> recipes = new ArrayList<>();
        for (AIResponse.Choice choice : aiResponse.getChoices()) {
            String aiRecipeText = choice.getText();

            // Anta att receptet är strukturerat som: "Namn\nIngredienser\nInstruktioner"
            String[] parts = aiRecipeText.split("\n", 3); // Dela upp texten i delar: namn, ingredienser, instruktioner

            if (parts.length == 3) {
                String name = parts[0].trim();
                String ingredients = parts[1].trim();
                String instructions = parts[2].trim();

                // Konvertera ingredienser till en lista
                List<String> ingredientList = List.of(ingredients.split(",\\s*"));

                // Skapa och lägg till receptet i listan
                Recipe recipe = new Recipe(name, instructions, ingredientList);
                recipes.add(recipe);
            }
        }
        return recipes;
    }

}
