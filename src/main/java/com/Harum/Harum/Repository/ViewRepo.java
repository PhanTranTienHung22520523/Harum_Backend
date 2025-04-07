package com.Harum.Harum.Repository;

import com.Harum.Harum.Models.Views;
import com.Harum.Harum.Models.Votes;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ViewRepo extends MongoRepository<Views, String> {
    // Tìm tất cả lượt xem cho một bài viết theo postId
    List<Views> findByPostId(String postId);
    Optional<Votes> findByUserIdAndPostId(String userId, String postId);
}
