package com.natasha.foodsaver.repository;

import com.natasha.foodsaver.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/*
 * MIT License
 * Copyright (c) [2024] [Natasha Shahran]
 *
 * Permission is granted under the MIT License to use, modify, and distribute
 * this software, provided credit is given to the original creator ([Natasha Shahran]).
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
 */


// Interface som utökar MongoRepository för att kunna utföra operationer på User-dokument i MongoDB
public interface UserRepository extends MongoRepository<User, String> {

    // Metod för att hitta en användare baserat på deras e-postadress
    User findByEmail(String email);

    // Metod för att kontrollera om en användare existerar baserat på deras id
    boolean existsById(String id); // För att kontrollera om en användare finns baserat på deras id.

    // Metod för att hitta en användare baserat på en verifieringstoken
    User findByVerificationToken(String token);

    Optional<User> findById(String id);       // Ensure this also returns Optional<User>
}
