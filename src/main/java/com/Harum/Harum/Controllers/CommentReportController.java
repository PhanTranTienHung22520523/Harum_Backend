package com.Harum.Harum.Controllers;

import com.Harum.Harum.DTO.CommentReportDetailsDTO;
import com.Harum.Harum.Enums.ReportStatus;
import com.Harum.Harum.Models.CommentReports;
import com.Harum.Harum.Services.CommentReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment_reports")
public class CommentReportController {

    @Autowired
    private CommentReportService commentReportService;

    // 1. Gửi báo cáo bình luận
    @PostMapping
    public String reportComment(@RequestBody CommentReports report) {
        return commentReportService.submitReport(report);
    }

    // 2. Lấy danh sách báo cáo theo commentId
    @GetMapping("/comment/{commentId}")
    public List<CommentReportDetailsDTO> getReportsByComment(@PathVariable String commentId) {
        return commentReportService.getReportsByCommentId(commentId);
    }

    // 3. Lấy danh sách báo cáo theo trạng thái
    @GetMapping("/status")
    public ResponseEntity<?> getReportsByStatus(@RequestParam String status) {
        try {
            ReportStatus reportStatus = ReportStatus.valueOf(status.toUpperCase());
            List<CommentReportDetailsDTO> reports = commentReportService.getReportsByStatus(reportStatus);
            return ResponseEntity.ok(reports);
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body("ReportStatus không hợp lệ. Các giá trị hợp lệ là: PENDING, REVIEW, RESOLVED.");
        }
    }

    // 4. Cập nhật trạng thái báo cáo
    @PutMapping("/{reportId}")
    public String updateReportStatus(@PathVariable String reportId, @RequestBody ReportStatus status) {
        return commentReportService.updateReportStatus(reportId, status);
    }


    //lay all
    @GetMapping("/all")
    public ResponseEntity<List<CommentReportDetailsDTO>> getAllCommentReports() {
        return ResponseEntity.ok(commentReportService.getAllReports());
    }
}
