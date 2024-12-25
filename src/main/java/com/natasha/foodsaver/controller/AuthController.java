package com.natasha.foodsaver.controller;

import com.natasha.foodsaver.exception.GlobalExceptionHandler;
import com.natasha.foodsaver.exception.UserAlreadyExistsException;
import com.natasha.foodsaver.model.User;
import com.natasha.foodsaver.repository.UserRepository;
import com.natasha.foodsaver.service.AuthService;
import com.natasha.foodsaver.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/*
 * MIT License
 * Copyright (c) [2024] [Natasha Shahran]
 *
 * Permission is granted under the MIT License to use, modify, and distribute
 * this software, provided credit is given to the original creator ([Natasha Shahran]).
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
 */


@RestController  // Markerar denna klass som en REST-controller som hanterar HTTP-förfrågningar
@RequestMapping("/api/auth")  // Anger grundvägen för alla API-anrop (t.ex. /api/auth/register, /api/auth/login)
public class AuthController {

    @Autowired
    private AuthService authService;  // Injekterar AuthService för autentisering och användarhantering

    @Autowired
    private UserRepository userRepository;  // Injekterar UserRepository för att interagera med databasen och användardata

    @Autowired
    private JwtService jwtService;  // Injekterar JwtService för JWT-tokenhantering

    // Registrering av ny användare
    @PostMapping("/register")
    public ResponseEntity<GlobalExceptionHandler.ResponseMessage> register(@Valid @RequestBody User user) {
        try {
            // Försöker registrera användaren via AuthService
            User savedUser = authService.register(user);
            // Returnera en framgångsrespons med status 201 (Created) och ett meddelande om att användaren är registrerad
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new GlobalExceptionHandler.ResponseMessage("User registered successfully!", savedUser));
        }
        catch (UserAlreadyExistsException e) {
            // Hantera fall där användaren redan finns
            return buildErrorResponse(HttpStatus.CONFLICT, e.getMessage());
        }
        catch (Exception e) {
            // Hantera generella fel vid registrering
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "User registration failed.");
        }
    }

    // Logga in en användare
    @PostMapping("/login")
    public ResponseEntity<GlobalExceptionHandler.ResponseMessage> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        try {
            // Försöker logga in och generera en JWT-token för användaren
            String token = authService.loginAndGenerateToken(email, password);
            // Hämta användaren från databasen
            User user = userRepository.findByEmail(email);
            if (user == null) {
                // Om användaren inte finns i databasen, returnera felmeddelande
                return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Invalid email or password.");
            }
            // Returnera en framgångsrespons med användarinformation och token
            return ResponseEntity.ok(new GlobalExceptionHandler.ResponseMessage("Welcome, " + user.getFullName() + "!",
                    Map.of("user", user, "token", token)));
        }
        catch (Exception e) {
            // Hantera fel vid inloggning
            return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Invalid email or password.");
        }
    }

    // Logga ut användaren
    @PostMapping("/logout")
    public ResponseEntity<GlobalExceptionHandler.ResponseMessage> logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            // Logga ut användaren genom att rensa autentisering från SecurityContext
            new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
            // Returnera en framgångsrespons efter utloggning
            return ResponseEntity.ok(new GlobalExceptionHandler.ResponseMessage("Successfully logged out.", null));
        }
        catch (Exception e) {
            // Hantera fel vid utloggning
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error logging out: " + e.getMessage());
        }
    }

    // Hjälpmetod för att bygga felrespons med status och meddelande
    private ResponseEntity<GlobalExceptionHandler.ResponseMessage> buildErrorResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status)
                .body(new GlobalExceptionHandler.ResponseMessage(message, null));
    }
}
