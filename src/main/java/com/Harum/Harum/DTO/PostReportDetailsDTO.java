package com.Harum.Harum.DTO;

import com.Harum.Harum.Enums.ReportStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;

import java.time.Instant;
import java.util.Date;

@Getter
@Setter
public class PostReportDetailsDTO {
    private String reportId;
    private String reporterId;
    private String reporterName;
    private String reporterAvatar;
    private String postId;
    private String reason;
    private String status;
    private String createdAt;

    // Constructor không tham số
    public PostReportDetailsDTO() {
    }

    // Constructor đầy đủ
    public PostReportDetailsDTO(String reportId, String reporterId, String reporterName, String reporterAvatar,
            String postId, String reason, String status, String createdAt) {
        this.reportId = reportId;
        this.reporterId = reporterId;
        this.reporterName = reporterName;
        this.reporterAvatar = reporterAvatar;
        this.postId = postId;
        this.reason = reason;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getter & Setter
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

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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
