package com.Harum.Harum.Controllers;

import com.Harum.Harum.Models.Posts;
import com.Harum.Harum.Services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    // 1. Create - Tạo mới bài post (chưa xử lý ảnh)
    @PostMapping
    public ResponseEntity<Posts> createPost(@RequestBody Posts post) {
        Posts createdPost = postService.createPost(post);
        return ResponseEntity.ok(createdPost);
    }

    // 2. Read - Lấy tất cả bài post với phân trang
    @GetMapping
    public ResponseEntity<Page<Posts>> getAllPosts(@RequestParam(defaultValue = "1") int page,
                                                   @RequestParam(defaultValue = "10") int size) {
        Page<Posts> posts = postService.getAllPosts(page, size);
        return ResponseEntity.ok(posts);
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

    // 6. Read - Lấy danh sách bài post theo topicId với phân trang
    @GetMapping("/topic/{topicId}")
    public ResponseEntity<Page<Posts>> getPostsByTopic(@PathVariable String topicId,
                                                       @RequestParam(defaultValue = "1") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        Page<Posts> posts = postService.getPostsByTopic(topicId, page, size);
        return ResponseEntity.ok(posts);
    }

    // 7. Read - Lấy danh sách bài post theo userId với phân trang
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<Posts>> getPostsByUser(@PathVariable String userId,
                                                      @RequestParam(defaultValue = "1") int page,
                                                      @RequestParam(defaultValue = "10") int size) {
        Page<Posts> posts = postService.getPostsByUser(userId, page, size);
        return ResponseEntity.ok(posts);
    }

    // 8. Read - Lấy danh sách bài post phổ biến (tính theo lượt xem) với phân trang
    @GetMapping("/popular")
    public ResponseEntity<Page<Posts>> getPopularPosts(@RequestParam(defaultValue = "1") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        try {
            Page<Posts> posts = postService.getPopularPosts(page, size);
            if (posts.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(posts, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 9. Read - Lấy danh sách bài viết nổi bật (tính theo lượt upvote) với phân trang
    @GetMapping("/top")
    public ResponseEntity<Page<Posts>> getTopPosts(@RequestParam(defaultValue = "1") int page,
                                                   @RequestParam(defaultValue = "10") int size) {
        try {
            Page<Posts> posts = postService.getTopPosts(page, size);
            if (posts.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(posts, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
