package com.natasha.foodsaver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;


@Configuration  // Markerar denna klass som en Spring-konfigurationsklass
@EnableWebSecurity  // Aktiverar webbsäkerhet för applikationen
public class SecurityConfig {

    // Definierar en säkerhetsfilterkedja för att konfigurera HTTP-säkerhet
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // Tillåter alla anrop utan autentisering (detta kan ändras beroende på behov)
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**") // Ignorera CSRF-skydd för API:er (kan justeras efter behov)
                );
        return http.build();  // Bygg och returnera säkerhetsfilterkedjan
    }

    // Definierar en bean för BCryptPasswordEncoder för att hantera lösenordshashning
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Skapar och returnerar en instans av BCryptPasswordEncoder
    }

    // Definierar en bean för RestTemplate, som används för att göra HTTP-anrop
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();  // Skapa och returnera en ny instans av RestTemplate för att utföra REST API-anrop
    }
}
