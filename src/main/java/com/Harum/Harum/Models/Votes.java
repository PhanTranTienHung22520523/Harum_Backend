package com.Harum.Harum.Models;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import java.time.Instant;
import com.Harum.Harum.Enums.VoteTypes;
@Data
@Document(collection = "votes")
public class Votes {
    @Id
    private String id;
    private String userId;
    private String postId;
    private VoteTypes voteType;
    private String createdAt;

    public Votes() {}

    public Votes(String userId, String postId, VoteTypes voteType) {
        this.userId = userId;
        this.postId = postId;
        this.voteType = voteType;
        this.createdAt = Instant.now().toString();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getPostId() { return postId; }
    public void setPostId(String postId) { this.postId = postId; }
    public VoteTypes getVoteType() { return voteType; }
    public void setVoteType(VoteTypes voteType) { this.voteType = voteType; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt() { this.createdAt = Instant.now().toString(); }
}
