package com.Harum.Harum.DTO;

import com.Harum.Harum.Enums.PostStatus;
import com.Harum.Harum.Enums.ReportStatus;
import com.Harum.Harum.Models.PostBlock;

import java.util.List;

public class PostDetailsDTO {
    private String id;
    private String title;
    private String content;
    private String imageUrl;
    private String topicId;
    private String topicName;
    private String userId;
    private String username;
    private String avatarUrl;
    private List<PostBlock> contentBlock;
    private int countLike;
    private int countDislike;
    private int countView;
    private String createdAt;
    private String updatedAt;
    private PostStatus status;
    private ReportStatus reportStatus;

    // Constructor không đối số
    public PostDetailsDTO() {}

    // Constructor đầy đủ
    public PostDetailsDTO(String id, String title, String content, String imageUrl,
                          String topicId, String topicName, String userId, String username,
                          String avatarUrl, List<PostBlock> contentBlock, int countLike,
                          int countDislike, int countView, String createdAt, String updatedAt,
                          PostStatus status, ReportStatus reportStatus) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.topicId = topicId;
        this.topicName = topicName;
        this.userId = userId;
        this.username = username;
        this.avatarUrl = avatarUrl;
        this.contentBlock = contentBlock;
        this.countLike = countLike;
        this.countDislike = countDislike;
        this.countView = countView;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.status = status;
        this.reportStatus = reportStatus;
    }

    // Getters và Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getTopicId() { return topicId; }
    public void setTopicId(String topicId) { this.topicId = topicId; }

    public String getTopicName() { return topicName; }
    public void setTopicName(String topicName) { this.topicName = topicName; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public List<PostBlock> getContentBlock() { return contentBlock; }
    public void setContentBlock(List<PostBlock> contentBlock) { this.contentBlock = contentBlock; }

    public int getCountLike() { return countLike; }
    public void setCountLike(int countLike) { this.countLike = countLike; }

    public int getCountDislike() { return countDislike; }
    public void setCountDislike(int countDislike) { this.countDislike = countDislike; }

    public int getCountView() { return countView; }
    public void setCountView(int countView) { this.countView = countView; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

    public PostStatus getStatus() { return status; }
    public void setStatus(PostStatus status) { this.status = status; }

    public ReportStatus getReportStatus() { return reportStatus; }
    public void setReportStatus(ReportStatus reportStatus) { this.reportStatus = reportStatus; }
}
