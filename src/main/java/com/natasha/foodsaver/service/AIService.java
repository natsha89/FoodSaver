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

    public List<Recipe> generateRecipes(String ingredients, List<String> allergens, String dietaryPreferences, int servings) {
        // Välj logik baserat på ingredienser och allergener
        if (shouldUseRuleBased(ingredients)) {
            return generateRuleBasedRecipes(ingredients, allergens, dietaryPreferences, servings);
        } else {
            return generateAIRecipes(ingredients, allergens, dietaryPreferences, servings);
        }
    }

    private boolean shouldUseRuleBased(String ingredients) {
        // Regelbaserad logik om det finns få ingredienser, kan utökas med komplex logik
        return ingredients.split(",").length <= 3;
    }

    private List<Recipe> generateRuleBasedRecipes(String ingredients, List<String> allergens, String dietaryPreferences, int servings) {
        List<Recipe> ruleBasedRecipes = new ArrayList<>();

        // Exempel på enkla recept baserat på kostpreferenser och allergier
        if (ingredients.contains("tomato") && !allergens.contains("garlic") && dietaryPreferences.contains("vegetarian")) {
            ruleBasedRecipes.add(new Recipe("Tomato Basil Pasta"));
        }
        if (ingredients.contains("egg") && !allergens.contains("gluten") && dietaryPreferences.contains("vegetarian")) {
            ruleBasedRecipes.add(new Recipe("Simple Omelette"));
        }
        if (ingredients.contains("chicken") && !allergens.contains("dairy") && dietaryPreferences.contains("low-carb")) {
            ruleBasedRecipes.add(new Recipe("Grilled Chicken Salad"));
        }

        return ruleBasedRecipes;
    }

    private List<Recipe> generateAIRecipes(String ingredients, List<String> allergens, String dietaryPreferences, int servings) {
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
        return aiResponse.getRecipes();
    }


    private AIResponse parseResponse(String responseBody) {
        try {
            return objectMapper.readValue(responseBody, AIResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing OpenAI response", e);
        }
    }
}
