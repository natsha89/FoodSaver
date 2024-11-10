package com.natasha.foodsaver; // Ensure this is at the root package

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FoodSaverApplication {
    public static void main(String[] args) {
        SpringApplication.run(FoodSaverApplication.class, args);
    }
}
