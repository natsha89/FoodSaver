package com.natasha.foodsaver.controller;

import com.natasha.foodsaver.exception.GlobalExceptionHandler;
import com.natasha.foodsaver.model.User;
import com.natasha.foodsaver.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // DTO-klass för att returnera användardata
    public static class UserDTO {
        private String id;
        private String fullName;
        private String email;
        private List<String> allergies;
        private List<String> dietaryPreferences;

        public UserDTO(String id, String fullName, String email, List<String> allergies, List<String> dietaryPreferences) {
            this.id = id;
            this.fullName = fullName;
            this.email = email;
            this.allergies = allergies;
            this.dietaryPreferences = dietaryPreferences;
        }

        // Getters
        public String getId() { return id; }
        public String getFullName() { return fullName; }
        public String getEmail() { return email; }
        public List<String> getAllergies() { return allergies; }
        public List<String> getDietaryPreferences() { return dietaryPreferences; }
    }

    // Login Endpoint
    @PostMapping("/login")
    public ResponseEntity<GlobalExceptionHandler.ResponseMessage> login(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String password = payload.get("password");

        try {
            User user = authService.login(email, password);

            // Skapa en DTO för användardata
            UserDTO userDTO = new UserDTO(user.getId(), user.getFullName(), user.getEmail(), user.getAllergies(), user.getDietaryPreferences());
            return ResponseEntity.ok(new GlobalExceptionHandler.ResponseMessage("Welcome, " + user.getFullName() + "!", userDTO));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new GlobalExceptionHandler.ResponseMessage("Invalid email or password.", null));
        }
    }

    // Logout Endpoint
    @PostMapping("/logout")
    public ResponseEntity<GlobalExceptionHandler.ResponseMessage> logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
            logoutHandler.logout(request, response, SecurityContextHolder.getContext().getAuthentication());

            return ResponseEntity.ok(new GlobalExceptionHandler.ResponseMessage("Successfully logged out.", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GlobalExceptionHandler.ResponseMessage("Error logging out: " + e.getMessage(), null));
        }
    }

    // Fetch Authenticated User's Profile
    @GetMapping("/users")
    public ResponseEntity<GlobalExceptionHandler.ResponseMessage> getUserProfile(@AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new GlobalExceptionHandler.ResponseMessage("Unauthorized access.", null));
        }

        // Skapa en DTO för användardata
        UserDTO userDTO = new UserDTO(user.getId(), user.getFullName(), user.getEmail(), user.getAllergies(), user.getDietaryPreferences());
        return ResponseEntity.ok(new GlobalExceptionHandler.ResponseMessage("User profile fetched successfully.", userDTO));
    }

    // Delete Account Endpoint
    @DeleteMapping("/delete")
    public ResponseEntity<GlobalExceptionHandler.ResponseMessage> deleteAccount(@AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new GlobalExceptionHandler.ResponseMessage("Unauthorized access.", null));
        }

        try {
            authService.deleteAccount(user.getId());
            return ResponseEntity.ok(new GlobalExceptionHandler.ResponseMessage("Your account has been deleted successfully.", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GlobalExceptionHandler.ResponseMessage("Error deleting account: " + e.getMessage(), null));
        }
    }
}
