package com.natasha.foodsaver.repository;

import com.natasha.foodsaver.model.Recipe;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

// Denna interface används för att definiera databasoperationer för "Recipe"-objekt i MongoDB
// MongoRepository tillhandahåller alla grundläggande CRUD-operationer (skapa, läsa, uppdatera, ta bort) för "Recipe"-objekt

public interface RecipeRepository extends MongoRepository<Recipe, String> {
    List<Recipe> findByUserId(String userId);
}
