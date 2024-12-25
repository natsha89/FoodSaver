package com.natasha.foodsaver.config;

import com.natasha.foodsaver.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


/*
 * MIT License
 * Copyright (c) [2024] [Natasha Shahran]
 *
 * Permission is granted under the MIT License to use, modify, and distribute
 * this software, provided credit is given to the original creator ([Natasha Shahran]).
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
 */


@Configuration // Markerar denna klass som en konfiguration för Spring
public class ApplicationConfiguration {

    private final UserRepository userRepository;

    // Konstruktor för att injicera userRepository (för att hämta användardata från databasen)
    public ApplicationConfiguration(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Bean för att skapa en UserDetailsService, som används för att hämta användardata vid autentisering
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findById(username) // Försöker hitta användaren baserat på användarnamnet (e-post)
                .orElseThrow(() -> new UsernameNotFoundException("User with email [" + username + "] not found")); // Om användaren inte finns, kasta ett undantag
    }

    // Bean för att skapa en PasswordEncoder med BCrypt-algoritm för att kryptera lösenord
    @Bean(name = "applicationPasswordEncoder")
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // BCryptPasswordEncoder är en säker metod för att kryptera lösenord
    }

    // Bean för att skapa en AuthenticationManager, som används för att hantera autentisering
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager(); // Hämtar AuthenticationManager från den automatiskt konfigurerade AuthenticationConfiguration
    }

    // Bean för att skapa en AuthenticationProvider, som används för att autentisera användare via DaoAuthenticationProvider
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(); // Skapar en DaoAuthenticationProvider
        authProvider.setUserDetailsService(userDetailsService()); // Sätter UserDetailsService som ska användas för att hämta användardata
        authProvider.setPasswordEncoder(passwordEncoder()); // Sätter PasswordEncoder som ska användas för att verifiera lösenord
        return authProvider; // Returnerar den konfigurerade AuthenticationProvider
    }
}
