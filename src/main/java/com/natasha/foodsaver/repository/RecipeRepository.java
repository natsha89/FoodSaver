package com.natasha.foodsaver.repository;

import com.natasha.foodsaver.model.Recipe;
import org.springframework.data.mongodb.repository.MongoRepository;

// Denna interface används för att definiera databasoperationer för "Recipe"-objekt i MongoDB
// MongoRepository tillhandahåller alla grundläggande CRUD-operationer (skapa, läsa, uppdatera, ta bort) för "Recipe"-objekt

public interface RecipeRepository extends MongoRepository<Recipe, String> {
    // Vi ärver alla metoder från MongoRepository för att hantera Recipe-objekt i vår MongoDB-databas.
    // Den generiska typen <Recipe, String> innebär att vi arbetar med Recipe-objekt där id:t är av typen String.

    // Exempel på operationer som är tillgängliga genom MongoRepository:
    // - save(T entity): Spara ett nytt objekt eller uppdatera ett existerande objekt.
    // - findById(String id): Hämta ett objekt baserat på id.
    // - findAll(): Hämta alla objekt.
    // - deleteById(String id): Ta bort ett objekt baserat på id.

    // Om vi vill lägga till egna sökmetoder, kan vi definiera dem här, till exempel:
    // List<Recipe> findByIngredientsContains(String ingredient);
    // Den här metoden skulle till exempel söka recept som innehåller en viss ingrediens.
}
