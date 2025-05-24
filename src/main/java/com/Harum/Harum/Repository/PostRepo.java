package com.Harum.Harum.Repository;
import com.Harum.Harum.Enums.PostStatus;
import com.Harum.Harum.Enums.ReportStatus;
import com.Harum.Harum.Models.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.Harum.Harum.Models.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepo extends MongoRepository<Posts, String> {
    Page<Posts> findByTopicId(String topicId, Pageable pageable);  // Lấy bài post theo topicId với phân trang
    Page<Posts> findByUserId(String userId, Pageable pageable);    // Lấy bài post theo userId với phân trang
    List<Posts> findByStatus(ReportStatus status);

    @Query("{ $text: { $search: ?0 } }")
    Page<Posts> searchPosts(String keyword, Pageable pageable);

}
