package com.Harum.Harum.Repository;

import com.Harum.Harum.Models.Comments;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentRepo extends MongoRepository<Comments, String> {
    List<Comments> findByPostId(String postId);
    List<Comments> findByUserId(String userId);
    long countByPostId(String postId);
}
