package com.Harum.Harum.Repository;

import com.Harum.Harum.Enums.ReportStatus;
import com.Harum.Harum.Models.Comments;
import com.Harum.Harum.Models.Posts;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepo extends MongoRepository<Comments, String> {
    List<Comments> findByPostId(String postId);
    List<Comments> findByUserId(String userId);
    long countByPostId(String postId);
    List<Comments> findByReportStatus(ReportStatus status);
}
