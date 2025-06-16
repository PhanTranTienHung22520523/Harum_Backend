package com.Harum.Harum.Controllers;

import com.Harum.Harum.DTO.PostReportDetailsDTO;
import com.Harum.Harum.Enums.ReportStatus;
import com.Harum.Harum.Models.CommentReports;
import com.Harum.Harum.Models.PostReports;
import com.Harum.Harum.Services.PostReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/post_reports")
public class PostReportController {

    @Autowired
    private PostReportService postReportService;

    // 1. Gửi báo cáo bài viết
    @PostMapping
    public String reportPost(@RequestBody PostReports report) {
        return postReportService.reportPost(report);
    }

    // 2. Lấy danh sách báo cáo theo postId
    @GetMapping("/post/{postId}")
    public List<PostReportDetailsDTO> getReportsByPost(@PathVariable String postId) {
        return postReportService.getReportsByPostId(postId);
    }

    // 3. Lấy danh sách báo cáo theo trạng thái
    @GetMapping("/status")
    public ResponseEntity<?> getReportsByStatus(@RequestParam String status) {
        try {
            ReportStatus reportStatus = ReportStatus.valueOf(status.toUpperCase());
            List<PostReportDetailsDTO> reports = postReportService.getReportsByStatus(reportStatus);
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
        return postReportService.updateReportStatus(reportId, status);
    }

    //lay all
    @GetMapping("/all")
    public ResponseEntity<List<PostReportDetailsDTO>> getAllPostReports() {
        return ResponseEntity.ok(postReportService.getAllReports());
    }
}
