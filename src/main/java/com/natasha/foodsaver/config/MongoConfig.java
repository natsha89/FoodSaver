package com.natasha.foodsaver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

/*
 * MIT License
 * Copyright (c) [2024] [Natasha Shahran]
 *
 * Permission is granted under the MIT License to use, modify, and distribute
 * this software, provided credit is given to the original creator ([Natasha Shahran]).
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
 */


@Configuration  // Markerar denna klass som en Spring-konfigurationsklass, så att Spring kan upptäcka och använda den vid uppstart
public class MongoConfig extends AbstractMongoClientConfiguration {

    // Metod för att definiera namn på databasen som används av MongoDB
    @Override
    protected String getDatabaseName() {
        return "foodsaver";  // Här returneras namnet på den MongoDB-databas som ska användas, "foodsaver"
    }
}
