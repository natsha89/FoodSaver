package com.natasha.foodsaver;

import com.natasha.foodsaver.model.AIResponse;
import com.natasha.foodsaver.model.Recipe;
import com.natasha.foodsaver.service.AIService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AIServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AIService aiService;

    @Value("${openai.api.url}")
    private String openAiUrl;

    @Value("${openai.api.key}")
    private String openAiApiKey;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGenerateRecipes_EmptyResponse() {
        // Arrange
        String ingredients = "tomato, cheese";

        when(restTemplate.postForObject(
                eq(openAiUrl),         // Use eq() matcher for openAiUrl
                any(HttpEntity.class),  // Use any() matcher for HttpEntity
                eq(AIResponse.class)    // Use eq() matcher for AIResponse.class
        )).thenReturn(null);

        // Act
        List<Recipe> recipes = aiService.generateRecipes(ingredients);

        // Assert
        assertEquals(Collections.emptyList(), recipes);
        verify(restTemplate, times(1)).postForObject(eq(openAiUrl), any(HttpEntity.class), eq(AIResponse.class));
    }


    @Test
    public void testGenerateRecipes_ExceptionHandling() {
        // Arrange
        String ingredients = "tomato, cheese";

        when(restTemplate.postForObject(
                eq(openAiUrl),       // Use eq() matcher for openAiUrl
                any(HttpEntity.class),
                eq(AIResponse.class)  // Use eq() matcher for AIResponse.class
        )).thenThrow(new RuntimeException("API error"));

        // Act
        List<Recipe> recipes = aiService.generateRecipes(ingredients);

        // Assert
        assertEquals(Collections.emptyList(), recipes);
        verify(restTemplate, times(1)).postForObject(eq(openAiUrl), any(HttpEntity.class), eq(AIResponse.class));
    }

}
