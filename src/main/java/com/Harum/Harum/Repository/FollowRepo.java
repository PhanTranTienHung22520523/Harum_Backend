package com.Harum.Harum.Repository;

import com.Harum.Harum.Models.Follows;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepo extends MongoRepository<Follows, String> {
    long countByFollowedId(String followedId); //Get following number
    long countByFollowerId(String followerId); //Get follower number
}
