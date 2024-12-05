package com.natasha.foodsaver.repository;

import com.natasha.foodsaver.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findByUserIdOrderByTimestampDesc(String userId);
    Optional<Notification> findByIdAndUserId(String id, String userId);
}
