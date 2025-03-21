package com.Harum.Harum.Models;


import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import java.time.Instant;
import com.Harum.Harum.Enums.NotificationTypes;

@Document (collection = "notifications")
@Data
public class Notifications {
    @Id
    private String id;
    private String userId;
    private String message;
    private boolean isRead;
    private NotificationTypes type;
    private String postId;
    private String commentId;
    private String followId;
    private String createdAt;

    public Notifications() {}

    public Notifications(String userId, String message, NotificationTypes type, String postId, String commentId, String followId) {
        this.userId = userId;
        this.message = message;
        this.type = type;
        this.postId = postId;
        this.commentId = commentId;
        this.followId = followId;
        this.isRead = false;
        this.createdAt = Instant.now().toString();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public boolean getIsRead() { return isRead; }
    public void setIsRead(boolean isRead) { this.isRead = isRead; }
    public NotificationTypes getType() { return type; }
    public void setType(NotificationTypes type) { this.type = type; }
    public String getPostId() { return postId; }
    public void setPostId(String postId) { this.postId = postId; }
    public String getCommentId() { return commentId; }
    public void setCommentId(String commentId) { this.commentId = commentId; }
    public String getFollowId() { return followId; }
    public void setFollowId(String followId) { this.followId = followId; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt() { this.createdAt = Instant.now().toString(); }
}
