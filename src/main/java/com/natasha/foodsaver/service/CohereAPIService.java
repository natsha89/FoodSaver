package com.natasha.foodsaver.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class CohereAPIService {

    // Uppdatera API URL:en till den rätta för generate-endpointen
    private static final String API_URL = "https://api.cohere.ai/v1/generate";

    private final String apiKey = "YOUR_API_KEY"; // Din Cohere API-nyckel
    private final RestTemplate restTemplate = new RestTemplate();

    // Metod för att generera text från Cohere API
    public String generateRecipe(String ingredients) {
        // Bygg prompten för att skapa recept
        String prompt = String.format(
                "Create a recipe using the following ingredients: %s. Exclude any ingredients that contain peanuts, dairy. Make sure the recipe is gluten-free, and prepare it in less than 30 minutes. The recipe should be suitable for 4 servings.",
                ingredients);

        // JSON-body som skickas till Cohere API
        String jsonBody = String.format("{\"model\":\"command-medium-20221108\",\"prompt\":\"%s\",\"max_tokens\":300,\"temperature\":0.7}", prompt);

        // Skapa headers med din API-nyckel
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey); // API-nyckeln sätts här
        headers.set("Content-Type", "application/json");  // Ange att begäran är JSON

        // Skapa en HTTP-entitet som inkluderar både JSON-body och headers
        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        // Gör POST-anropet till Cohere API
        ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.POST, entity, String.class);

        // Hämta och returnera svaret från API:et (dvs. genererat recept)
        return response.getBody();
    }

    public static void main(String[] args) {
        CohereAPIService cohereAPIService = new CohereAPIService();
        // Använd en lista av ingredienser för att skapa ett recept
        String recipe = cohereAPIService.generateRecipe("chicken, rice, broccoli");
        System.out.println(recipe);
    }
}

