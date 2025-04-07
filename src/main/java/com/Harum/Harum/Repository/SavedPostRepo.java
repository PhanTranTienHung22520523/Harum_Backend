package com.Harum.Harum.Repository;

import com.Harum.Harum.Models.SavedPosts;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavedPostRepo extends MongoRepository<SavedPosts, String> {
    List<SavedPosts> findByUserId( String userId);
    Optional<SavedPosts> findByUserIdAndPostId(String userId, String postId);
}
