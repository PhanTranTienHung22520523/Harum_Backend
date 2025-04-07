package com.Harum.Harum.Controllers;


import com.Harum.Harum.Models.Topics;
import com.Harum.Harum.Services.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/topics")
public class TopicController {

    @Autowired
    private TopicService topicService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Topics topic) {
        if (topic.getName() == null || topic.getName().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Tên chủ đề không được để trống.");
        }
        return ResponseEntity.ok(topicService.createTopic(topic));
    }
    @GetMapping
    public List<Topics> getAll() {
        return topicService.getAllTopics();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Topics> getById(@PathVariable String id) {
        return topicService.getTopicById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Topics> update(@PathVariable String id, @RequestBody Topics topic) {
        Topics updated = topicService.updateTopic(id, topic);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        return topicService.deleteTopic(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }
}
