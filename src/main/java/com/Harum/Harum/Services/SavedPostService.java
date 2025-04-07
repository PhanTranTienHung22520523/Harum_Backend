package com.Harum.Harum.Services;

import com.Harum.Harum.Models.SavedPosts;
import com.Harum.Harum.Repository.PostRepo;
import com.Harum.Harum.Repository.SavedPostRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SavedPostService {

    @Autowired
    private SavedPostRepo savedPostRepo;

    @Autowired
    private PostRepo postRepo;

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
}
