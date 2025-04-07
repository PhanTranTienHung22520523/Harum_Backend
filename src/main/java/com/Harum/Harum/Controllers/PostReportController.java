package com.Harum.Harum.Controllers;

import com.Harum.Harum.Enums.ReportStatus;
import com.Harum.Harum.Models.PostReports;
import com.Harum.Harum.Services.PostReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/post_reports/")
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
    public List<PostReports> getReportsByPost(@PathVariable String postId) {
        return postReportService.getReportsByPostId(postId);
    }

    // 3. Lấy danh sách báo cáo theo trạng thái
    @GetMapping("/status/{status}")
    public List<PostReports> getReportsByStatus(@PathVariable ReportStatus status) {
        return postReportService.getReportsByStatus(status);
    }

    // 4. Cập nhật trạng thái báo cáo
    @PutMapping("/{reportId}")
    public String updateReportStatus(@PathVariable String reportId, @RequestBody ReportStatus status) {
        return postReportService.updateReportStatus(reportId, status);
    }
}
