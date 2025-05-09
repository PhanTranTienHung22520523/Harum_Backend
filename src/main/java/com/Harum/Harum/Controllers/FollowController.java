package com.Harum.Harum.Controllers;


import com.Harum.Harum.Models.Follows;
import com.Harum.Harum.Services.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/follow")
public class FollowController {
    @Autowired
    private FollowService followService;

    @GetMapping("/followers/{userId}")
    public List<Follows> getFollowers(@PathVariable String userId) {
        return followService.getFollowers(userId);
    }

    @GetMapping("/following/{userId}")
    public List<Follows> getFollowing(@PathVariable String userId) {
        return followService.getFollowing(userId);
    }

    @GetMapping("/is-following")
    public boolean isFollowing(@RequestParam String followerId, @RequestParam String followedId) {
        return followService.isFollowing(followerId, followedId);
    }

    @PostMapping
    public ResponseEntity<Follows> followUser(@RequestParam String followerId, @RequestParam String followedId) {
        Follows followed = followService.followUser(followerId, followedId);
        if (followed != null) {
            return ResponseEntity.ok(followed);
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> unfollowUser(@RequestParam String followerId, @RequestParam String followedId) {
        followService.unfollowUser(followerId, followedId);
        return ResponseEntity.noContent().build();
    }
}
