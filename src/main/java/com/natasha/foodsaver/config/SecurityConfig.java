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

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/register", "/api/auth/login", "/api/auth/verify", "/api/auth/resend-verification").permitAll() // Tillåt registrering, inloggning och återutsändning av verifieringslänk utan autentisering
                        .anyRequest().authenticated() // Alla andra anrop kräver autentisering
                )
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/auth/**") // Försäkra att CSRF skyddet är avstängt för dessa endpoints
                ); // CSRF skydd är aktiverat men ignorerar för specifika URLer som vi tillhandahåller API-anrop för

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://192.168.0.88:3000")); // Tillåt origin från frontend på port 3000
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Tillåt dessa HTTP-metoder
        configuration.setAllowCredentials(true); // Tillåter cookies och headers
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type")); // Tillåt vissa headers

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);  // Applicera CORS-konfigurationen på alla vägar
        return source;
    }
}
