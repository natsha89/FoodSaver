package com.natasha.foodsaver.controller;

import com.natasha.foodsaver.model.Notification;
import com.natasha.foodsaver.repository.NotificationRepository;
import com.natasha.foodsaver.service.JwtService;
import com.natasha.foodsaver.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private static final Logger logger = LoggerFactory.getLogger(FoodItemController.class);

    @Autowired
    private JwtService jwtService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationService notificationService;

    // Fetch notifications for the authenticated user
    @GetMapping("/user")
    public ResponseEntity<String> getNotificationsForUser(@RequestHeader("Authorization") String token) {
        // Log the incoming token to ensure it's being received
        logger.info("Received token: {}", token);

        // Extract userId from token
        String userId = jwtService.extractUserIdFromToken(token);
        if (userId == null) {
            logger.error("Token extraction failed for token: {}", token);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        // Fetch notifications for the user
        List<Notification> notifications = notificationService.getNotificationByUserId(userId);

        if (notifications.isEmpty()) {
            logger.info("No notifications found for user: {}", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There are no new notifications for you.");
        }

        logger.info("Found notifications for user: {}", userId);
        return ResponseEntity.ok(notifications.toString()); // Send the list of notifications
    }

    // Mark a notification as read
    @PostMapping("/markAsRead/{id}")
    public ResponseEntity<Void> markNotificationAsRead(@RequestHeader("Authorization") String token,
                                                       @PathVariable("id") Long id) {
        // Log the incoming token to ensure it's being received
        logger.info("Received token: {}", token);

        // Extract userId from token
        String userId = jwtService.extractUserIdFromToken(token);
        if (userId == null) {
            logger.error("Token extraction failed for token: {}", token);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Find the notification by ID
        Notification notification = notificationRepository.findById(String.valueOf(id)).orElse(null);
        if (notification == null) {
            logger.error("Notification not found for ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Mark the notification as read
        notification.setRead(true);
        notificationRepository.save(notification);

        logger.info("Notification with ID: {} marked as read for user: {}", id, userId);
        return ResponseEntity.ok().build(); // Return 200 OK with no content
    }
}
