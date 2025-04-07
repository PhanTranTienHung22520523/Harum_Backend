package com.Harum.Harum.Controllers;

import com.Harum.Harum.Models.Views;
import com.Harum.Harum.Models.Votes;
import com.Harum.Harum.Services.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/vote")
public class VoteController {
    @Autowired
    private VoteService voteService;

    // tạo moi
    @PostMapping
    public ResponseEntity<Votes> createVote(@RequestBody Votes vote )
    {
        try {
            Votes createdVote = voteService.createVote(vote);
            return new ResponseEntity<>(createdVote, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // Kiểm tra xem user đã tương tác với post chưa

    @GetMapping("/check/{userId}/{postId}")
    public Optional<Votes> checkUserVote(@PathVariable String userId, @PathVariable String postId) {
        return voteService.getVoteByUserAndPost(userId, postId);
    }
}
