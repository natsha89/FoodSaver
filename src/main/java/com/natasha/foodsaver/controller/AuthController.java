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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController  // Denna klass hanterar REST API-anrop och är en controller för autentisering
@RequestMapping("/api/auth")  // Definierar basvägen för alla endpoints i denna controller
public class AuthController {

    @Autowired
    private AuthService authService;  // Injektionspunkt för autentiseringstjänsten

    @Autowired
    private UserRepository userRepository;

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

    @PostMapping("/login")
    public ResponseEntity<GlobalExceptionHandler.ResponseMessage> login(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String password = payload.get("password");

        try {
            // Försök att logga in användaren och få tillbaka JWT-token
            String token = authService.loginAndGenerateToken(email, password);

            // Hämta användaren baserat på email för att kunna returnera användarinformation
            User user = userRepository.findByEmail(email); // Assuming this is available via a repository
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new GlobalExceptionHandler.ResponseMessage("Invalid email or password.", null));
            }

            // Returnera JWT-token tillsammans med användarinformation
            return ResponseEntity.ok(new GlobalExceptionHandler.ResponseMessage("Welcome, " + user.getFullName() + "!", Map.of("user", user, "token", token)));
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

    @PostMapping("/logout")
    public ResponseEntity<GlobalExceptionHandler.ResponseMessage> logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            // Invalidate the user's session and clear the security context
            new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
            return ResponseEntity.ok(new GlobalExceptionHandler.ResponseMessage("Successfully logged out.", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GlobalExceptionHandler.ResponseMessage("Error logging out: " + e.getMessage(), null));
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