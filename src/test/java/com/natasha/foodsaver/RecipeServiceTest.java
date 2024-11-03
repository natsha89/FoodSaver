package com.natasha.foodsaver;

import com.natasha.foodsaver.model.Recipe;
import com.natasha.foodsaver.repository.RecipeRepository;
import com.natasha.foodsaver.service.AIService;
import com.natasha.foodsaver.service.RecipeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class RecipeServiceTest {

    @MockBean
    private AIService aiService;

    @MockBean
    private RecipeRepository recipeRepository;

    @Autowired
    private RecipeService recipeService;

    @Test
    public void testGenerateAIRecipes() {
        // Setup mock behavior
        when(aiService.generateRecipes(anyString())).thenReturn(mockRecipes());

        // Call the service method
        List<Recipe> recipes = recipeService.generateAIRecipes("tomato, cheese");

        // Assert results
        assertNotNull(recipes);
        assertFalse(recipes.isEmpty());
    }

    private List<Recipe> mockRecipes() {
        // Create some mock Recipe objects
        Recipe recipe1 = new Recipe();
        recipe1.setId("1");
        recipe1.setName("Tomato and Cheese Salad");
        recipe1.setFoodItem(List.of("Tomato", "Cheese", "Olive Oil", "Basil"));

        Recipe recipe2 = new Recipe();
        recipe2.setId("2");
        recipe2.setName("Cheesy Tomato Pasta");
        recipe2.setFoodItem(List.of("Tomato", "Cheese", "Pasta", "Garlic"));

        return List.of(recipe1, recipe2); // Return a list of mock recipes
    }

    @Test
    public void testGenerateAIRecipesNoResults() {
        when(aiService.generateRecipes(anyString())).thenReturn(List.of()); // No recipes returned

        List<Recipe> recipes = recipeService.generateAIRecipes("unknown ingredient");

        assertNotNull(recipes);
        assertTrue(recipes.isEmpty()); // Should be empty
    }
    @Test
    public void testGenerateAIRecipesThrowsException() {
        when(aiService.generateRecipes(anyString())).thenThrow(new RuntimeException("Service unavailable"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            recipeService.generateAIRecipes("tomato, cheese");
        });

        assertEquals("Service unavailable", exception.getMessage());
    }
}
