package com.natasha.foodsaver.repository;

import com.natasha.foodsaver.model.FoodItem;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

public interface FoodItemRepository extends MongoRepository<FoodItem, String> {
    List<FoodItem> findByUserId(String userId);
    List<FoodItem> findByExpirationDateBetween(Date expirationDate, Date expirationDate2);

}
