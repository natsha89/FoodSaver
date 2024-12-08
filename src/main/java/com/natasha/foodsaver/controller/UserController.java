package com.natasha.foodsaver.controller;

import com.natasha.foodsaver.exception.GlobalExceptionHandler;
import com.natasha.foodsaver.model.User;
import com.natasha.foodsaver.service.UserService;
import com.natasha.foodsaver.service.JwtService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;  // Injektar UserService för att hantera användarlogik
    @Autowired
    private JwtService jwtService;  // Injektar JwtService för att hantera JWT-token

    // Endpoint för att hämta alla användare
    @GetMapping
    public ResponseEntity<GlobalExceptionHandler.ResponseMessage> getAllUsers() {
        try {
            // Anropa UserService för att hämta alla användare
            List<User> users = userService.getAllUsers();
            // Returnera en framgångsrik svar med alla användare
            return ResponseEntity.ok(new GlobalExceptionHandler.ResponseMessage("Alla användare hämtades framgångsrikt.", users));
        } catch (Exception e) {
            // Vid fel, returnera ett internt serverfel med ett felmeddelande
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Fel vid hämtning av användare: " + e.getMessage());
        }
    }

    // Endpoint för att hämta den aktuella användaren baserat på token
    @GetMapping("/me")
    public ResponseEntity<GlobalExceptionHandler.ResponseMessage> getCurrentUser(@RequestHeader("Authorization") String token) {
        try {
            // Extrahera användar-ID från token
            String userId = jwtService.extractUserIdFromToken(token);
            // Anropa UserService för att hämta användaren baserat på ID
            User user = userService.getUserById(userId);
            // Returnera den aktuella användaren med ett framgångsrikt svar
            return ResponseEntity.ok(new GlobalExceptionHandler.ResponseMessage("Användaren hittades framgångsrikt.", user));
        } catch (Exception e) {
            // Vid fel, returnera ett 404 Not Found med ett felmeddelande
            return buildErrorResponse(HttpStatus.NOT_FOUND, "Fel vid hämtning av användare: " + e.getMessage());
        }
    }

    // Endpoint för att radera en användare baserat på ID
    @DeleteMapping("/{id}")
    public ResponseEntity<GlobalExceptionHandler.ResponseMessage> deleteUserById(@PathVariable String id) {
        try {
            // Anropa UserService för att radera användaren
            userService.deleteUserById(id);
            // Returnera ett framgångsrikt svar när användaren är raderad
            return ResponseEntity.ok(new GlobalExceptionHandler.ResponseMessage("Användaren med ID " + id + " har raderats framgångsrikt.", null));
        } catch (Exception e) {
            // Vid fel, returnera ett internt serverfel med ett felmeddelande
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Fel vid radering av användare: " + e.getMessage());
        }
    }

    // Hjälpmetod för att bygga ett fel-svar med ett specifikt statuskod och felmeddelande
    private ResponseEntity<GlobalExceptionHandler.ResponseMessage> buildErrorResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status)
                .body(new GlobalExceptionHandler.ResponseMessage(message, null));
    }

    // Endpoint för att uppdatera den aktuella användaren
    @PutMapping("/me")
    public ResponseEntity<GlobalExceptionHandler.ResponseMessage> updateCurrentUser(
            @RequestHeader("Authorization") String token,
            @RequestBody User updatedUser
    ) {
        try {
            // Extrahera användar-ID från token
            String userId = jwtService.extractUserIdFromToken(token);
            // Anropa UserService för att uppdatera användarens information
            User savedUser = userService.updateUser(userId, updatedUser);
            // Returnera det uppdaterade användarobjektet med ett framgångsrikt svar
            return ResponseEntity.ok(new GlobalExceptionHandler.ResponseMessage("Användaren har uppdaterats framgångsrikt.", savedUser));
        } catch (Exception e) {
            // Vid fel, returnera ett bad request (HTTP 400) med ett felmeddelande
            return buildErrorResponse(HttpStatus.BAD_REQUEST, "Fel vid uppdatering av användare: " + e.getMessage());
        }
    }
}
