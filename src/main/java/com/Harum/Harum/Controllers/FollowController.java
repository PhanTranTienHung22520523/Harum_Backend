package com.Harum.Harum.Controllers;

import com.Harum.Harum.Models.Users;
import com.Harum.Harum.Services.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/follow")
public class FollowController {

    @Autowired
    private FollowService followService;

    // Toggle follow/unfollow
    @PostMapping("/interact/{followerId}/{followedId}")
    public String toggleFollow(@PathVariable String followerId, @PathVariable String followedId) {
        return followService.toggleFollow(followerId, followedId);
    }
    @GetMapping("/check/{followerId}/{followedId}")
    public boolean isFollowing(@PathVariable String followerId, @PathVariable String followedId) {
        return followService.isFollowing(followerId, followedId);
    }

    // Get followed users (with pagination)
    @GetMapping("/followed-users/{followerId}/{page}/{size}")
    public List<Users> getFollowedUsers(
            @PathVariable String followerId,
            @PathVariable int page,
            @PathVariable int size) {
        return followService.getFollowedUsers(followerId, page, size);
    }

    @GetMapping("/followers/{userId}/{page}/{size}")
    public List<Users> getFollowers(
            @PathVariable String userId,
            @PathVariable int page,
            @PathVariable int size) {
        return followService.getFollowers(userId, page, size);
    }
}
