package com.Harum.Harum.Models;

import com.Harum.Harum.Enums.CommentStatus;
import com.Harum.Harum.Enums.ReportStatus;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Document(collection = "comments")
@Data
public class Comments {
    @Id
    private String id;
    private String userId;
    private String postId;
    private String content;
    private String createdAt = Instant.now().toString();
    private List<Comments> replyComments;
    private String parentId;
    private String status = "ENABLE";
    private ReportStatus reportStatus;
    private String updatedAt;

    public Comments() {
    }

    public Comments(String userId, String postId, String content) {
        this.userId = userId;
        this.postId = postId;
        this.content = content;
        this.createdAt = Instant.now().toString();
    }

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public List<Comments> getReplyComments() {
        return replyComments;
    }

    public void setReplyComments(List<Comments> replyComments) {
        this.replyComments = replyComments;
    }

    public void setParentId(String parentCommentId) {
        this.parentId = parentCommentId;
    }

    public String getParentIdc() {
        return parentId;
    }

    public void setReportStatus(ReportStatus reportStatus) {
        this.reportStatus = reportStatus;
    }

    public ReportStatus getReportStatus() {
        return this.reportStatus;
    }

    public void setUpdatedAt(Date date) {
        this.updatedAt = date.toString();
    }

    public String getStatus() {
        return null;
    }

    public void setStatus(String disable) {
    }
}