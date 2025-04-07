package com.Harum.Harum.Services;

import com.Harum.Harum.Models.Posts;
import com.Harum.Harum.Repository.PostRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepo postRepository;

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
    public Optional<Posts> getPostById(String id) {
        return postRepository.findById(id);
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
    public Page<Posts> getPostsByTopic(String topicId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return postRepository.findByTopicId(topicId, pageable);
    }

    // 7. Read - Lấy danh sách bài post theo userId với phân trang
    public Page<Posts> getPostsByUser(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return postRepository.findByUserId(userId, pageable);
    }

    // 8. Read - Lấy dánh sách bài post phổ biến (tính theo lượt xem) với phân trang
    public Page<Posts> getPopularPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("countView"))); // Sắp xếp theo countView giảm dần
        return postRepository.findAll(pageable);
    }

    // 9. Read - Lấy danh sách bài viết nổi bật (tính theo upvote) với phân trang
    public Page<Posts> getTopPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("countLike"))); // Sắp xếp theo countLike giảm dần
        return postRepository.findAll(pageable);
    }
}
