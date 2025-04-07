package com.Harum.Harum.Services;

import com.Harum.Harum.Enums.ReportStatus;
import com.Harum.Harum.Models.PostReports;
import com.Harum.Harum.Repository.PostReportRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostReportService {

    @Autowired
    private PostReportRepo postReportRepo;

    // 1. Gửi report (nếu chưa tồn tại)
    public String reportPost(PostReports report) {
        Optional<PostReports> existingReport = postReportRepo.findByReporterIdAndPostId(report.getReporterId(), report.getPostId());

        if (existingReport.isPresent()) {
            return "Already reported";
        }

        report.setStatus(ReportStatus.PENDING); // mặc định
        postReportRepo.save(report);
        return "Report submitted";
    }

    // 2. Lấy danh sách report theo bài viết
    public List<PostReports> getReportsByPostId(String postId) {
        return postReportRepo.findByPostId(postId);
    }

    // 3. Lấy danh sách report theo trạng thái
    public List<PostReports> getReportsByStatus(ReportStatus status) {
        return postReportRepo.findByStatus(status);
    }

    // 4. Cập nhật trạng thái report

    // xu ly report
    public String updateReportStatus(String reportId, ReportStatus status) {
        Optional<PostReports> reportOpt = postReportRepo.findById(reportId);

        if (reportOpt.isPresent()) {
            PostReports report = reportOpt.get();
            report.setStatus(status);
            postReportRepo.save(report);
            return "Status updated";
        } else {
            return "Report not found";
        }
    }
}
