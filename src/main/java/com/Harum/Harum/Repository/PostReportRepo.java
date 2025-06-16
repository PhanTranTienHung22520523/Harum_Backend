package com.Harum.Harum.Repository;

import com.Harum.Harum.Enums.ReportStatus;
import com.Harum.Harum.Models.PostReports;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostReportRepo extends MongoRepository<PostReports, String> {

    // Tìm report theo người báo cáo và bài viết để kiểm tra trùng lặp
    Optional<PostReports> findByReporterIdAndPostId(String reporterId, String postId);

    // Lấy danh sách report theo bài viết
    List<PostReports> findByPostId(String postId);

    // Lấy danh sách report theo trạng thái
    List<PostReports> findByStatus(ReportStatus status);

}
