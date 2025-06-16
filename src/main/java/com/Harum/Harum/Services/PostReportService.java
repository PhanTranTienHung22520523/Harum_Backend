package com.Harum.Harum.Services;

import com.Harum.Harum.DTO.PostReportDetailsDTO;
import com.Harum.Harum.Enums.ReportStatus;
import com.Harum.Harum.Models.CommentReports;
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

    @Autowired
    private UserService userService;


    private PostReportDetailsDTO toPostReportDetailsDTO(PostReports report) {
        PostReportDetailsDTO dto = new PostReportDetailsDTO();

        dto.setReportId(report.getId());
        dto.setReporterId(report.getReporterId());
        dto.setPostId(report.getPostId());
        dto.setReason(report.getReason());
        dto.setStatus(report.getStatus().name());
        dto.setCreatedAt(report.getCreatedAt());

        // Lấy thông tin người report
        userService.getUserById(report.getReporterId()).ifPresent(user -> {
            dto.setReporterName(user.getUsername());
            dto.setReporterAvatar(user.getAvatarUrl());
        });

        return dto;
    }

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
    public List<PostReportDetailsDTO> getReportsByPostId(String postId) {
        return postReportRepo.findByPostId(postId)
                .stream()
                .map(this::toPostReportDetailsDTO)
                .toList();
    }

    // 3. Lấy danh sách report theo trạng thái
    public List<PostReportDetailsDTO> getReportsByStatus(ReportStatus status) {
        return postReportRepo.findByStatus(status)
                .stream()
                .map(this::toPostReportDetailsDTO)
                .toList();
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

    // 5. Lấy tất cả post report
    public List<PostReportDetailsDTO> getAllReports() {
        List<PostReports> reports = postReportRepo.findAll();
        return reports.stream()
                .map(this::toPostReportDetailsDTO)
                .toList();
    }

    //lay theo id
    public Optional<PostReportDetailsDTO> getReportById(String reportId) {
        return postReportRepo.findById(reportId)
                .map(this::toPostReportDetailsDTO);
    }
}
