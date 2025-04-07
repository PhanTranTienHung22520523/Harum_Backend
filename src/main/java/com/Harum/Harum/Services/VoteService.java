package com.Harum.Harum.Services;

import com.Harum.Harum.Enums.VoteTypes;
import com.Harum.Harum.Models.Posts;
import com.Harum.Harum.Models.Votes;
import com.Harum.Harum.Repository.PostRepo;
import com.Harum.Harum.Repository.VoteRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VoteService {
    @Autowired
    private VoteRepo voteRepository;
    @Autowired
    private PostRepo postRepository;
    // tao vote moi
    public Votes createVote (Votes vote)
    {
        Votes savedVote = voteRepository.save(vote);
        // Lấy bài viết theo postId
        Posts post = postRepository.findById(vote.getPostId()).orElse(null);

        if (post != null) {
            // Kiểm tra voteType và cập nhật bài viết tương ứng
            if (vote.getVoteType() == VoteTypes.UPVOTE) {
                post.setCountLike(post.getCountLike() + 1);  // Tăng countLike lên
            } else if (vote.getVoteType() == VoteTypes.DOWNVOTE) {
                post.setCountDislike(post.getCountDislike() + 1);  // Tăng countDislike lên
            }
            postRepository.save(post);  // Lưu bài viết sau khi cập nhật
        }

        return savedVote;
    }
    // Kiểm tra xem user đã tương tác với post hay chưa
    public Optional<Votes> getVoteByUserAndPost(String userId, String postId) {
        return voteRepository.findByUserIdAndPostId(userId, postId);
    }


}
