package com.Harum.Harum.Models;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "views")
public class Views {

    @Id
    private String id;
    private String userId;
    private String postId;
    private String createdAt;

    public Views() {
        this.createdAt = Instant.now().toString(); // Tự động gán thời gian tạo
    }

    public Views(String userId, String postId) {
        this.userId = userId;
        this.postId = postId;
        this.createdAt = Instant.now().toString(); // Tự động gán thời gian tạo
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
