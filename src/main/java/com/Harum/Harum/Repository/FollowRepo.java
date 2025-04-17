package com.Harum.Harum.Repository;

import com.Harum.Harum.Models.Follows;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepo extends MongoRepository<Follows, String> {
    long countByFollowedId(String followedId); //Get following number
    long countByFollowerId(String followerId); //Get follower number
    Optional<Follows> findByFollowerIdAndFollowedId(String followerId, String followedId);
    void deleteByFollowerIdAndFollowedId(String followerId, String followedId);
    List<Follows> findByFollowerId(String followerId, Pageable pageable);
}
