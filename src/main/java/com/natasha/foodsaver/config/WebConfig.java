package com.natasha.foodsaver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration  // Markerar denna klass som en Spring-konfigurationsklass
public class WebConfig implements WebMvcConfigurer {

    // Överskrider metoden för att konfigurera CORS-inställningar
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // Tillåter CORS för alla rutter ("/**" innebär alla vägar).
                .allowedOrigins("http://localhost:3000")  // Tillåt endast anrop från denna ursprung (localhost:3000)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // Tillåt specifika HTTP-metoder
                .allowedHeaders("*")  // Tillåt alla typer av headers
                .allowCredentials(true);  // Tillåt att skicka cookies och autentiseringsuppgifter med förfrågningar
    }
}
