package com.natasha.foodsaver.controller;

import com.natasha.foodsaver.exception.GlobalExceptionHandler;
import com.natasha.foodsaver.exception.UserAlreadyExistsException;
import com.natasha.foodsaver.model.User;
import com.natasha.foodsaver.repository.UserRepository;
import com.natasha.foodsaver.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;

    public AuthController(AuthService authService, UserRepository userRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<GlobalExceptionHandler.ResponseMessage> register(@Valid @RequestBody User user) {
        try {
            User savedUser = authService.register(user);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new GlobalExceptionHandler.ResponseMessage("User registered successfully!", savedUser));
        }
        catch (UserAlreadyExistsException e) {
            return buildErrorResponse(HttpStatus.CONFLICT, e.getMessage());
        }
        catch (Exception e) {
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "User registration failed.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<GlobalExceptionHandler.ResponseMessage> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        try {
            String token = authService.loginAndGenerateToken(email, password);
            User user = userRepository.findByEmail(email);
            if (user == null) {
                return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Invalid email or password.");
            }
            return ResponseEntity.ok(new GlobalExceptionHandler.ResponseMessage("Welcome, " + user.getFullName() + "!",
                    Map.of("user", user, "token", token)));
        }
        catch (Exception e) {
            return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Invalid email or password.");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<GlobalExceptionHandler.ResponseMessage> logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());

            return ResponseEntity.ok(new GlobalExceptionHandler.ResponseMessage("Successfully logged out.", null));
        }
        catch (Exception e) {
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error logging out: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GlobalExceptionHandler.ResponseMessage> deleteUserById(@PathVariable String id) {
        try {
            authService.deleteAccount(id);

            return ResponseEntity.ok(new GlobalExceptionHandler.ResponseMessage("User with ID " + id + " has been deleted successfully.", null));
        }
        catch (Exception e) {
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting user: " + e.getMessage());
        }
    }

    @GetMapping("/users")
    public ResponseEntity<GlobalExceptionHandler.ResponseMessage> getAllUsers() {
        try {
            List<User> users = authService.getAllUsers();
            return ResponseEntity.ok(new GlobalExceptionHandler.ResponseMessage("All users fetched successfully.", users));
        }
        catch (Exception e) {
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching users: " + e.getMessage());
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<GlobalExceptionHandler.ResponseMessage> getUserById(@PathVariable String id) {
        try {
            User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
            return ResponseEntity.ok(new GlobalExceptionHandler.ResponseMessage("User found successfully.", user));
        }
        catch (Exception e) {
            return buildErrorResponse(HttpStatus.NOT_FOUND, "Error fetching user: " + e.getMessage());
        }
    }

    private ResponseEntity<GlobalExceptionHandler.ResponseMessage> buildErrorResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status)
                .body(new GlobalExceptionHandler.ResponseMessage(message, null));
    }
}