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

    @Value("${cohere.api.key}")
    private String cohereApiKey;

    @Value("${cohere.api.url}")
    private String cohereApiUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public AIService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public List<Recipe> generateAIRecipes(String ingredients, List<String> allergens, String dietaryPreferences, int servings) {
        String dietPrompt = dietaryPreferences != null && !dietaryPreferences.isEmpty() ? dietaryPreferences : "no specific dietary preferences";

        // Skapa AI-prompt med alla parametrar
        String prompt = String.format(
                "Create a recipe using the following ingredients: %s. Exclude any ingredients that contain: %s. Make sure the recipe is %s, and suitable for %d servings.",
                ingredients,
                String.join(", ", allergens),
                dietPrompt, // Hanterar fallet där kostpreferenser saknas
                servings
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + cohereApiKey);
        headers.set("Content-Type", "application/json");

        // JSON-body med parametrar enligt Cohere's Generate API dokumentation
        String jsonBody = String.format(
                "{\"model\":\"command-nightly\", " +
                        "\"prompt\":\"%s\", " +
                        "\"max_tokens\":300, " +
                        "\"temperature\":0.7, " +
                        "\"k\":0, " +
                        "\"p\":0.75, " +
                        "\"stop_sequences\":[], " +
                        "\"return_likelihoods\":\"NONE\"}",
                prompt
        );

        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        // Skicka begäran till Cohere API
        ResponseEntity<String> response = restTemplate.exchange(cohereApiUrl, HttpMethod.POST, entity, String.class);

        // Bearbeta svaret från Cohere
        AIResponse aiResponse = parseResponse(response.getBody());

        // Omvandla AIResponse till en lista av Recipe
        return mapAIResponseToRecipes(aiResponse);
    }

    private AIResponse parseResponse(String responseBody) {
        try {
            // Om Cohere:s svar innehåller en lista med val, använd den för att extrahera texten.
            AIResponse aiResponse = objectMapper.readValue(responseBody, AIResponse.class);

            if (aiResponse == null || aiResponse.getChoices() == null || aiResponse.getChoices().isEmpty()) {
                throw new RuntimeException("Cohere returned an invalid response.");
            }

            return aiResponse;
        } catch (Exception e) {
            // Logga eventuella fel och kasta ett undantag
            System.err.println("Error parsing response: " + e.getMessage());
            throw new RuntimeException("Error parsing Cohere response", e);
        }
    }

    // Omvandla AIResponse till en lista av Recipe
    private List<Recipe> mapAIResponseToRecipes(AIResponse aiResponse) {
        List<Recipe> recipes = new ArrayList<>();
        for (AIResponse.Choice choice : aiResponse.getChoices()) {
            String aiRecipeText = choice.getText();

            // Förbättra hur du delar upp texten här och extraherar ingredienser och instruktioner
            String[] parts = aiRecipeText.split("\n", 3);

            if (parts.length == 3) {
                String name = parts[0].trim();
                String ingredients = parts[1].trim();
                String instructions = parts[2].trim();

                // Skapa och lägg till receptet i listan
                Recipe recipe = new Recipe(name, instructions, List.of(ingredients.split(",\\s*")));
                recipes.add(recipe);
            }
        }
        return recipes;
    }
}
