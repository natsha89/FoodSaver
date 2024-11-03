package com.natasha.foodsaver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "foodItems")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodItem {
    @Id
    private String id;
    private String name;
    private double quantity;
    private String unit;
    private Date expirationDate;
    private String userId;
    private List<String> allergens; // Field for allergens
}
