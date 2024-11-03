package com.natasha.foodsaver.service;

import com.natasha.foodsaver.model.AIResponse;
import com.natasha.foodsaver.model.Recipe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
public class AIService {

    @Value("${openai.api.url}") // URL to your OpenAI API
    private String openAiUrl;

    @Value("${openai.api.key}") // Your OpenAI API key
    private String openAiApiKey;

    private final RestTemplate restTemplate;


    public AIService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Recipe> generateRecipes(String ingredients) {
        String payload = "{\"prompt\": \"Create a recipe using the following ingredients: " + ingredients + "\", \"max_tokens\": 150}";

        try {
            // Log the payload for debugging
            System.out.println("Payload: " + payload);

            AIResponse aiResponse = restTemplate.postForObject(openAiUrl, createRequestEntity(payload), AIResponse.class);

            // Debugging: Check what aiResponse contains
            System.out.println("AI Response: " + aiResponse);

            return aiResponse != null ? aiResponse.getRecipes() : Collections.emptyList();
        } catch (Exception e) {
            e.printStackTrace(); // Replace with proper logging
            return Collections.emptyList();
        }
    }

    private HttpEntity<String> createRequestEntity(String payload) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openAiApiKey);
        return new HttpEntity<>(payload, headers);
    }
}
