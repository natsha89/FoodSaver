package com.natasha.foodsaver.controller;

import com.natasha.foodsaver.exception.GlobalExceptionHandler;
import com.natasha.foodsaver.exception.ResourceNotFoundException;
import com.natasha.foodsaver.model.FoodItem;
import com.natasha.foodsaver.model.Notification;
import com.natasha.foodsaver.service.JwtService;
import com.natasha.foodsaver.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
public class NotificationController {
    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    @Autowired
    private JwtService jwtService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/api/notifications")
    public ResponseEntity<?> getNotifications(@RequestHeader("Authorization") String token) {
        String userId = jwtService.extractUserIdFromToken(token);
        if (userId == null) {
            logger.error("Token extraction failed");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new GlobalExceptionHandler.ResponseMessage("Invalid token", null));
        }

        List<Notification> notifications = notificationService.getAllNotifications(userId);

        if (notifications.isEmpty()) {
            logger.info("No notifications found for user: {}", userId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }

        logger.info("Found {} notifications for user: {}", notifications.size(), userId);
        return ResponseEntity.ok(notifications);
    }

    @PutMapping("/api/notifications/{notificationId}/read")
    public ResponseEntity<?> markNotificationAsRead(@PathVariable String notificationId, @RequestHeader("Authorization") String token
    ) {
        String userId = jwtService.extractUserIdFromToken(token);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new GlobalExceptionHandler.ResponseMessage("Invalid token", null));

        }

        try {
            Notification updatedNotification = notificationService.markAsRead(notificationId, userId);
            return ResponseEntity.ok(updatedNotification);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new GlobalExceptionHandler.ResponseMessage("Notification not found", null));
        }
    }
}