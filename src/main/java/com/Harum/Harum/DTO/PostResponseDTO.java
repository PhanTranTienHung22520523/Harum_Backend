package com.Harum.Harum.DTO;

import com.Harum.Harum.Models.PostBlock;

import java.util.List;

public class PostResponseDTO {
    private String id;
    private String title;
    private String content;
    private String imageUrl;
    private String status;
    private String createdAt;
    private String updatedAt;
    private String topicId;
    private String topicName;
    private String userImage;
    private String userId;
    private String username;
    private int countLike;
    private int countDislike;
    private int countView;
    private List<PostBlock> contentBlock;

    public PostResponseDTO() {
    }

    public PostResponseDTO(String id, String title, String userImage, String content, String imageUrl, String status,
                           String createdAt, String updatedAt, String topicId, String topicName,
                           String userId, String username, int countLike, int countDislike, int countView,
                           List<PostBlock> contentBlock) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.topicId = topicId;
        this.topicName = topicName;
        this.userId = userId;
        this.username = username;
        this.countLike = countLike;
        this.countDislike = countDislike;
        this.countView = countView;
        this.contentBlock = contentBlock;
        this.userImage = userImage;
    }

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getImageUrl() { return imageUrl; }
    public String getStatus() { return status; }
    public String getUserImage(){ return userImage;}
    public String getCreatedAt() { return createdAt; }
    public String getUpdatedAt() { return updatedAt; }
    public String getTopicId() { return topicId; }
    public String getTopicName() { return topicName; }
    public String getUserId() { return userId; }
    public String getUsername() { return username; }
    public int getCountLike() { return countLike; }
    public int getCountDislike() { return countDislike; }
    public int getCountView() { return countView; }
    public List<PostBlock> getContentBlock() { return contentBlock; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setContent(String content) { this.content = content; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setUserImage(String userImage) {this.userImage=userImage;}
    public void setStatus(String status) { this.status = status; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
    public void setTopicId(String topicId) { this.topicId = topicId; }
    public void setTopicName(String topicName) { this.topicName = topicName; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setUsername(String username) { this.username = username; }
    public void setCountLike(int countLike) { this.countLike = countLike; }
    public void setCountDislike(int countDislike) { this.countDislike = countDislike; }
    public void setCountView(int countView) { this.countView = countView; }
    public void setContentBlock(List<PostBlock> contentBlock) { this.contentBlock = contentBlock; }
}
