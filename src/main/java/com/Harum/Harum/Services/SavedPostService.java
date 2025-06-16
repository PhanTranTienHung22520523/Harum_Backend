package com.Harum.Harum.Services;


import com.Harum.Harum.Models.Posts;
import com.Harum.Harum.Models.SavedPosts;
import com.Harum.Harum.Models.Users;
import com.Harum.Harum.Repository.PostRepo;
import com.Harum.Harum.Repository.SavedPostRepo;
import com.Harum.Harum.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.Harum.Harum.DTO.SavedPostResponseDTO;

import java.util.List;
import java.util.Optional;

@Service
public class SavedPostService {

    @Autowired
    private SavedPostRepo savedPostRepo;

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private UserRepo userRepo;

    // Hàm tương tác với bài viết: nếu đã lưu thì xoá, chưa thì thêm mới
    public String interactPost(SavedPosts savedPosts) {
        String userId = savedPosts.getUserId();
        String postId = savedPosts.getPostId();

        Optional<SavedPosts> existingSaved = savedPostRepo.findByUserIdAndPostId(userId, postId);

        if (existingSaved.isPresent()) {
            // Đã lưu rồi, thì xoá đi
            savedPostRepo.delete(existingSaved.get());
            return "Post removed from saved list";
        } else {
            // Chưa lưu -> thêm mới
            savedPostRepo.save(savedPosts);
            return "Post saved successfully";
        }
    }

    public List<SavedPosts> getSavedPostByUser(String userId) {
        return savedPostRepo.findByUserId(userId);
    }

    public boolean isPostSaved(String userId, String postId) {
        return savedPostRepo.findByUserIdAndPostId(userId, postId).isPresent();
    }

    public List<SavedPostResponseDTO> getSavedPostByUserDTO(String userId) {
        List<SavedPosts> savedPosts = savedPostRepo.findByUserId(userId);

        Optional<Users> userOpt = userRepo.findById(userId);
        if (userOpt.isEmpty()) return List.of(); // hoặc throw exception

        Users user = userOpt.get();

        return savedPosts.stream().map(sp -> {
            Optional<Posts> postOpt = postRepo.findById(sp.getPostId());
            return postOpt.map(post -> new SavedPostResponseDTO(
                    sp.getId(),
                    sp.getCreatedAt(),
                    user,
                    post
            )).orElse(null);
        }).filter(dto -> dto != null).toList();
    }


    public List<Posts> getSavedPostsDetailByUser(String userId) {
        List<SavedPosts> savedPosts = savedPostRepo.findByUserId(userId);
        List<String> postIds = savedPosts.stream()
                .map(SavedPosts::getPostId)
                .toList();
        return postRepo.findAllById(postIds);
    }
}
