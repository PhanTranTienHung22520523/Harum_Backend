package com.Harum.Harum.Controllers;

import com.Harum.Harum.Enums.PostStatus;
import com.Harum.Harum.Enums.ReportStatus;
import com.Harum.Harum.Models.PostBlock;
import com.Harum.Harum.Models.Posts;
import com.Harum.Harum.Models.Topics;
import com.Harum.Harum.Repository.PostRepo;
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
    @Autowired
    private PostRepo postRepository;

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
            @RequestPart(value = "images", required = false) MultipartFile[] images,
            @RequestPart(value = "coverImage", required = false) MultipartFile coverImage
    ) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<PostBlock> contentBlocks = objectMapper.readValue(blocksJson, new TypeReference<>() {});

            // Upload ảnh cover nếu có, nếu không thì dùng mặc định theo topic
            String coverUrl;
            if (coverImage != null && !coverImage.isEmpty()) {
                coverUrl = cloudinaryService.uploadFile(coverImage);
            } else {
                coverUrl = getDefaultCoverUrlByTopic(topicId); // URL mặc định theo topic
            }

            // Upload ảnh trong content blocks
            int imageIndex = 0;
            if (images != null) {
                for (PostBlock block : contentBlocks) {
                    if ("image".equalsIgnoreCase(block.getType()) && imageIndex < images.length) {
                        String url = cloudinaryService.uploadFile(images[imageIndex]);
                        block.setValue(url);
                        imageIndex++;
                    }
                }
            }

            // Tạo post
            Posts post = new Posts();
            post.setTitle(title);
            post.setUserId(userId);
            post.setTopicId(topicId);
            post.setContentBlock(contentBlocks);
            post.setImageUrl(coverUrl); // Gán ảnh cover (upload hoặc mặc định)
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
            @PathVariable("id") String postId,
            @RequestParam("title") String title,
            @RequestParam("topicId") String topicId,
            @RequestParam("blocks") String blocksJson,
            @RequestPart(value = "images", required = false) MultipartFile[] images,
            @RequestPart(value = "coverImage", required = false) MultipartFile coverImage
    ) {
        try {
            Posts existing = postRepository.findById(postId)
                    .orElseThrow(() -> new RuntimeException("Post not found"));


            ObjectMapper objectMapper = new ObjectMapper();
            List<PostBlock> contentBlocks = objectMapper.readValue(blocksJson, new TypeReference<>() {});

            // Upload cover mới nếu có
            if (coverImage != null && !coverImage.isEmpty()) {
                String coverUrl = cloudinaryService.uploadFile(coverImage);
                existing.setImageUrl(coverUrl);
            }

            // Upload ảnh block mới nếu có
            int imageIndex = 0;
            if (images != null) {
                for (PostBlock block : contentBlocks) {
                    if ("image".equalsIgnoreCase(block.getType()) && imageIndex < images.length) {
                        String url = cloudinaryService.uploadFile(images[imageIndex]);
                        block.setValue(url);
                        imageIndex++;
                    }
                }
            }

            existing.setTitle(title);
            existing.setTopicId(topicId);
            existing.setContentBlock(contentBlocks);
            existing.setUpdatedAt(new Date());

            Posts updated = postService.updatePost(postId, existing);
            return ResponseEntity.ok(updated);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/admin/pending")
    public ResponseEntity<List<Posts>> getPendingPosts() {
        List<Posts> posts = postService.getPostsByStatus(ReportStatus.PENDING);
        return ResponseEntity.ok(posts);
    }


    @PutMapping("/admin/{id}/status")
    public ResponseEntity<Posts> updatePostStatus(
            @PathVariable("id") String postId,
            @RequestParam("status") ReportStatus status
    ) {
        Posts post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));


        post.setReportStatus(status);
        post.setUpdatedAt(new Date());
        Posts updated = postService.updatePost(postId,post);
        return ResponseEntity.ok(updated);
    }


    public String getDefaultCoverUrlByTopic(String topicId) {
        switch (topicId) {
            case "67f3596980e7a31c46a4e33c": return "https://res.cloudinary.com/dgwokfdvm/image/upload/v1747487233/xahoi_gbpxym.png";
            case "67f3594480e7a31c46a4e33b": return "https://res.cloudinary.com/dgwokfdvm/image/upload/v1747487232/tinhyeu_exwljq.jpg";
            case "67f3587d80e7a31c46a4e336": return "https://res.cloudinary.com/dgwokfdvm/image/upload/v1747487232/tranhluan_qs3lav.jpg";
            case "67f3584980e7a31c46a4e334": return "https://res.cloudinary.com/dgwokfdvm/image/upload/v1747487231/tamly_hgnz30.jpg";
            case "67f3585d80e7a31c46a4e335": return "https://res.cloudinary.com/dgwokfdvm/image/upload/v1747487231/giaoduc_lnfas3.png";
            case "67f3589080e7a31c46a4e337": return "https://res.cloudinary.com/dgwokfdvm/image/upload/v1747487231/khoahoc_ifeddd.jpg";
            case "67f3591980e7a31c46a4e339": return "https://res.cloudinary.com/dgwokfdvm/image/upload/v1747487231/nghethuat_cmdh6t.jpg";
            case "67f357e280e7a31c46a4e333": return "https://res.cloudinary.com/dgwokfdvm/image/upload/v1747487232/thethao_qpanfo.jpg";
            case "67f358a780e7a31c46a4e338": return "https://res.cloudinary.com/dgwokfdvm/image/upload/v1747487232/lichsu_kzccug.jpg";
            case "67f3593780e7a31c46a4e33a": return "https://res.cloudinary.com/dgwokfdvm/image/upload/v1747487232/sach_iqzymp.jpg";
            default: return "https://res.cloudinary.com/dgwokfdvm/image/upload/v1747323953/leuxd3jchdvo8abhswfx.png";
        }
    }

}
