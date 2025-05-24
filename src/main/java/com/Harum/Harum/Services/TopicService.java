package com.Harum.Harum.Services;

import com.Harum.Harum.Models.Topics;
import com.Harum.Harum.Repository.TopicRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class TopicService {

    @Autowired
    private TopicRepo topicRepo;

    // CREATE
    public Topics createTopic(Topics topic) {
        return topicRepo.save(topic);
    }

    // READ ALL
    public List<Topics> getAllTopics() {
        return topicRepo.findAll();
    }

    // READ BY ID
    public Optional<Topics> getTopicById(String id) {
        return topicRepo.findById(id);
    }

    // UPDATE
    public Topics updateTopic(String id, Topics updatedTopic) {
        Optional<Topics> optionalTopic = topicRepo.findById(id);
        if (optionalTopic.isPresent()) {
            Topics topic = optionalTopic.get();
            topic.setName(updatedTopic.getName());

            return topicRepo.save(topic);
        } else {
            return null;
        }
    }

    // DELETE
    public boolean deleteTopic(String id) {
        if (topicRepo.existsById(id)) {
            topicRepo.deleteById(id);
            return true;
        }
        return false;
    }

}
