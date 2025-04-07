package com.Harum.Harum.Controllers;

import com.Harum.Harum.Enums.ReportStatus;
import com.Harum.Harum.Models.CommentReports;
import com.Harum.Harum.Services.CommentReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment_reports/ ")
public class CommentReportController {

    @Autowired
    private CommentReportService commentReportService;

    // 1. Gửi báo cáo bình luận
    @PostMapping
    public String reportComment(@RequestBody CommentReports report) {
        return commentReportService.reportComment(report);
    }

    // 2. Lấy danh sách báo cáo theo commentId
    @GetMapping("/comment/{commentId}")
    public List<CommentReports> getReportsByComment(@PathVariable String commentId) {
        return commentReportService.getReportsByCommentId(commentId);
    }

    // 3. Lấy danh sách báo cáo theo trạng thái
    @GetMapping("/status/{status}")
    public List<CommentReports> getReportsByStatus(@PathVariable ReportStatus status) {
        return commentReportService.getReportsByStatus(status);
    }

    // 4. Cập nhật trạng thái báo cáo
    @PutMapping("/{reportId}")
    public String updateReportStatus(@PathVariable String reportId, @RequestBody ReportStatus status) {
        return commentReportService.updateReportStatus(reportId, status);
    }
}
