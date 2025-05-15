package com.Harum.Harum.Services;

import com.Harum.Harum.DTO.PostResponseDTO;
import com.Harum.Harum.Models.Posts;
import com.Harum.Harum.Models.Users;
import com.Harum.Harum.Repository.PostRepo;
import com.Harum.Harum.Repository.TopicRepo;
import com.Harum.Harum.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.text.Normalizer;

@Service
public class SearchService {
    @Autowired
    private UserRepo userRepository;

    @Autowired
    private PostRepo postRepository;
    @Autowired
    private TopicRepo topicRepository;

    public Page<Users> searchUsers(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        // Chuẩn hoá keyword

        return userRepository.searchUsers(keyword, pageable);
    }

    public Page<PostResponseDTO> searchPosts(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        // Chuẩn hoá keyword
        String normalizedKeyword = removeDiacritics(keyword).toLowerCase();

        Page<Posts> posts = postRepository.searchPosts(normalizedKeyword , pageable);
        // Chuyển đổi User sang UserResponseDTO
        return convertToPostResponseDTO(posts);

    }
    private Page<PostResponseDTO> convertToPostResponseDTO(Page<Posts> postsPage) {
        return postsPage.map(post -> {
            PostResponseDTO dto = new PostResponseDTO();
            dto.setId(post.getId());
            dto.setTitle(post.getTitle());
            dto.setContent(post.getContent());
            dto.setImageUrl(post.getImageUrl());

            dto.setCreatedAt(post.getCreatedAt());
            dto.setUpdatedAt(post.getUpdatedAt());
            dto.setTopicId(post.getTopicId());
            dto.setUserId(post.getUserId());
            dto.setCountLike(post.getCountLike());
            dto.setCountDislike(post.getCountDislike());
            dto.setCountView(post.getCountView());
            dto.setContentBlock(post.getContentBlock());

            // Lấy topicName
            topicRepository.findById(post.getTopicId())
                    .ifPresent(topic -> dto.setTopicName(topic.getName()));

            // Lấy username
            userRepository.findById(post.getUserId())
                    .ifPresent(user -> {
                        dto.setUsername(user.getUsername());
                        dto.setUserImage(user.getAvatarUrl());
                    });

            return dto;
        });
    }
    public static String removeDiacritics(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
    public String normalizeText(String input) {
        if (input == null) return "";

        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        normalized = normalized.replaceAll("đ", "d").replaceAll("Đ", "D");

        return normalized.toLowerCase().replaceAll("[^a-z0-9 ]", "").trim();
    }
}
