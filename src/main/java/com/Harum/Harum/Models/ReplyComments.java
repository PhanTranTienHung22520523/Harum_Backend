package com.Harum.Harum.Models;


import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import java.time.Instant;

@Document (collection = "reply_comments")
@Data
public class ReplyComments {
    @Id
    private String id;
    private String rootCommentId;
    private String userId;
    private String content;
    private String createdAt;

    public ReplyComments() {}
    public ReplyComments(String rootCommentId, String userId, String content) {
        this.rootCommentId = rootCommentId;
        this.userId = userId;
        this.content = content;
        this.createdAt = Instant.now().toString();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getRootCommentId() { return rootCommentId; }
    public void setRootCommentId(String rootCommentId) { this.rootCommentId = rootCommentId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt() { this.createdAt = Instant.now().toString(); }
}
