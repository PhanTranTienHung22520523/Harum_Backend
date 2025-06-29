package com.Harum.Harum.Controllers;// package com.Harum.Harum.Controllers;

import com.Harum.Harum.DTO.PostRecommendation;
import com.Harum.Harum.DTO.PostResponseDTO;
import com.Harum.Harum.Services.PostService;
import com.Harum.Harum.Services.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList; // <<< THÊM IMPORT
import java.util.Arrays;    // <<< THÊM IMPORT
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/recommend")
public class RecommendationController {

    private final RecommendationService recommendationService;
    private final PostService postService;

    @Autowired
    public RecommendationController(RecommendationService recommendationService, PostService postService) {
        this.recommendationService = recommendationService;
        this.postService = postService;
    }

    /**
     * Lấy danh sách bài viết gợi ý cho người dùng.
     * Nếu không có gợi ý, sẽ lấy các bài viết mặc định từ các chủ đề hot.
     * @param userId ID của người dùng
     * @param page Trang hiện tại (mặc định là 1)
     * @param size Số lượng phần tử mỗi trang (mặc định là 10)
     * @return Một trang (Page) chứa các bài viết gợi ý.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<Page<PostResponseDTO>> getUserRecommendations(
            @PathVariable String userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        // --- BƯỚC A: Lấy danh sách gợi ý từ model AI ---
        List<PostRecommendation> basicRecommendations = recommendationService.getRecommendations(userId);

        Pageable pageable = PageRequest.of(page - 1, size);

        // --- BƯỚC B: Kiểm tra kết quả và thực hiện logic tương ứng ---

        // --- TRƯỜNG HỢP 1: CÓ GỢI Ý TỪ MODEL ---
        if (basicRecommendations != null && !basicRecommendations.isEmpty()) {
            // Trích xuất danh sách ID
            List<String> recommendedPostIds = basicRecommendations.stream()
                    .map(PostRecommendation::getId)
                    .collect(Collectors.toList());

            // Gọi PostService để lấy thông tin đầy đủ VÀ phân trang
            Page<PostResponseDTO> fullPostPage = postService.getPostsByIdsWithPaging(recommendedPostIds, pageable);

            return ResponseEntity.ok(fullPostPage);

        }
        // --- TRƯỜNG HỢP 2: KHÔNG CÓ GỢI Ý (LOGIC FALLBACK) ---
        else {
            // Định nghĩa các topicId mặc định
            List<String> fallbackTopicIds = Arrays.asList(
                    "67f3584980e7a31c46a4e334",
                    "67f3594480e7a31c46a4e33b",
                    "67f3596980e7a31c46a4e33c"
            );

            // Tạo một danh sách để tổng hợp kết quả
            List<PostResponseDTO> fallbackPosts = new ArrayList<>();

            // Lặp qua từng topic và lấy 4 bài viết
            for (String topicId : fallbackTopicIds) {
                // Lấy 4 bài viết đầu tiên của topic này (page=0, size=4)
                Page<PostResponseDTO> postsFromTopic = postService.getPostsByTopic(topicId, 0, 4);
                if (postsFromTopic != null) {
                    fallbackPosts.addAll(postsFromTopic.getContent());
                }
            }
            Collections.shuffle(fallbackPosts);
            // Tạo một đối tượng Page từ danh sách đã tổng hợp
            // Lưu ý: Phân trang này được áp dụng trên kết quả fallback (tối đa 12 bài)
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), fallbackPosts.size());

            List<PostResponseDTO> pageContent = (start <= end) ? fallbackPosts.subList(start, end) : List.of();

            Page<PostResponseDTO> resultPage = new PageImpl<>(pageContent, pageable, fallbackPosts.size());

            return ResponseEntity.ok(resultPage);
        }
    }

    // --- Giữ nguyên API getRecommendationsByTopic ---
    @GetMapping("/{userId}/by-topic/{topicId}")
    public ResponseEntity<Page<PostResponseDTO>> getRecommendationsByTopic(
            @PathVariable String userId,
            @PathVariable String topicId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        // ... logic của phương thức này giữ nguyên như cũ ...
        List<PostRecommendation> allRecommendations = recommendationService.getRecommendations(userId);

        if (allRecommendations == null || allRecommendations.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<String> allRecommendedIds = allRecommendations.stream()
                .map(PostRecommendation::getId)
                .collect(Collectors.toList());

        List<PostResponseDTO> fullRecommendedPosts = postService.getPostsByIds(allRecommendedIds);

        List<PostResponseDTO> filteredByTopicPosts = fullRecommendedPosts.stream()
                .filter(post -> topicId.equals(post.getTopicId()))
                .collect(Collectors.toList());

        if (filteredByTopicPosts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        Pageable pageable = PageRequest.of(page - 1, size);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filteredByTopicPosts.size());

        List<PostResponseDTO> pageContent = (start <= end) ? filteredByTopicPosts.subList(start, end) : List.of();

        Page<PostResponseDTO> postPage = new PageImpl<>(pageContent, pageable, filteredByTopicPosts.size());

        return ResponseEntity.ok(postPage);
    }
}