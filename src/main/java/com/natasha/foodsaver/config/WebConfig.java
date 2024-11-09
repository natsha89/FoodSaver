package com.natasha.foodsaver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration  // Markerar denna klass som en Spring-konfigurationsklass
public class WebConfig implements WebMvcConfigurer {

    // Överskriver metoden för att lägga till CORS-konfiguration
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Tillåter alla endpoints i applikationen att ta emot CORS-förfrågningar
        registry.addMapping("/**")  // Tillåt alla vägar (endpoints)
                .allowedOrigins("http://localhost:3000")  // Endast tillåt anrop från denna origin (Frontend som körs på port 3000)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // Tillåt dessa HTTP-metoder för CORS
                .allowedHeaders("*");  // Tillåt alla headers i CORS-förfrågningar
    }

    // Bean som skapar en instans av RestTemplate
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();  // Skapa och returnera en ny instans av RestTemplate
    }
}
