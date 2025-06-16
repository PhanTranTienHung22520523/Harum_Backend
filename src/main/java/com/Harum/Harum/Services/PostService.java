package com.Harum.Harum.Services;

import com.Harum.Harum.DTO.PostDetailsDTO;
import com.Harum.Harum.DTO.PostResponseDTO;
import com.Harum.Harum.Enums.PostStatus;
import com.Harum.Harum.Enums.ReportStatus;
import com.Harum.Harum.Models.Posts;
import com.Harum.Harum.Models.Topics;
import com.Harum.Harum.Models.Users;
import com.Harum.Harum.Repository.PostRepo;
import com.Harum.Harum.Repository.TopicRepo;
import com.Harum.Harum.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.Harum.Harum.Enums.PostStatus;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepo postRepository;
    @Autowired
    private TopicRepo topicRepository;
    @Autowired
    private UserRepo userRepository;

    // 1. Create - Tạo mới bài post
    public Posts createPost(Posts post) {
        return postRepository.save(post);
    }

    // 2. Read - Lấy tất cả bài post (với phân trang)
    public Page<Posts> getAllPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return postRepository.findAll(pageable);
    }

    // 3. Read - Lấy bài post theo ID

   // public Optional<Posts> getPostById(String id) {
    //    return postRepository.findById(id);
    //}
   public Optional<PostDetailsDTO> getPostDetailsById(String postId) {
       Optional<Posts> postOpt = postRepository.findById(postId);

       if (postOpt.isEmpty()) return Optional.empty();

       Posts post = postOpt.get();

       Optional<Users> userOpt = userRepository.findById(post.getUserId());
       Optional<Topics> topicOpt = topicRepository.findById(post.getTopicId());

       PostDetailsDTO dto = new PostDetailsDTO();
       dto.setId(post.getId());
       dto.setTitle(post.getTitle());
       dto.setContent(post.getContent());
       dto.setImageUrl(post.getImageUrl());
       dto.setTopicId(post.getTopicId());
       dto.setTopicName(topicOpt.map(Topics::getName).orElse(null));
       dto.setUserId(post.getUserId());
       dto.setUsername(userOpt.map(Users::getUsername).orElse(null));
       dto.setAvatarUrl(userOpt.map(Users::getAvatarUrl).orElse(null));
       dto.setContentBlock(post.getContentBlock());
       dto.setCountLike(post.getCountLike());
       dto.setCountDislike(post.getCountDislike());
       dto.setCountView(post.getCountView());
       dto.setCreatedAt(post.getCreatedAt());
       dto.setUpdatedAt(post.getUpdatedAt());
       dto.setStatus(post.getStatus());
       dto.setReportStatus(post.getReportStatus());

       return Optional.of(dto);
   }
    // 4. Update - Cập nhật bài post theo ID
    public Posts updatePost(String id, Posts updatedPost) {
        if (postRepository.existsById(id)) {
            updatedPost.setId(id);
            return postRepository.save(updatedPost);
        }
        return null;
    }

    // 5. Delete - Xóa bài post theo ID
    public boolean deletePost(String id) {
        if (postRepository.existsById(id)) {
            postRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // 6. Read - Lấy danh sách bài post theo topicId với phân trang
    public Page<PostResponseDTO> getPostsByTopic(String topicId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Posts> postsPage = postRepository.findByTopicId(topicId, pageable);
        return convertToPostResponseDTO(postsPage);
    }
    // 6.1 Read - Lấy danh sách bài post noi bat  theo topicId với phân trang
    public Page<PostResponseDTO> getTopPostsByTopic(String topicId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("countView")));
        Page<Posts> postsPage = postRepository.findByTopicId(topicId, pageable);
        return convertToPostResponseDTO(postsPage);
    }

    // 7. Read - Lấy danh sách bài post theo userId với phân trang
    public Page<PostResponseDTO> getPostsByUser(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Posts> postsPage = postRepository.findByUserId(userId, pageable);
        return convertToPostResponseDTO(postsPage);
    }

    // 8. Read - Lấy danh sách bài post phổ biến (tính theo lượt xem)
    public Page<PostResponseDTO> getPopularPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("countView")));
        Page<Posts> postsPage = postRepository.findAll(pageable);
        return convertToPostResponseDTO(postsPage);
    }

    // 9. Read - Lấy danh sách bài viết nổi bật (tính theo lượt thích)
    public Page<PostResponseDTO> getTopPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("countLike")));
        Page<Posts> postsPage = postRepository.findAll(pageable);
        return convertToPostResponseDTO(postsPage);
    }

    // Hàm dùng chung để map Posts → PostResponseDTO
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


    public List<Posts> getPostsByStatus(ReportStatus status) {
        return postRepository.findByStatus(status);
    }
}
