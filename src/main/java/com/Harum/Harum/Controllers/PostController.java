package com.Harum.Harum.Controllers;

import com.Harum.Harum.Models.PostBlock;
import com.Harum.Harum.Models.Posts;
import com.Harum.Harum.Models.Topics;
import com.Harum.Harum.Services.CloudinaryService;
import com.Harum.Harum.Services.PostService;
import com.Harum.Harum.Services.TopicService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private TopicService topicService;

    // 1. Create - Tạo mới bài post (chưa xử lý ảnh)
    @PostMapping
    public ResponseEntity<Posts> createPost(@RequestBody Posts post) {

        if (post.getCreatedAt() == null) {
            post.setCreatedAt();
        }
        if (post.getUpdatedAt() == null) {
            post.setUpdatedAt(new Date());
        }
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
    //10. Tao post co anh
    @PostMapping(value = "/with-blocks", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Posts> createPostWithBlocks(
            @RequestParam("title") String title,
            @RequestParam("userId") String userId,
            @RequestParam("topicId") String topicId,
            @RequestParam("blocks") String blocksJson,
            @RequestPart(value = "images", required = false) MultipartFile[] images
    ) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<PostBlock> contentBlocks = objectMapper.readValue(blocksJson, new TypeReference<>() {});

            int imageIndex = 0;
            for (PostBlock block : contentBlocks) {
                if ("image".equalsIgnoreCase(block.getType()) && images != null && imageIndex < images.length) {
                    String url = cloudinaryService.uploadFile(images[imageIndex]);
                    block.setValue(url);
                    imageIndex++;
                }
            }

            Posts post = new Posts();
            post.setTitle(title);
            post.setUserId(userId);
            post.setTopicId(topicId);
            post.setContentBlock(contentBlocks);
            post.setCreatedAt();
            post.setUpdatedAt(new Date());

            Posts saved = postService.createPost(post);
            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }


    // 11. Cập nhật bài viết với content block (ảnh + văn bản hỗn hợp)
    @PutMapping("/with-blocks/{id}")
    public ResponseEntity<Posts> updatePostWithBlocks(
            @PathVariable String id,
            @RequestParam("title") String title,
            @RequestParam("topicId") String topicId,
            @RequestParam("blocks") String blocksJson,
            @RequestPart(value = "images", required = false) MultipartFile[] images
    ) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<PostBlock> contentBlocks = objectMapper.readValue(blocksJson, new TypeReference<>() {});

            Map<String, MultipartFile> imageMap = new HashMap<>();
            if (images != null) {
                for (MultipartFile image : images) {
                    imageMap.put(image.getOriginalFilename(), image);
                }
            }

            for (PostBlock block : contentBlocks) {
                if ("image".equalsIgnoreCase(block.getType())) {
                    String key = block.getValue();
                    if (imageMap.containsKey(key)) {
                        String url = cloudinaryService.uploadFile(imageMap.get(key));
                        block.setValue(url);
                    }
                    // Nếu không có ảnh mới thì giữ nguyên đường dẫn cũ
                }
            }

            Optional<Posts> existingPostOpt = postService.getPostById(id);
            if (existingPostOpt.isEmpty()) return ResponseEntity.notFound().build();

            Posts post = existingPostOpt.get();
            post.setTitle(title);
            post.setContentBlock(contentBlocks);
            post.setUpdatedAt(new Date());
            post.setTopicId(topicId);

            Posts updated = postService.updatePost(id, post);
            return ResponseEntity.ok(updated);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
