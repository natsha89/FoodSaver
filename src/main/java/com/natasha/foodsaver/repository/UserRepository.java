package com.natasha.foodsaver.repository;

import com.natasha.foodsaver.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    // Den här interfacet definierar databasoperationer för User-objekt i MongoDB

    // findByEmail(String email)
    // Den här metoden hämtar en användare baserat på deras emailadress.
    // Vi använder namngivna metoder från Spring Data som automatiskt genererar den nödvändiga frågan.
    // Exempel: findByEmail("user@example.com") kommer att returnera en användare med det angivna emailet.
    User findByEmail(String email);

    // existsById(String id)
    // Den här metoden returnerar ett boolean-värde för att kontrollera om en användare med ett specifikt id finns i databasen.
    // Det är användbart när man vill verifiera om en användare med ett visst id redan finns innan man skapar eller uppdaterar data.
    boolean existsById(String id); // För att kontrollera om en användare finns baserat på deras id.

    // findByVerificationToken(String token)
    // Den här metoden hämtar en användare baserat på deras verifieringstoken.
    // Vi använder den här metoden för att hantera e-postverifiering, när vi behöver hitta användaren som har ett specifikt verifieringstoken.
    User findByVerificationToken(String token);

}
