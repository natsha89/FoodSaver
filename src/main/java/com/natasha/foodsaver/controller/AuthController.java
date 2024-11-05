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

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<GlobalExceptionHandler.ResponseMessage> register(@Valid @RequestBody User user) {
        try {
            User savedUser = authService.register(user);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new GlobalExceptionHandler.ResponseMessage("User registered successfully!", savedUser));
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new GlobalExceptionHandler.ResponseMessage(e.getMessage(), null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GlobalExceptionHandler.ResponseMessage("User registration failed.", null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<GlobalExceptionHandler.ResponseMessage> login(@RequestParam String email, @RequestParam String password) {
        try {
            User user = authService.login(email, password);
            return ResponseEntity.ok(new GlobalExceptionHandler.ResponseMessage("Welcome, " + user.getFullName() + "!", user));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new GlobalExceptionHandler.ResponseMessage("Invalid email or password.", null));
        }
    }


    @DeleteMapping("/delete")
    public ResponseEntity<GlobalExceptionHandler.ResponseMessage> deleteAccount(@AuthenticationPrincipal User user) {
        try {
            authService.deleteAccount(String.valueOf(Long.valueOf(user.getId())));
            return ResponseEntity.ok(new GlobalExceptionHandler.ResponseMessage("Your account has been deleted successfully.", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GlobalExceptionHandler.ResponseMessage("Error deleting account: " + e.getMessage(), null));
        }
    }
}