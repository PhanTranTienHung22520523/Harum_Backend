package com.Harum.Harum.Models;


import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import java.time.Instant;
import java.util.Date;

import com.Harum.Harum.Enums.ReportStatus;
@Data
@Document(collection = "reports")
public class PostReports {
    @Id
    private String id;
    private String reporterId;
    private String postId;
    private String reason;
    private ReportStatus status;
    private String createdAt=Instant.now().toString();

    public PostReports() {}

    public PostReports(String reporterId, String postId, String reason, ReportStatus status) {
        this.reporterId = reporterId;
        this.postId = postId;
        this.reason = reason;
        this.status = status;
        this.createdAt = Instant.now().toString();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getReporterId() { return reporterId; }
    public void setReporterId(String reporterId) { this.reporterId = reporterId; }
    public String getPostId() { return postId; }
    public void setPostId(String postId) { this.postId = postId; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public ReportStatus getStatus() { return status; }
    public void setStatus(ReportStatus status) { this.status = status; }
    public String getCreatedAt() { return createdAt; }
}
