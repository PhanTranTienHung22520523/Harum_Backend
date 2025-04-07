package com.Harum.Harum.Services;

import com.Harum.Harum.Enums.VoteTypes;
import com.Harum.Harum.Models.Posts;
import com.Harum.Harum.Models.Votes;
import com.Harum.Harum.Repository.PostRepo;
import com.Harum.Harum.Repository.VoteRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VoteService {

    @Autowired
    private VoteRepo voteRepository;

    @Autowired
    private PostRepo postRepository;

    // 1. Hàm kiểm tra đã vote chưa
    public Optional<Votes> checkUserVote(String userId, String postId) {
        return voteRepository.findByUserIdAndPostId(userId, postId);
    }

    // 2. Hàm xử lý tương tác (vote hoặc cập nhật hoặc xóa vote)
    public String interactVote( Votes vote) {
        String userId = vote.getUserId();
        String postId = vote.getPostId();
        VoteTypes newVoteType  = vote.getVoteType();
        Optional<Votes> existingVoteOpt = voteRepository.findByUserIdAndPostId(userId, postId);
        Posts post = postRepository.findById(postId).orElse(null);

        if (post == null) return "Post not found";

        if (existingVoteOpt.isEmpty()) {
            // Chưa vote -> tạo mới

            voteRepository.save(vote);

            if (newVoteType == VoteTypes.UPVOTE) {
                post.setCountLike(post.getCountLike() + 1);
            } else {
                post.setCountDislike(post.getCountDislike() + 1);
            }

            postRepository.save(post);
            return "Vote created";
        } else {
            // Đã vote rồi
            Votes existingVote = existingVoteOpt.get();
            VoteTypes currentVoteType = existingVote.getVoteType();

            if (currentVoteType == newVoteType) {
                // Cùng loại -> xóa vote
                voteRepository.delete(existingVote);
                if (currentVoteType == VoteTypes.UPVOTE) {
                    post.setCountLike(post.getCountLike() - 1);
                } else {
                    post.setCountDislike(post.getCountDislike() - 1);
                }
                postRepository.save(post);
                return "Vote removed";
            } else {
                // Khác loại -> cập nhật vote
                existingVote.setVoteType(newVoteType);
                voteRepository.save(existingVote);

                if (newVoteType == VoteTypes.UPVOTE) {
                    post.setCountLike(post.getCountLike() + 1);
                    post.setCountDislike(post.getCountDislike() - 1);
                } else {
                    post.setCountDislike(post.getCountDislike() + 1);
                    post.setCountLike(post.getCountLike() - 1);
                }

                postRepository.save(post);
                return "Vote updated";
            }
        }
    }
}
