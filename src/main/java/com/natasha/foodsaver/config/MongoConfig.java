package com.natasha.foodsaver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

@Configuration  // Markerar denna klass som en Spring-konfigurationsklass, så att Spring kan upptäcka och använda den vid uppstart
public class MongoConfig extends AbstractMongoClientConfiguration {

    // Metod för att definiera namn på databasen som används av MongoDB
    @Override
    protected String getDatabaseName() {
        return "foodsaver";  // Här returneras namnet på den MongoDB-databas som ska användas, "foodsaver"
    }
}
