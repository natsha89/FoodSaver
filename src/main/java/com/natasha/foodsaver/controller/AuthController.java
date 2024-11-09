package com.natasha.foodsaver.controller;

import com.natasha.foodsaver.exception.GlobalExceptionHandler;
import com.natasha.foodsaver.exception.UserAlreadyExistsException;
import com.natasha.foodsaver.model.User;
import com.natasha.foodsaver.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController  // Denna klass hanterar REST API-anrop och är en controller för autentisering
@RequestMapping("/api/auth")  // Definierar basvägen för alla endpoints i denna controller
public class AuthController {

    @Autowired
    private AuthService authService;  // Injektionspunkt för autentiseringstjänsten

    // Endpoint för att registrera en ny användare
    @PostMapping("/register")
    public ResponseEntity<GlobalExceptionHandler.ResponseMessage> register(@Valid @RequestBody User user) {
        try {
            // Försök att registrera användaren
            User savedUser = authService.register(user);
            return ResponseEntity.status(HttpStatus.CREATED)  // Om registreringen lyckas, returnera status 201 (skapat)
                    .body(new GlobalExceptionHandler.ResponseMessage("User registered successfully!", savedUser));
        } catch (UserAlreadyExistsException e) {
            // Om användaren redan finns, returnera status 409 (konflikt)
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new GlobalExceptionHandler.ResponseMessage(e.getMessage(), null));
        } catch (RuntimeException e) {
            // Hantera andra fel, som systemfel, och returnera status 500 (serverfel)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GlobalExceptionHandler.ResponseMessage("User registration failed.", null));
        }
    }

    // Endpoint för användarinloggning
    @PostMapping("/login")
    public ResponseEntity<GlobalExceptionHandler.ResponseMessage> login(@RequestParam String email, @RequestParam String password) {
        try {
            // Försök att logga in användaren
            User user = authService.login(email, password);
            return ResponseEntity.ok(new GlobalExceptionHandler.ResponseMessage("Welcome, " + user.getFullName() + "!", user));
        } catch (RuntimeException e) {
            // Om inloggningen misslyckas, returnera status 401 (obehörig)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new GlobalExceptionHandler.ResponseMessage("Invalid email or password.", null));
        }
    }

    // Endpoint för att ta bort användarens konto
    @DeleteMapping("/delete")
    public ResponseEntity<GlobalExceptionHandler.ResponseMessage> deleteAccount(@AuthenticationPrincipal User user) {
        try {
            // Ta bort användarens konto baserat på användarens ID (från autentisering)
            authService.deleteAccount(String.valueOf(Long.valueOf(user.getId())));
            return ResponseEntity.ok(new GlobalExceptionHandler.ResponseMessage("Your account has been deleted successfully.", null));
        } catch (RuntimeException e) {
            // Om det uppstår ett fel vid radering av kontot, returnera status 500 (serverfel)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GlobalExceptionHandler.ResponseMessage("Error deleting account: " + e.getMessage(), null));
        }
    }
    @GetMapping("/users")
    public ResponseEntity<GlobalExceptionHandler.ResponseMessage> getAllUsers() {
        try {
            // Hämta alla användare från AuthService
            List<User> users = authService.getAllUsers();
            return ResponseEntity.ok(new GlobalExceptionHandler.ResponseMessage("All users fetched successfully.", users));
        } catch (RuntimeException e) {
            // Om det uppstår ett fel vid hämtning av användare, returnera status 500 (serverfel)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GlobalExceptionHandler.ResponseMessage("Error fetching users: " + e.getMessage(), null));
        }
    }
}
