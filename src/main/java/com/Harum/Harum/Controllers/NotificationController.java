package com.Harum.Harum.Controllers;

import com.Harum.Harum.Models.Notifications;
import com.Harum.Harum.Services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public List<Notifications> getAllNotifications() {
        return notificationService.getAllNotifications();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notifications> getNotificationById(@PathVariable String id) {
        Optional<Notifications> optional = notificationService.getNotificationById(id);
        return optional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public List<Notifications> getNotificationsByUserId(@PathVariable String userId) {
        return notificationService.getNotificationsByUserId(userId);
    }

    @PostMapping
    public Notifications createNotification(@RequestBody Notifications notification) {
        return notificationService.createNotification(notification);
    }

    @DeleteMapping("/{id}")
    public void deleteNotification(@PathVariable String id) {
        notificationService.deleteNotification(id);
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Notifications> markAsRead(@PathVariable String id) {
        Notifications updated = notificationService.markAsRead(id);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }
}
