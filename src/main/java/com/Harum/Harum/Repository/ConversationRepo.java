package com.Harum.Harum.Repository;

import com.Harum.Harum.Models.Conversations;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ConversationRepo extends MongoRepository<Conversations, String>{
    Optional<Conversations> findByUser1IdAndUser2Id(String user1Id, String user2Id);
    Optional<Conversations> findByUser2IdAndUser1Id(String user2Id, String user1Id);
    List<Conversations> findByUser1IdOrUser2Id(String user1Id, String user2Id);
}
