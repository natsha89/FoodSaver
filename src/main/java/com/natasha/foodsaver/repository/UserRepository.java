package com.natasha.foodsaver.repository;

import com.natasha.foodsaver.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    User findByEmail(String email);
    boolean existsById(String id); // For checking if a user exists
    User findByVerificationToken(String token);

}
