package com.Harum.Harum.Controllers;

import com.Harum.Harum.DTO.CommentDetailsDTO;
import com.Harum.Harum.Enums.ReportStatus;
import com.Harum.Harum.Models.Comments;
import com.Harum.Harum.Models.Posts;
import com.Harum.Harum.Repository.CommentRepo;
import com.Harum.Harum.Services.CloudinaryService;
import com.Harum.Harum.Services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/comment")
public class CommentController {
    @Autowired
    private CommentService commentsService;

    @Autowired
    private CommentRepo commentRepository;

    @GetMapping
    public List<Comments> getAllComments() {
        return commentsService.getAllComments();
    }

    @GetMapping("/{id}")
    public Optional<CommentDetailsDTO> getCommentById(@PathVariable String id) {
        return commentsService.getCommentById(id);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentDetailsDTO>> getCommentsByPostId(@PathVariable String postId) {
        List<CommentDetailsDTO> comments = commentsService.getCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    @PutMapping("/{id}/toggle-status")
    public ResponseEntity<CommentDetailsDTO> toggleCommentStatus(@PathVariable String id) {
        CommentDetailsDTO updatedCommentDto = commentsService.toggleCommentStatus(id);
        return ResponseEntity.ok(updatedCommentDto);
    }

    @GetMapping("/user/{userId}")
    public List<CommentDetailsDTO> getCommentsByUserId(@PathVariable String userId) {
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

    @GetMapping("/admin/pending")
    public ResponseEntity<List<CommentDetailsDTO>> getPendingPosts() {
        List<CommentDetailsDTO> commentss = commentsService.getCommentssByStatus(ReportStatus.PENDING);
        return ResponseEntity.ok(commentss);
    }

    @PutMapping("/admin/{id}/status")
    public ResponseEntity<Comments> updatePostStatus(
            @PathVariable("id") String commentId,
            @RequestParam("status") ReportStatus status) {
        Comments comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        comment.setReportStatus(status);
        comment.setUpdatedAt(new Date());
        Comments updated = commentsService.updateCommentStatus(commentId, status);
        return ResponseEntity.ok(updated);
    }
}
