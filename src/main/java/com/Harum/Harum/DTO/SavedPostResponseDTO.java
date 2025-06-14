package com.Harum.Harum.DTO;

public class SavedPostResponseDTO {
    private String id;
    private String userId;
    private String postId;
    private String createdAt;

    public SavedPostResponseDTO(String id, String userId, String postId, String createdAt) {
        this.id = id;
        this.userId = userId;
        this.postId = postId;
        this.createdAt = createdAt;
    }

    // Getters
    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getPostId() { return postId; }
    public String getCreatedAt() { return createdAt; }
}
