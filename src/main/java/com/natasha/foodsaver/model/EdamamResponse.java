package com.natasha.foodsaver.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class EdamamResponse {

    @JsonProperty("hits")
    private List<Hit> hits;

    // Lägg till denna metod
    public List<Hit> getHits() {
        return hits;
    }

    public List<Recipe> toRecipes() {
        return hits.stream().map(Hit::getRecipe).toList();
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Hit {
        private Recipe recipe;

        public Recipe getRecipe() {
            return recipe;
        }

        public void setRecipe(Recipe recipe) {
            this.recipe = recipe;
        }
    }
}
