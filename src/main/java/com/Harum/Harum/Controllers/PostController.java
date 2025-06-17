package com.Harum.Harum.Controllers;

import com.Harum.Harum.DTO.PostDetailsDTO;
import com.Harum.Harum.Enums.PostStatus;
import com.Harum.Harum.Enums.ReportStatus;
import com.Harum.Harum.DTO.PostResponseDTO;
import com.Harum.Harum.Models.PostBlock;
import com.Harum.Harum.Models.Posts;
import com.Harum.Harum.Models.Topics;
import com.Harum.Harum.Models.Users;
import com.Harum.Harum.Repository.PostRepo;
import com.Harum.Harum.Repository.TopicRepo;
import com.Harum.Harum.Repository.UserRepo;
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
import org.springframework.web.server.ResponseStatusException;

import java.text.Normalizer;
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

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private TopicRepo topicRepo;

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
    public ResponseEntity<PostDetailsDTO> getPostById(@PathVariable String id) {
        Posts post = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy bài viết"));

        Users user = userRepo.findById(post.getUserId()).orElse(null);
        Topics topic = topicRepo.findById(post.getTopicId()).orElse(null);

        PostDetailsDTO dto = new PostDetailsDTO(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getImageUrl(),
                post.getTopicId(),
                topic != null ? topic.getName() : null,
                post.getUserId(),
                user != null ? user.getUsername() : null,
                user != null ? user.getAvatarUrl() : null,
                post.getContentBlock(),
                post.getCountLike(),
                post.getCountDislike(),
                post.getCountView(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                post.getStatus(),
                post.getReportStatus());

        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}/toggle-status")
    public ResponseEntity<PostResponseDTO> togglePostStatus(@PathVariable String id) {
        PostResponseDTO updatedPostDto = postService.togglePostStatus(id);
        return ResponseEntity.ok(updatedPostDto);
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
    public ResponseEntity<?> getPostsByTopic(
            @PathVariable String topicId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            // Page trong Spring bắt đầu từ 0
            Page<PostResponseDTO> posts = postService.getPostsByTopic(topicId, page - 1, size);
            return ResponseEntity.ok(posts);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Tham số không hợp lệ: " + e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy topic với ID: " + topicId);
        } catch (Exception e) {
            // Ghi log nếu cần thiết
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Đã xảy ra lỗi khi lấy danh sách bài viết.");
        }
    }

    @GetMapping("/hot-topic/{topicId}")
    public ResponseEntity<?> getTopPostsByTopic(
            @PathVariable String topicId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            // Page trong Spring bắt đầu từ 0
            Page<PostResponseDTO> posts = postService.getPostsByTopic(topicId, page - 1, size);
            return ResponseEntity.ok(posts);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Tham số không hợp lệ: " + e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy topic với ID: " + topicId);
        } catch (Exception e) {
            // Ghi log nếu cần thiết
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Đã xảy ra lỗi khi lấy danh sách bài viết.");
        }
    }

    // 7. Read - Lấy danh sách bài post theo userId với phân trang
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getPostsByUser(@PathVariable String userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<PostResponseDTO> posts = postService.getPostsByUser(userId, page - 1, size);
            if (posts.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 8. Read - Lấy danh sách bài post phổ biến (tính theo lượt xem) với phân trang
    @GetMapping("/popular")
    public ResponseEntity<?> getPopularPosts(@RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<PostResponseDTO> posts = postService.getPopularPosts(page - 1, size);
            if (posts.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 9. Read - Lấy danh sách bài viết nổi bật (tính theo lượt upvote) với phân
    // trang
    @GetMapping("/top")
    public ResponseEntity<?> getTopPosts(@RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<PostResponseDTO> posts = postService.getTopPosts(page - 1, size);
            if (posts.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Xử lý ngoại lệ toàn cục trong Controller
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        // Ghi log nếu cần thiết
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Đã xảy ra lỗi trong hệ thống: " + e.getMessage());
    }

    // 10. Tao post co anh
    @PostMapping(value = "/with-blocks", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Posts> createPostWithBlocks(
            @RequestParam("title") String title,
            @RequestParam("userId") String userId,
            @RequestParam("topicId") String topicId,
            @RequestParam("blocks") String blocksJson,
            @RequestPart(value = "images", required = false) MultipartFile[] images,
            @RequestPart(value = "coverImage", required = false) MultipartFile coverImage) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<PostBlock> contentBlocks = objectMapper.readValue(blocksJson, new TypeReference<>() {
            });

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

            // chuan hoa title sau do luu vao DB
            post.setNormalizedTitle(normalizeText((title)));

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
            @RequestPart(value = "coverImage", required = false) MultipartFile coverImage) {
        try {
            Posts existing = postRepository.findById(postId)
                    .orElseThrow(() -> new RuntimeException("Post not found"));

            ObjectMapper objectMapper = new ObjectMapper();
            List<PostBlock> contentBlocks = objectMapper.readValue(blocksJson, new TypeReference<>() {
            });

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
            @RequestParam("status") ReportStatus status) {
        Posts post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        post.setReportStatus(status);
        post.setUpdatedAt(new Date());
        Posts updated = postService.updatePost(postId, post);
        return ResponseEntity.ok(updated);
    }

    public String getDefaultCoverUrlByTopic(String topicId) {
        switch (topicId) {
            case "67f3596980e7a31c46a4e33c":
                return "https://res.cloudinary.com/dgwokfdvm/image/upload/v1747487233/xahoi_gbpxym.png";
            case "67f3594480e7a31c46a4e33b":
                return "https://res.cloudinary.com/dgwokfdvm/image/upload/v1747487232/tinhyeu_exwljq.jpg";
            case "67f3587d80e7a31c46a4e336":
                return "https://res.cloudinary.com/dgwokfdvm/image/upload/v1747487232/tranhluan_qs3lav.jpg";
            case "67f3584980e7a31c46a4e334":
                return "https://res.cloudinary.com/dgwokfdvm/image/upload/v1747487231/tamly_hgnz30.jpg";
            case "67f3585d80e7a31c46a4e335":
                return "https://res.cloudinary.com/dgwokfdvm/image/upload/v1747487231/giaoduc_lnfas3.png";
            case "67f3589080e7a31c46a4e337":
                return "https://res.cloudinary.com/dgwokfdvm/image/upload/v1747487231/khoahoc_ifeddd.jpg";
            case "67f3591980e7a31c46a4e339":
                return "https://res.cloudinary.com/dgwokfdvm/image/upload/v1747487231/nghethuat_cmdh6t.jpg";
            case "67f357e280e7a31c46a4e333":
                return "https://res.cloudinary.com/dgwokfdvm/image/upload/v1747487232/thethao_qpanfo.jpg";
            case "67f358a780e7a31c46a4e338":
                return "https://res.cloudinary.com/dgwokfdvm/image/upload/v1747487232/lichsu_kzccug.jpg";
            case "67f3593780e7a31c46a4e33a":
                return "https://res.cloudinary.com/dgwokfdvm/image/upload/v1747487232/sach_iqzymp.jpg";
            default:
                return "https://res.cloudinary.com/dgwokfdvm/image/upload/v1747323953/leuxd3jchdvo8abhswfx.png";
        }
    }

    public String normalizeText(String input) {
        if (input == null)
            return "";

        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        normalized = normalized.replaceAll("đ", "d").replaceAll("Đ", "D");

        return normalized.toLowerCase().replaceAll("[^a-z0-9 ]", "").trim();
    }
}
