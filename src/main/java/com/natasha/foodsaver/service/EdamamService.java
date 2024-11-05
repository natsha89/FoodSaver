package com.natasha.foodsaver.service;

import com.natasha.foodsaver.model.EdamamResponse;
import com.natasha.foodsaver.model.Recipe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class EdamamService {

    @Value("${edamam.app_id}")
    private String appId;

    @Value("${edamam.app_key}")
    private String appKey;

    @Value("${edamam.recipe_url}")
    private String recipeUrl;

    @Value("${edamam.allergy_url}")
    private String allergies;

    private final RestTemplate restTemplate;

    public EdamamService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Recipe> searchRecipes(String query, List<String> allergies) {
        StringBuilder url = new StringBuilder(recipeUrl)
                .append("?type=public&q=").append(query)
                .append("&app_id=").append(appId)
                .append("&app_key=").append(appKey);

        if (allergies != null && !allergies.isEmpty()) {
            for (String allergy : allergies) {
                url.append("&health=").append(allergy);
            }
        }

        try {
            EdamamResponse response = restTemplate.getForObject(url.toString(), EdamamResponse.class);
            return response != null && response.getHits() != null ? response.toRecipes() : Collections.emptyList();
        } catch (Exception e) {
            e.printStackTrace(); // Replace with proper logging
            return Collections.emptyList();
        }
    }

    public List<String> getSupportedAllergies() {
        String url = "https://api.edamam.com/api/health-labels?app_id=" + appId + "&app_key=" + appKey;

        List<String> allergies = new ArrayList<>();
        try {
            String[] supportedAllergies = restTemplate.getForObject(url, String[].class);
            if (supportedAllergies != null) {
                Collections.addAll(allergies, supportedAllergies);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Replace with proper logging
        }
        return allergies;
    }
}