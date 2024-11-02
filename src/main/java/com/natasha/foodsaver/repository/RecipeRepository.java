package com.natasha.foodsaver.repository;

import com.natasha.foodsaver.model.Recipe;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface RecipeRepository extends MongoRepository<Recipe, String> {
    List<Recipe> findByUserId(String userId);
}
