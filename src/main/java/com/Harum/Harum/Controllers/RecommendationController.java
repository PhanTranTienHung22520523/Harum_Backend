package com.Harum.Harum.Controllers;// package com.Harum.Harum.Controllers;

import com.Harum.Harum.DTO.PostRecommendation;
import com.Harum.Harum.DTO.PostResponseDTO; // <<< THÊM IMPORT NÀY
import com.Harum.Harum.Services.PostService; // <<< THÊM IMPORT NÀY
import com.Harum.Harum.Services.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page; // <<< THÊM IMPORT NÀY
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest; // <<< THÊM IMPORT NÀY
import org.springframework.data.domain.Pageable; // <<< THÊM IMPORT NÀY
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*; // <<< THÊM IMPORT NÀY

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/recommend")
public class RecommendationController {

    private final RecommendationService recommendationService;
    private final PostService postService; // <<< TIÊM POSTSERVICE VÀO

    @Autowired
    public RecommendationController(RecommendationService recommendationService, PostService postService) {
        this.recommendationService = recommendationService;
        this.postService = postService; // <<< KHỞI TẠO POSTSERVICE
    }

    /**
     * Lấy danh sách bài viết gợi ý cho người dùng, đã được làm giàu thông tin và có phân trang.
     * @param userId ID của người dùng
     * @param page Trang hiện tại (mặc định là 0)
     * @param size Số lượng phần tử mỗi trang (mặc định là 10)
     * @return Một trang (Page) chứa các bài viết gợi ý với đầy đủ thông tin.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<Page<PostResponseDTO>> getUserRecommendations(
            @PathVariable String userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        // --- BƯỚC A: Lấy danh sách gợi ý cơ bản (ID và Title) từ FastAPI ---
        List<PostRecommendation> basicRecommendations = recommendationService.getRecommendations(userId);

        // Nếu không có gợi ý nào từ model, trả về 204 No Content
        if (basicRecommendations == null || basicRecommendations.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        // --- BƯỚC B: Trích xuất danh sách ID từ kết quả gợi ý ---
        // Thứ tự của danh sách ID này rất quan trọng
        List<String> recommendedPostIds = basicRecommendations.stream()
                .map(PostRecommendation::getId)
                .collect(Collectors.toList());

        // --- BƯỚC C: Tạo đối tượng Pageable cho việc phân trang ---
        Pageable pageable = PageRequest.of(page-1, size);

        // --- BƯỚC D: Gọi PostService để lấy thông tin đầy đủ VÀ phân trang ---
        // Phương thức này sẽ xử lý việc chỉ lấy dữ liệu cho trang hiện tại
        Page<PostResponseDTO> fullPostPage = postService.getPostsByIdsWithPaging(recommendedPostIds, pageable);

        // Trả về kết quả cuối cùng
        return ResponseEntity.ok(fullPostPage);
    }



    // --- API MỚI: Gợi ý theo Topic ---
    /**
     * Lấy danh sách bài viết gợi ý cho người dùng, được lọc theo một chủ đề cụ thể và có phân trang.
     * @param userId ID của người dùng
     * @param topicId ID của chủ đề cần lọc
     * @param page Trang hiện tại
     * @param size Số lượng phần tử mỗi trang
     * @return Một trang (Page) chứa các bài viết gợi ý thuộc chủ đề đó.
     */
    @GetMapping("/{userId}/by-topic/{topicId}")
    public ResponseEntity<Page<PostResponseDTO>> getRecommendationsByTopic(
            @PathVariable String userId,
            @PathVariable String topicId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        // --- BƯỚC A: Lấy TOÀN BỘ danh sách gợi ý gốc từ FastAPI ---
        List<PostRecommendation> allRecommendations = recommendationService.getRecommendations(userId);

        if (allRecommendations == null || allRecommendations.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        // --- BƯỚC B: Lấy thông tin đầy đủ của TẤT CẢ các bài viết gợi ý ---
        // Lấy list ID
        List<String> allRecommendedIds = allRecommendations.stream()
                .map(PostRecommendation::getId)
                .collect(Collectors.toList());

        // Gọi PostService để lấy thông tin chi tiết (chưa phân trang)
        List<PostResponseDTO> fullRecommendedPosts = postService.getPostsByIds(allRecommendedIds);


        // --- BƯỚC C: Lọc danh sách bài viết theo topicId mong muốn ---
        List<PostResponseDTO> filteredByTopicPosts = fullRecommendedPosts.stream()
                .filter(post -> topicId.equals(post.getTopicId()))
                .collect(Collectors.toList());

        if (filteredByTopicPosts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        // --- BƯỚC D: Thực hiện phân trang TRÊN BỘ DỮ LIỆU ĐÃ LỌC ---
        Pageable pageable = PageRequest.of(page-1, size);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filteredByTopicPosts.size());

        // Lấy sublist cho trang hiện tại
        List<PostResponseDTO> pageContent = (start <= end) ? filteredByTopicPosts.subList(start, end) : List.of();

        // Tạo đối tượng Page để trả về
        Page<PostResponseDTO> postPage = new PageImpl<>(pageContent, pageable, filteredByTopicPosts.size());

        return ResponseEntity.ok(postPage);
    }
}