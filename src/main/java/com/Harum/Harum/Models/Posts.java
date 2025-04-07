package com.Harum.Harum.Models;


import com.Harum.Harum.Enums.PostStatus;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import java.time.Instant;
import java.util.Date;

@Data
@Document(collection = "posts")
public class Posts {
    @Id
    private String id;
    private String userId;
    private String title;
    private String content;
    private String imageUrl;
    private PostStatus status;
    private String createdAt;
    private String updatedAt;
    @DBRef  // Liên kết tới Topic
    private Topics topic;  // Thay vì dùng topicId kiểu String
    private int countLike;
    private int countDislike;
    private int countView;

    public Posts() {

            this.countLike = 0;      // Mặc định là 0
            this.countDislike = 0;   // Mặc định là 0
            this.countView = 0;      // Mặc định là 0

    }

    public Posts(String userId, String title, String content, String imageUrl, PostStatus status, Date updatedAt,  Topics topic) {
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.status = status;
        this.createdAt = Instant.now().toString();
        this.updatedAt = updatedAt.toString();
        this.topic = topic;
        this.countLike = 0;      // Mặc định là 0
        this.countDislike = 0;   // Mặc định là 0
        this.countView = 0;      // Mặc định là 0
    }
    // Getter và Setter cho Topic
    public Topics getTopic() {
        return topic;
    }
    public int getCountLike() {
        return countLike;
    }

    public void setCountLike(int countLike) {
        this.countLike = countLike;
    }

    public int getCountDislike() {
        return countDislike;
    }

    public void setCountDislike(int countDislike) {
        this.countDislike = countDislike;
    }

    public int getCountView() {
        return countView;
    }

    public void setCountView(int countView) {
        this.countView = countView;
    }
    public void setTopic(Topics topic) {
        this.topic = topic;
    }
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public PostStatus getStatus() { return status; }
    public void setStatus(PostStatus status) { this.status = status; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt() { this.createdAt = Instant.now().toString(); }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt.toString(); }
}
