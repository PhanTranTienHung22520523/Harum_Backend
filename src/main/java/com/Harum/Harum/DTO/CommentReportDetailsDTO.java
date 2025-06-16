package com.Harum.Harum.DTO;


import lombok.Data;

@Data
public class CommentReportDetailsDTO {
    private String reportId;
    private String reporterId;
    private String reporterName;
    private String reporterAvatar;

    private String commentId;
    private String commentContent;
    private String commentOwnerId;
    private String commentOwnerName;
    private String commentOwnerAvatar;

    private String reason;

    private String status;
    private String createdAt;

    public void ReportDetailsDTO(String reportId, String reporterId, String reporterName, String reporterAvatar,
                            String commentId, String commentContent, String commentOwnerId,
                            String commentOwnerName, String commentOwnerAvatar,
                            String status, String createdAt, String reason) {
        this.reportId = reportId;
        this.reporterId = reporterId;
        this.reporterName = reporterName;
        this.reporterAvatar = reporterAvatar;
        this.commentId = commentId;
        this.commentContent = commentContent;
        this.commentOwnerId = commentOwnerId;
        this.commentOwnerName = commentOwnerName;
        this.commentOwnerAvatar = commentOwnerAvatar;
        this.status = status;
        this.createdAt = createdAt;
        this.reason=reason;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getReporterId() {
        return reporterId;
    }

    public void setReporterId(String reporterId) {
        this.reporterId = reporterId;
    }

    public String getReporterName() {
        return reporterName;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }

    public String getReporterAvatar() {
        return reporterAvatar;
    }

    public void setReporterAvatar(String reporterAvatar) {
        this.reporterAvatar = reporterAvatar;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getCommentOwnerId() {
        return commentOwnerId;
    }

    public void setCommentOwnerId(String commentOwnerId) {
        this.commentOwnerId = commentOwnerId;
    }

    public String getCommentOwnerName() {
        return commentOwnerName;
    }

    public void setCommentOwnerName(String commentOwnerName) {
        this.commentOwnerName = commentOwnerName;
    }

    public String getCommentOwnerAvatar() {
        return commentOwnerAvatar;
    }

    public void setCommentOwnerAvatar(String commentOwnerAvatar) {
        this.commentOwnerAvatar = commentOwnerAvatar;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
