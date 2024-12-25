package com.natasha.foodsaver.repository;

import com.natasha.foodsaver.model.Recipe;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/*
 * MIT License
 * Copyright (c) [2024] [Natasha Shahran]
 *
 * Permission is granted under the MIT License to use, modify, and distribute
 * this software, provided credit is given to the original creator ([Natasha Shahran]).
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
 */


// Denna interface används för att definiera databasoperationer för "Recipe"-objekt i MongoDB
// MongoRepository tillhandahåller alla grundläggande CRUD-operationer (skapa, läsa, uppdatera, ta bort) för "Recipe"-objekt

public interface RecipeRepository extends MongoRepository<Recipe, String> {
    List<Recipe> findByUserId(String userId);
}
