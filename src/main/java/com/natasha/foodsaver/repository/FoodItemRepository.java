package com.natasha.foodsaver.repository;

import com.natasha.foodsaver.model.FoodItem;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

// Denna interface används för att definiera databasoperationer för "FoodItem"-objekt i MongoDB
// MongoRepository tillhandahåller alla grundläggande CRUD-operationer (skapa, läsa, uppdatera, ta bort) för "FoodItem"-objekt

public interface FoodItemRepository extends MongoRepository<FoodItem, String> {
    List<FoodItem> findByUserId(String userId); // Spring Data JPA hanterar denna query automatiskt

}
