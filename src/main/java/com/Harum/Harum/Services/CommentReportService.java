package com.Harum.Harum.Services;

import com.Harum.Harum.DTO.CommentReportDetailsDTO;
import com.Harum.Harum.Enums.ReportStatus;
import com.Harum.Harum.Models.CommentReports;
import com.Harum.Harum.Models.Comments;
import com.Harum.Harum.Models.Users;
import com.Harum.Harum.Repository.CommentReportRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentReportService {

    @Autowired
    private  CommentReportRepo commentReportRepo;

    @Autowired
    private  UserService userService;

    @Autowired
    private  CommentService commentService;

    public CommentReportService(CommentReportRepo commentReportRepo, UserService userService, CommentService commentService) {
        this.commentReportRepo = commentReportRepo;
        this.userService = userService;
        this.commentService = commentService;
    }

    private CommentReportDetailsDTO convertToDetailsDTO(CommentReports report) {
        CommentReportDetailsDTO dto = new CommentReportDetailsDTO();
        dto.setReportId(report.getId());
        dto.setReporterId(report.getReporterId());
        dto.setCommentId(report.getCommentId());
        dto.setStatus(report.getStatus().name());
        dto.setCreatedAt(report.getCreatedAt());
        dto.setReason(report.getReason());

        System.out.println("xong cmt");



        // Lấy thông tin người báo cáo
        userService.getUserById(report.getReporterId()).ifPresent(reporter -> {
            dto.setReporterName(reporter.getUsername());
            dto.setReporterAvatar(reporter.getAvatarUrl());
        });

        System.out.println("xong lay ng report");
        // Lấy thông tin comment và chủ comment
        commentService.getCommentByIdd(report.getCommentId()).ifPresent(comment -> {
            System.out.println("Lay duoc id");
            dto.setCommentContent(comment.getContent());
            dto.setCommentOwnerId(comment.getUserId());
            System.out.println("theo comment: "+comment.getUserId());
            System.out.println("theo dto: "+dto.getCommentOwnerId());
            System.out.println("comment id: "+dto.getCommentId());
            System.out.println("comment id theo report: "+report.getCommentId());;
        });
        userService.getUserByCommentId(report.getCommentId()).ifPresent(owner -> {
            System.out.println("Lay duoc id ng comment: "+ owner.getId()+owner.getUsername()+owner.getAvatarUrl());
            dto.setCommentOwnerName(owner.getUsername());
            System.out.println("done");
            dto.setCommentOwnerAvatar(owner.getAvatarUrl());
            System.out.println("done2");
        });
//        Optional<Users> user=userService.getUserByCommentId(report.getCommentId());
//        System.out.println("Lay duoc id ng comment: "+ user.get().getId()+user.get().getUsername()+user.get().getAvatarUrl());
//            if(user.isPresent()){
//                dto.setCommentOwnerAvatar(user.get().getAvatarUrl());
//                dto.setCommentOwnerName(user.get().getUsername());
//            }

//        Optional<Comments> comment=commentService.getCommentById(report.getCommentId());
//        if (comment.isPresent()) {
//            dto.setCommentContent(comment.get().getContent());
//            dto.setCommentOwnerId(comment.get().getUserId());
//            Optional<Users> user=userService.getUserById(comment.get().getUserId());
//            if(user.isPresent()){
//                dto.setCommentOwnerAvatar(user.get().getAvatarUrl());
//                dto.setCommentOwnerName(user.get().getUsername());
//            }
//        }

        return dto;
    }

    // 1. Gửi report (nếu chưa tồn tại)
    public String submitReport(CommentReports report) {
        Optional<CommentReports> existingReport = commentReportRepo.findByReporterIdAndCommentId(report.getReporterId(), report.getCommentId());

        if (existingReport.isPresent()) {
            return "Already reported";
        }

        report.setStatus(ReportStatus.PENDING); // mặc định
        commentReportRepo.save(report);
        return "Report submitted";
    }

    // 2. Lấy danh sách report theo comment
    @Transactional
    public List<CommentReportDetailsDTO> getReportsByCommentId(String commentId) {
        return commentReportRepo.findByCommentId(commentId)
                .stream()
                .map(this::convertToDetailsDTO)
                .toList();
    }

    // 3. Lấy danh sách report theo trạng thái
    @Transactional
    public List<CommentReportDetailsDTO> getReportsByStatus(ReportStatus status) {
        return commentReportRepo.findByStatus(status)
                .stream()
                .map(this::convertToDetailsDTO)
                .toList();
    }

    // 4. Cập nhật trạng thái report
    public String updateReportStatus(String reportId, ReportStatus status) {
        CommentReports report = commentReportRepo.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        report.setStatus(status);
        commentReportRepo.save(report);
        return "Status updated";
    }

    // 5. Lấy tất cả comment report
    @Transactional
    public List<CommentReportDetailsDTO> getAllReports() {
        return commentReportRepo.findAll().stream()
                .filter(report -> report.getCommentId() != null)
                .map(this::convertToDetailsDTO)
                .collect(Collectors.toList());
    }
}
