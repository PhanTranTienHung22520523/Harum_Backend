package com.Harum.Harum.Controllers;

import com.Harum.Harum.DTO.PostResponseDTO;
import com.Harum.Harum.Models.Users;
import com.Harum.Harum.Services.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")

public class SearchController {

    @Autowired
    private SearchService searchService;

    // Tìm kiếm người dùng
    @GetMapping("/users")
    public ResponseEntity<?> searchUsers(
            @RequestParam("keyword") String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Users> users = searchService.searchUsers(keyword, page, size);
        return ResponseEntity.ok(users);
    }

    // Tìm kiếm bài viết
    @GetMapping("/posts")
    public ResponseEntity<?> searchPosts(
            @RequestParam("keyword") String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<PostResponseDTO> posts = searchService.searchPosts(keyword, page, size);
        return ResponseEntity.ok(posts);
    }

    // Nếu muốn gom cả users + posts vào 1 kết quả duy nhất
    @GetMapping("/all")
    public ResponseEntity<?> searchAll(
            @RequestParam("keyword") String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Users> users = searchService.searchUsers(keyword, page, size);
        Page<PostResponseDTO> posts = searchService.searchPosts(keyword, page, size);

        return ResponseEntity.ok(new SearchResultResponse(users, posts));
    }

    // Inner class response cho /all
    private static class SearchResultResponse {
        public final Page<Users> users;
        public final Page<PostResponseDTO> posts;

        public SearchResultResponse(Page<Users> users, Page<PostResponseDTO> posts) {
            this.users = users;
            this.posts = posts;
        }
    }
}
