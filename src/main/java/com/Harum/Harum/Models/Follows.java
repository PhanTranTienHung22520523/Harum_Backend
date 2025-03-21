package com.Harum.Harum.Models;


import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import java.time.Instant;

@Document (collection = "follows")
@Data
public class Follows {
    @Id
    private String id;
    private String followerId;
    private String followedId;
    private String createdAt;

    public Follows() {}

    public Follows(String followerId, String followedId) {
        this.followerId = followerId;
        this.followedId = followedId;
        this.createdAt = Instant.now().toString();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getFollowerId() { return followerId; }
    public void setFollowerId(String followerId) { this.followerId = followerId; }
    public String getFollowedId() { return followedId; }
    public void setFollowedId(String followedId) { this.followedId = followedId; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt() { this.createdAt = Instant.now().toString(); }
}
