package com.Harum.Harum.Services;

import com.Harum.Harum.Enums.ReportStatus;
import com.Harum.Harum.Models.CommentReports;
import com.Harum.Harum.Repository.CommentReportRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentReportService {

    @Autowired
    private CommentReportRepo commentReportRepo;

    // 1. Gửi report (nếu chưa tồn tại)
    public String reportComment(CommentReports report) {
        Optional<CommentReports> existingReport = commentReportRepo.findByReporterIdAndCommentId(report.getReporterId(), report.getCommentId());

        if (existingReport.isPresent()) {
            return "Already reported";
        }

        report.setStatus(ReportStatus.PENDING); // mặc định
        commentReportRepo.save(report);
        return "Report submitted";
    }

    // 2. Lấy danh sách report theo comment
    public List<CommentReports> getReportsByCommentId(String commentId) {
        return commentReportRepo.findByCommentId(commentId);
    }

    // 3. Lấy danh sách report theo trạng thái
    public List<CommentReports> getReportsByStatus(ReportStatus status) {
        return commentReportRepo.findByStatus(status);
    }

    // 4. Cập nhật trạng thái report
    public String updateReportStatus(String reportId, ReportStatus status) {
        Optional<CommentReports> reportOpt = commentReportRepo.findById(reportId);

        if (reportOpt.isPresent()) {
            CommentReports report = reportOpt.get();
            report.setStatus(status);
            commentReportRepo.save(report);
            return "Status updated";
        } else {
            return "Report not found";
        }
    }
}
