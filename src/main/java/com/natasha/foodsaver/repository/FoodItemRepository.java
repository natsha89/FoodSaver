package com.natasha.foodsaver.repository;

import com.natasha.foodsaver.model.FoodItem;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

// Denna interface används för att definiera databasoperationer för "FoodItem"-objekt i MongoDB
// MongoRepository tillhandahåller alla grundläggande CRUD-operationer (skapa, läsa, uppdatera, ta bort) för "FoodItem"-objekt

public interface FoodItemRepository extends MongoRepository<FoodItem, String> {
    List<FoodItem> findByUserId(String userId); // Spring Data JPA hanterar denna query automatiskt
    // Vi ärver alla metoder från MongoRepository för att hantera FoodItem-objekt i vår MongoDB-databas.
    // Den generiska typen <FoodItem, String> innebär att vi arbetar med FoodItem-objekt där id:t är av typen String.

    // Exempel på operationer som är tillgängliga genom MongoRepository:
    // - save(T entity): Spara ett nytt objekt eller uppdatera ett existerande objekt.
    // - findById(String id): Hämta ett objekt baserat på id.
    // - findAll(): Hämta alla objekt.
    // - deleteById(String id): Ta bort ett objekt baserat på id.
}
