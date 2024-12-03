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
import java.util.Collections;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AIService {

    // Hämtar API-nyckeln och URL för Cohere API från application.properties
    @Value("${cohere.api.key}")
    private String cohereApiKey;

    @Value("${cohere.api.url}")
    private String cohereApiUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    // Konstruktor för att injicera RestTemplate och ObjectMapper
    public AIService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    // Metod för att generera recept med hjälp av AI baserat på ingredienser, allergener, kostpreferenser och antal portioner
    public List<Recipe> generateAIRecipes(String ingredients, List<String> allergens, String dietaryPreferences, int servings) {
        // Om kostpreferenser inte är angivna, sätt det till "inga specifika kostpreferenser"
        String dietPrompt = dietaryPreferences != null && !dietaryPreferences.isEmpty() ? dietaryPreferences : "no specific dietary preferences";

        // Skapa AI-prompt med alla parametrar
        String prompt = String.format(
                "Create a recipe using the following ingredients: %s. Exclude any ingredients that contain: %s. Make sure the recipe is %s, and suitable for %d servings.",
                ingredients,
                String.join(", ", allergens),
                dietPrompt, // Hanterar fallet där kostpreferenser saknas
                servings
        );

        // Skapa HTTP-headers med API-nyckel och content-type
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + cohereApiKey);
        headers.set("Content-Type", "application/json");

        // Skapa JSON-body enligt Coheres Generate API dokumentation
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

        // Skapa en HTTP-begäran med den skapade JSON-body och headers
        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        // Skicka begäran till Cohere API och få svaret
        ResponseEntity<String> response = restTemplate.exchange(cohereApiUrl, HttpMethod.POST, entity, String.class);

        // Bearbeta svaret från Cohere API
        AIResponse aiResponse = parseResponse(response.getBody());

        // Omvandla AI:s svar till en lista av recept
        return mapAIResponseToRecipes(aiResponse);
    }

    // Metod för att bearbeta svaret från Cohere API
    private AIResponse parseResponse(String responseBody) {
        System.out.println("test response" + responseBody);
        try {
            // Parsar svaret med hjälp av AIResponse, som ska spegla den faktiska API-strukturen
            AIResponse aiResponse = objectMapper.readValue(responseBody, AIResponse.class);

            // Kontrollera att vi har giltiga generationer i svaret
            if (aiResponse == null || aiResponse.getGenerations() == null || aiResponse.getGenerations().isEmpty()) {
                throw new RuntimeException("Cohere returned an invalid response.");
            }

            return aiResponse;
        } catch (Exception e) {
            // Logga eventuella fel och kasta ett undantag
            System.err.println("Error parsing response: " + e.getMessage());
            throw new RuntimeException("Error parsing Cohere response", e);
        }
    }

    // Metod för att omvandla AI:s svar till en lista av recept
    private List<Recipe> mapAIResponseToRecipes(AIResponse aiResponse) {
        List<Recipe> recipes = new ArrayList<>();

        // Loop över varje generation i AI:s svar och skapa ett recept
        for (AIResponse.Generation generation : aiResponse.getGenerations()) {
            String aiRecipeText = generation.getText();

            // Dela upp AI-svaret i tre delar: namn, ingredienser, instruktioner
            String[] parts = aiRecipeText.split("\n", 3);

            // Om vi har fått alla delar (namn, ingredienser och instruktioner), skapa receptet
            if (parts.length == 3) {
                String name = parts[0].trim(); // Receptets namn
                String ingredients = parts[1].trim(); // Ingredienserna
                String instructions = parts[2].trim(); // Instruktionerna

                // Skapa och lägg till receptet i listan, använd foodItem istället för foodItems
                Recipe recipe = new Recipe(name, instructions, Collections.singletonList(ingredients));
                recipes.add(recipe);
            }
        }
        return recipes;
    }
}
