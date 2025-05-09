package com.Harum.Harum.Services;

import com.Harum.Harum.Models.Notifications;
import com.Harum.Harum.Repository.NotificationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepo notificationRepo;

    public List<Notifications> getAllNotifications() {
        return notificationRepo.findAll();
    }

    public Optional<Notifications> getNotificationById(String id) {
        return notificationRepo.findById(id);
    }

    public List<Notifications> getNotificationsByUserId(String userId) {
        return notificationRepo.findByUserId(userId);
    }

    public Notifications createNotification(Notifications notification) {
        return notificationRepo.save(notification);
    }

    public void deleteNotification(String id) {
        notificationRepo.deleteById(id);
    }

    public Notifications markAsRead(String id) {
        Optional<Notifications> optional = notificationRepo.findById(id);
        if (optional.isPresent()) {
            Notifications notification = optional.get();
            notification.setIsRead(true);
            return notificationRepo.save(notification);
        }
        return null;
    }
}
