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
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AIService {

    @Value("${openai.api.key}")
    private String openAiApiKey;

    @Value("${openai.api.url}")
    private String openAiApiUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper; // För att konvertera JSON till objekt

    public AIService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public List<Recipe> generateRecipes(String ingredients, List<String> allergens) {
        String prompt = "Create a recipe using the following ingredients: " + ingredients;

        // Skapa begäran till OpenAI API
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + openAiApiKey);
        headers.set("Content-Type", "application/json");

        String jsonBody = String.format("{\"model\":\"text-davinci-003\",\"prompt\":\"%s\",\"max_tokens\":300,\"temperature\":0.7}", prompt);
        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        // Skicka begäran till OpenAI API
        ResponseEntity<String> response = restTemplate.exchange(openAiApiUrl, HttpMethod.POST, entity, String.class);

        // Hantera responsen från OpenAI och konvertera den till en lista med recept
        AIResponse aiResponse = parseResponse(response.getBody());
        return aiResponse.getRecipes();  // Returnera recepten
    }

    private AIResponse parseResponse(String responseBody) {
        try {
            // Parse JSON-svaret till en AIResponse-objekt
            return objectMapper.readValue(responseBody, AIResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing OpenAI response", e);
        }
    }
}
