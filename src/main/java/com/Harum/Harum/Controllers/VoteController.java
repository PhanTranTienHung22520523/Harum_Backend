package com.Harum.Harum.Controllers;

import com.Harum.Harum.Models.Votes;
import com.Harum.Harum.Services.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/vote")
public class VoteController {

    @Autowired
    private VoteService voteService;

    // 1. Kiểm tra user đã vote bài viết chưa
    @GetMapping("/check/{userId}/{postId}")
    public ResponseEntity<Optional<Votes>> checkVote(@PathVariable String userId, @PathVariable String postId) {
        Optional<Votes> vote = voteService.checkUserVote(userId, postId);
        return ResponseEntity.ok(vote);
    }

    // 2. Xử lý tương tác vote (create, update, remove)
    @PostMapping("/interact")
    public ResponseEntity<String> interactVote(@RequestBody Votes vote) {
        String result = voteService.interactVote(vote);
        return ResponseEntity.ok(result);
    }
}
