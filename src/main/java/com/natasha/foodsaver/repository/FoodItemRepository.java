package com.natasha.foodsaver.repository;

import com.natasha.foodsaver.model.FoodItem;
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


// Denna interface används för att definiera databasoperationer för "FoodItem"-objekt i MongoDB
// MongoRepository tillhandahåller alla grundläggande CRUD-operationer (skapa, läsa, uppdatera, ta bort) för "FoodItem"-objekt

public interface FoodItemRepository extends MongoRepository<FoodItem, String> {
    List<FoodItem> findByUserId(String userId);

}
