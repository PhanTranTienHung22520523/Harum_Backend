package com.Harum.Harum.Repository;

import com.Harum.Harum.Models.Notifications;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NotificationRepo extends MongoRepository<Notifications, String> {
    List<Notifications> findByUserId(String userId);
}
