package com.Harum.Harum.Repository;

import com.Harum.Harum.Models.Posts;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepo extends MongoRepository<Posts, String> {
    List<Posts> findByTopicId(String topicId); //
    List<Posts> findByUserId(String userId);
}
