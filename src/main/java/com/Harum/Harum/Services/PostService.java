package com.Harum.Harum.Services;

import com.Harum.Harum.Models.Posts;
import com.Harum.Harum.Repository.PostRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepo postRepository;

    // 1. Create - Tạo mới bài post
    public Posts createPost(Posts post) {
        return postRepository.save(post);
    }

    // 2. Read - Lấy tất cả bài post
    public List<Posts> getAllPosts() {
        return postRepository.findAll();
    }

    // 3. Read - Lấy bài post theo ID
    public Optional<Posts> getPostById(String id) {
        return postRepository.findById(id);
    }

    // 4. Update - Cập nhật bài post theo ID
    public Posts updatePost(String id, Posts updatedPost) {
        // Kiểm tra xem bài post có tồn tại không
        if (postRepository.existsById(id)) {
            updatedPost.setId(id); // Đảm bảo id không thay đổi
            return postRepository.save(updatedPost);
        }
        return null; // Trả về null nếu không tìm thấy post với id đó
    }

    // 5. Delete - Xóa bài post theo ID
    public boolean deletePost(String id) {
        if (postRepository.existsById(id)) {
            postRepository.deleteById(id);
            return true;
        }
        return false; // Trả về false nếu không tìm thấy post
    }
    // 6. Read - Lấy danh sách bài post theo topicId
    public List<Posts> getPostsByTopic(String topicId) {
        return postRepository.findByTopicId(topicId);
    }
    // 7. Read - Lấy danh sách bài post theo userId
    public List<Posts> getPostsByUser(String userId) {
        return postRepository.findByUserId(userId);
    }
    // 8. Read - Lấy dánh sách bài post phổ biến ( tính theo luơợt xem )

    // LUU Y: LAM PHAN TRANG
    public List<Posts> getPopularPosts() {
        return postRepository.findAll(Sort.by(Sort.Order.desc("countView"))); // Sắp xếp theo countView giảm dần
    }
    // 9. Read - Lấy danh sách bài viết nổi bật ( tiính theo upvote)
    public List<Posts> getTopPosts() {
        return postRepository.findAll(Sort.by(Sort.Order.desc("countLike"))); // Sắp xếp theo countLike giảm dần
    }

}
