package com.natasha.foodsaver.service;

import com.natasha.foodsaver.exception.ResourceNotFoundException;
import com.natasha.foodsaver.model.Notification;
import com.natasha.foodsaver.repository.NotificationRepository;
import com.natasha.foodsaver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private UserRepository userRepository;

    public List<Notification> getAllNotifications(String userId) {
        return notificationRepository.findByUserIdOrderByTimestampDesc(userId);
    }

    public Notification markAsRead(String notificationId, String userId) {
        Notification notification = notificationRepository.findByIdAndUserId(notificationId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));

        notification.setRead(true);
        return notificationRepository.save(notification);
    }

    public void createNotification(String userId, String message) {
        if (userId == null || message == null) {
            throw new IllegalArgumentException("User ID and message cannot be null");
        }
        Notification notification = new Notification(message, userId);
        notificationRepository.save(notification);
    }

    public void addNotification(String expirationMessage) {
        
    }
}
