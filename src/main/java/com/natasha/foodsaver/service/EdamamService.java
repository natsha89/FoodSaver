package com.natasha.foodsaver.service;

import com.natasha.foodsaver.model.EdamamResponse;
import com.natasha.foodsaver.model.Recipe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class EdamamService {

    @Value("${edamam.app_id}")
    private String appId;

    @Value("${edamam.app_key}")
    private String appKey;

    @Value("${edamam.recipe_url}")
    private String recipeUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<Recipe> searchRecipes(String query, List<String> allergies) {
        StringBuilder url = new StringBuilder(recipeUrl + "?type=public&q=" + query +
                "&app_id=" + appId + "&app_key=" + appKey);

        if (allergies != null && !allergies.isEmpty()) {
            for (String allergy : allergies) {
                url.append("&health=").append(allergy);
            }
        }

        EdamamResponse response = restTemplate.getForObject(url.toString(), EdamamResponse.class);
        if (response != null && response.getHits() != null) {
            return response.toRecipes();
        }
        return new ArrayList<>();
    }
}
