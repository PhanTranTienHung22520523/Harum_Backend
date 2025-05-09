package com.Harum.Harum.Controllers;

import com.Harum.Harum.Models.Comments;
import com.Harum.Harum.Services.CloudinaryService;
import com.Harum.Harum.Services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/comment")
public class CommentController {
    @Autowired
    private CommentService commentsService;

    @GetMapping
    public List<Comments> getAllComments() {
        return commentsService.getAllComments();
    }

    @GetMapping("/{id}")
    public Optional<Comments> getCommentById(@PathVariable String id) {
        return commentsService.getCommentById(id);
    }

    @GetMapping("/post/{postId}")
    public List<Comments> getCommentsByPostId(@PathVariable String postId) {
        return commentsService.getCommentsByPostId(postId);
    }

    @GetMapping("/user/{userId}")
    public List<Comments> getCommentsByUserId(@PathVariable String userId) {
        return commentsService.getCommentsByUserId(userId);
    }

    @PostMapping
    public Comments createComment(@RequestBody Comments comment) {
        return commentsService.createComment(comment);
    }

    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable String id) {
        commentsService.deleteComment(id);
    }

    @GetMapping("/count/post/{postId}")
    public long countCommentsByPostId(@PathVariable String postId) {
        return commentsService.countCommentsByPostId(postId);
    }

    @PostMapping("/{parentId}/reply")
    public ResponseEntity<Comments> addReplyComment(@PathVariable String parentId, @RequestBody Comments reply) {
        Comments updated = commentsService.addReplyComment(parentId, reply);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comments> updateComment(@PathVariable String id, @RequestBody Comments updatedComment) {
        Comments updated = commentsService.updateComment(id, updatedComment.getContent());
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

}
