package com.Harum.Harum.Repository;

import com.Harum.Harum.Models.Follows;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepo extends MongoRepository<Follows, String> {
    long countByFollowedId(String followedId); //Get following number
    long countByFollowerId(String followerId); //Get follower number
    List<Follows> findByFollowerId(String followerId);
    List<Follows> findByFollowedId(String followedId);
    boolean existsByFollowerIdAndFollowedId(String followerId, String followedId);
    void deleteByFollowerIdAndFollowedId(String followerId, String followedId);
}
