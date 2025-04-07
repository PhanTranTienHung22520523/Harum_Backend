package com.Harum.Harum.Repository;

import com.Harum.Harum.Models.Topics;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepo  extends MongoRepository<Topics, String> {
}
