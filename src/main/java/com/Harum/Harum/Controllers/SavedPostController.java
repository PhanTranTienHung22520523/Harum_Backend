package com.Harum.Harum.Controllers;

import com.Harum.Harum.Models.SavedPosts;
import com.Harum.Harum.Services.SavedPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/saved-posts")
public class SavedPostController {

    @Autowired
    private SavedPostService savedPostService;

    // 1. Tương tác lưu / bỏ lưu
    @PostMapping("/interact")
    public ResponseEntity<String> interactSavedPost(@RequestBody SavedPosts savedPosts) {
        String result = savedPostService.interactPost(savedPosts);
        return ResponseEntity.ok(result);
    }

    // 2. Lấy danh sách bài viết đã lưu theo userId
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SavedPosts>> getSavedPostsByUser(@PathVariable String userId) {
        List<SavedPosts> savedPosts = savedPostService.getSavedPostByUser(userId);
        return ResponseEntity.ok(savedPosts);
    }

    // 3. Kiểm tra xem bài viết đã được lưu bởi user chưa
    @GetMapping("/check/{userId}/{postId}")
    public ResponseEntity<Boolean> isPostSaved(
            @PathVariable String userId,
            @PathVariable String postId
    ) {
        boolean isSaved = savedPostService.isPostSaved(userId, postId);
        return ResponseEntity.ok(isSaved);
    }
}
