package com.Harum.Harum.Services;

import com.Harum.Harum.Models.Follows;
import com.Harum.Harum.Models.Users;
import com.Harum.Harum.Repository.FollowRepo;
import com.Harum.Harum.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FollowService {

    @Autowired
    private FollowRepo followRepository;
    @Autowired
    private UserRepo userRepository;
    // Toggle follow/unfollow
    public String toggleFollow(String followerId, String followedId) {
        Optional<Follows> existingFollow = followRepository.findByFollowerIdAndFollowedId(followerId, followedId);

        if (existingFollow.isPresent()) {
            // Unfollow
            followRepository.deleteByFollowerIdAndFollowedId(followerId, followedId);
            return "Unfollowed successfully";
        } else {
            // Follow
            Follows newFollow = new Follows(followerId, followedId);
            followRepository.save(newFollow);
            return "Followed successfully";
        }
    }

    // Check if following
    public boolean isFollowing(String followerId, String followedId) {
        return followRepository.findByFollowerIdAndFollowedId(followerId, followedId).isPresent();
    }



    // Get list of followed User objects with pagination
    public List<Users> getFollowedUsers(String followerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Follows> follows = followRepository.findByFollowerId(followerId, pageable);

        List<String> followedUserIds = follows.stream()
                .map(Follows::getFollowedId)
                .collect(Collectors.toList());

        return userRepository.findAllById(followedUserIds);
    }
}
