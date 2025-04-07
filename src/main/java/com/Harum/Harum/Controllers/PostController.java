package com.Harum.Harum.Controllers;

import com.Harum.Harum.Models.Posts;
import com.Harum.Harum.Services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    // 1. Create - Tạo mới bài post  ( chưa xử lý ảnh )
    @PostMapping
    public ResponseEntity<Posts> createPost(@RequestBody Posts post) {
        Posts createdPost = postService.createPost(post);
        return ResponseEntity.ok(createdPost);
    }

    // 2. Read - Lấy tất cả bài post
    @GetMapping
    public ResponseEntity<List<Posts>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    // 3. Read - Lấy bài post theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Posts> getPostById(@PathVariable String id) {
        Optional<Posts> post = postService.getPostById(id);
        return post.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 4. Update - Cập nhật bài post theo ID
    @PutMapping("/{id}")
    public ResponseEntity<Posts> updatePost(@PathVariable String id, @RequestBody Posts updatedPost) {
        Posts result = postService.updatePost(id, updatedPost);
        if (result != null) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.notFound().build();
    }

    // 5. Delete - Xóa bài post theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable String id) {
        boolean deleted = postService.deletePost(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // 6. Read - Lấy danh sách bài post theo topicId
    @GetMapping("/topic/{topicId}")
    public ResponseEntity<List<Posts>> getPostsByTopic(@PathVariable String topicId) {
        return ResponseEntity.ok(postService.getPostsByTopic(topicId));
    }

    // 7. Read - Lấy danh sách bài post theo userId
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Posts>> getPostsByUser(@PathVariable String userId)
    {
        return ResponseEntity.ok(postService.getPostsByUser(userId));
    }
    // 8. Read - Lấy danh sách bài post phổ biến (tính theo lượt xem)
    @GetMapping("/popular")
    public ResponseEntity<List<Posts>> getPopularPosts() {
        try {
            List<Posts> posts = postService.getPopularPosts();
            if (posts.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(posts, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // 9. Read - Lấy danh sách bài viết nổi bật (tính theo lượt upvote)
    @GetMapping("/top")
    public ResponseEntity<List<Posts>> getTopPosts() {
        try {
            List<Posts> posts = postService.getTopPosts();
            if (posts.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(posts, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
