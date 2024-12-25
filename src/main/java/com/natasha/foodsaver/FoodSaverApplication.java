package com.natasha.foodsaver; // Ensure this is at the root package

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/*
 * MIT License
 * Copyright (c) [2024] [Natasha Shahran]
 *
 * Permission is granted under the MIT License to use, modify, and distribute
 * this software, provided credit is given to the original creator ([Natasha Shahran]).
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
 */

@SpringBootApplication
@EnableScheduling
public class FoodSaverApplication {
    public static void main(String[] args) {
        SpringApplication.run(FoodSaverApplication.class, args);
    }
}
