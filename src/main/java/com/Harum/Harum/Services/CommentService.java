package com.Harum.Harum.Services;

import com.Harum.Harum.Enums.NotificationTypes;
import com.Harum.Harum.Models.Comments;
import com.Harum.Harum.Models.Notifications;
import com.Harum.Harum.Models.Posts;
import com.Harum.Harum.Repository.CommentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    private CommentRepo commentsRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private PostService postService;

    public List<Comments> getAllComments() {
        return commentsRepository.findAll();
    }

    public Optional<Comments> getCommentById(String id) {
        return commentsRepository.findById(id);
    }

    public List<Comments> getCommentsByPostId(String postId) {
        return commentsRepository.findByPostId(postId);
    }

    public List<Comments> getCommentsByUserId(String userId) {
        return commentsRepository.findByUserId(userId);
    }

    public Comments createComment(Comments comment) {
        Comments saved = commentsRepository.save(comment);

        // Sau khi lưu comment, gửi notification cho chủ bài post
        Optional<Posts> postOpt = postService.getPostById(comment.getPostId());
        if (postOpt.isPresent()) {
            Posts post = postOpt.get();
            String ownerId = post.getUserId();
            if (!ownerId.equals(comment.getUserId())) { // Không gửi noti nếu tự cmt bài mình
                Notifications noti = new Notifications(
                        ownerId,
                        "Người dùng đã bình luận vào bài viết của bạn.",
                        NotificationTypes.COMMENT, // Enum COMMENT
                        comment.getPostId(),
                        saved.getId(),
                        null
                );
                notificationService.createNotification(noti);
            }
        }

        return saved;
    }

    public void deleteComment(String id) {
        commentsRepository.deleteById(id);
    }
    public long countCommentsByPostId(String postId) {
        return commentsRepository.countByPostId(postId);
    }
    public Comments addReplyComment(String parentCommentId, Comments reply) {
        Optional<Comments> parentOpt = commentsRepository.findById(parentCommentId);
        if (parentOpt.isPresent()) {
            Comments parent = parentOpt.get();
            reply.setParentId(parentCommentId);
            reply.setPostId(parent.getPostId()); // giữ postId theo comment cha
            Comments savedReply = commentsRepository.save(reply);

            // Gửi notification cho chủ comment gốc (nếu khác người reply)
            if (!parent.getUserId().equals(reply.getUserId())) {
                Notifications noti = new Notifications(
                        parent.getUserId(),
                        "Ai đó đã trả lời bình luận của bạn.",
                        NotificationTypes.COMMENT,
                        reply.getPostId(),
                        savedReply.getId(),
                        null
                );
                notificationService.createNotification(noti);
            }

            return savedReply;
        }
        return null;
    }
    public Comments updateComment(String id, String newContent) {
        Optional<Comments> optionalComment = commentsRepository.findById(id);
        if (optionalComment.isPresent()) {
            Comments comment = optionalComment.get();
            comment.setContent(newContent);
            return commentsRepository.save(comment);
        }
        return null;
    }


}
