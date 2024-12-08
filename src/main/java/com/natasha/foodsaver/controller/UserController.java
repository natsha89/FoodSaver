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
    private UserService userService;  // Inject UserService
    @Autowired
    private JwtService jwtService;

    @GetMapping
    public ResponseEntity<GlobalExceptionHandler.ResponseMessage> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();  // Use UserService to get users
            return ResponseEntity.ok(new GlobalExceptionHandler.ResponseMessage("All users fetched successfully.", users));
        } catch (Exception e) {
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching users: " + e.getMessage());
        }
    }

    @GetMapping("/me")
    public ResponseEntity<GlobalExceptionHandler.ResponseMessage> getCurrentUser(@RequestHeader("Authorization") String token) {
        try {
            String userId = jwtService.extractUserIdFromToken(token);
            User user = userService.getUserById(userId);  // Use UserService to get the current user
            return ResponseEntity.ok(new GlobalExceptionHandler.ResponseMessage("User found successfully.", user));
        } catch (Exception e) {
            return buildErrorResponse(HttpStatus.NOT_FOUND, "Error fetching user: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GlobalExceptionHandler.ResponseMessage> deleteUserById(@PathVariable String id) {
        try {
            userService.deleteUserById(id);  // Delegate to UserService for deleting user
            return ResponseEntity.ok(new GlobalExceptionHandler.ResponseMessage("User with ID " + id + " has been deleted successfully.", null));
        } catch (Exception e) {
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting user: " + e.getMessage());
        }
    }

    private ResponseEntity<GlobalExceptionHandler.ResponseMessage> buildErrorResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status)
                .body(new GlobalExceptionHandler.ResponseMessage(message, null));
    }
    @PutMapping("/me")
    public ResponseEntity<GlobalExceptionHandler.ResponseMessage> updateCurrentUser(
            @RequestHeader("Authorization") String token,
            @RequestBody User updatedUser
    ) {
        try {
            String userId = jwtService.extractUserIdFromToken(token);
            User savedUser = userService.updateUser(userId, updatedUser);
            return ResponseEntity.ok(new GlobalExceptionHandler.ResponseMessage("User updated successfully.", savedUser));
        } catch (Exception e) {
            return buildErrorResponse(HttpStatus.BAD_REQUEST, "Error updating user: " + e.getMessage());
        }
    }
}
