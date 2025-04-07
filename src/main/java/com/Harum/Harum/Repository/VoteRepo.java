package com.Harum.Harum.Repository;

import com.Harum.Harum.Models.Views;
import com.Harum.Harum.Models.Votes;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoteRepo extends MongoRepository<Votes,String> {
    // Tìm một vote dựa trên userId và postId
    Optional<Votes> findByUserIdAndPostId(String userId, String postId);
}
