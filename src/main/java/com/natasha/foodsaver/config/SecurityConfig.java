package com.natasha.foodsaver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration  // Markerad som en Spring-konfigurationsklass
@EnableWebSecurity  // Aktiverar säkerhetsinställningar för webben
public class SecurityConfig {

    // Bean som definierar säkerhetsfilterkedjan
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()  // Tillåt alla anrop utan autentisering (för enkelhet, kan justeras för mer restriktiv åtkomst)
                )
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))  // Tillåter CORS-konfigurationen för externa anrop
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**") // Ignorera CSRF-skyddet för specifika endpoints (REST API)
                );
        return http.build();  // Returnera den konstruerade säkerhetskonfigurationen
    }

    // Bean som definierar en BCryptPasswordEncoder för lösenordshantering
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Återvänder en instans av BCryptPasswordEncoder
    }

    // Bean för CORS-konfiguration
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();  // Skapa en CORS-konfiguration
        configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://192.168.0.88:3000"));  // Tillåt dessa origins (t.ex. frontendapplikationer på port 3000)
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));  // Tillåt dessa HTTP-metoder för CORS
        configuration.setAllowCredentials(true);  // Tillåt cookies och headers (t.ex. för autentisering)
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));  // Tillåt specifika headers (t.ex. Authorization för Bearer tokens)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);  // Använd CORS-konfigurationen på alla vägar i applikationen
        return source;  // Återvänder CORS-konfigurationskällan
    }
}
