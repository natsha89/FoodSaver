package com.natasha.foodsaver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

@Configuration  // Används för att markera denna klass som en Spring Configuration-klass
public class MongoConfig extends AbstractMongoClientConfiguration {

    // Metod för att definiera namn på databasen som används av MongoDB
    @Override
    protected String getDatabaseName() {
        return "foodsaver";  // Namnet på MongoDB-databasen som ska användas
    }
}
