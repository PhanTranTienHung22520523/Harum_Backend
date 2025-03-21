package com.Harum.Harum.Models;


import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import java.time.Instant;
@Data
@Document(collection = "saved_posts")
public class SavedPosts {
    @Id
    private String id;
    private String userId;
    private String postId;
    private String createdAt;

    public SavedPosts() {}

    public SavedPosts(String userId, String postId) {
        this.userId = userId;
        this.postId = postId;
        this.createdAt = Instant.now().toString();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getPostId() { return postId; }
    public void setPostId(String postId) { this.postId = postId; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt() { this.createdAt = Instant.now().toString(); }
}
