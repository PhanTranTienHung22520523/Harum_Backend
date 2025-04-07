package com.Harum.Harum.Repository;


import com.Harum.Harum.Models.CommentReports;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentReportRepo extends MongoRepository<CommentReports, String> {

    // Tìm report theo người báo cáo và bài viết để kiểm tra trùng lặp
    Optional<CommentReports> findByReporterIdAndCommentId(String reporterId, String commentId);

    // Lấy danh sách report theo bài viết
    List<CommentReports> findByCommentId(String commentId);

    // Lấy danh sách report theo trạng thái
    List<CommentReports> findByStatus(Enum status);
}
