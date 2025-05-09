package com.Harum.Harum.Repository;

import com.Harum.Harum.Models.Messages;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface MessageRepo extends MongoRepository<Messages, String> {
    List<Messages> findByConversationId(String conversationId);
}
