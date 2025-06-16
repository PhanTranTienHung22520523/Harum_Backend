package com.Harum.Harum.DTO;


import com.Harum.Harum.Models.Users;
import com.Harum.Harum.Models.Posts;

public class SavedPostResponseDTO {
    private String savedPostId;
    private String savedAt;
    private Users user;
    private Posts post;

    public SavedPostResponseDTO() {
    }

    // Constructor đầy đủ
    public SavedPostResponseDTO(String savedPostId, String savedAt, Users user, Posts post) {
        this.savedPostId = savedPostId;
        this.savedAt = savedAt;
        this.user = user;
        this.post = post;
    }

    // Getters và Setters
    public String getSavedPostId() {
        return savedPostId;
    }

    public void setSavedPostId(String savedPostId) {
        this.savedPostId = savedPostId;
    }

    public String getSavedAt() {
        return savedAt;
    }

    public void setSavedAt(String savedAt) {
        this.savedAt = savedAt;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Posts getPost() {
        return post;
    }

    public void setPost(Posts post) {
        this.post = post;
    }
}
