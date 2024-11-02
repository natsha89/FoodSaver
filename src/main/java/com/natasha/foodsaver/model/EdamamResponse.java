package com.natasha.foodsaver.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EdamamResponse {
    private List<Hit> hits;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Hit {
        private Recipe recipe;
    }

    public List<Recipe> toRecipes() {
        return hits.stream().map(Hit::getRecipe).collect(Collectors.toList());
    }
}
