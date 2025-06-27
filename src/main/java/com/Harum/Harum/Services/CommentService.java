package com.Harum.Harum.Services;

import com.Harum.Harum.DTO.CommentDetailsDTO;
import com.Harum.Harum.DTO.PostDetailsDTO;
import com.Harum.Harum.Enums.NotificationTypes;
import com.Harum.Harum.Enums.ReportStatus;
import com.Harum.Harum.Models.Comments;
import com.Harum.Harum.Models.Notifications;
import com.Harum.Harum.Models.Posts;
import com.Harum.Harum.Models.Users;
import com.Harum.Harum.Repository.CommentRepo;
import com.Harum.Harum.Repository.UserRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.messaging.simp.SimpMessagingTemplate;

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

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private UserRepo userRepo;

    private CommentDetailsDTO convertToDTO(Comments comment) {
        Users user = userRepo.findById(comment.getUserId()).orElse(null);
        return new CommentDetailsDTO(
                comment.getId(),
                comment.getPostId(),
                comment.getUserId(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getParentIdc(),
                comment.getStatus(),
                user != null ? user.getUsername() : null,
                user != null ? user.getAvatarUrl() : null);
    }

    public List<Comments> getAllComments() {
        return commentsRepository.findAll();
    }

    public Optional<CommentDetailsDTO> getCommentById(String id) {
        return commentsRepository.findById(id).map(this::convertToDTO);
    }

    public List<CommentDetailsDTO> getCommentsByPostId(String postId) {
        List<Comments> comments = commentsRepository.findByPostId(postId);
        List<CommentDetailsDTO> dtoList = new ArrayList<>();

        for (Comments comment : comments) {
            Users user = userRepo.findById(comment.getUserId()).orElse(null);

            CommentDetailsDTO dto = new CommentDetailsDTO(
                    comment.getId(),
                    comment.getPostId(),
                    comment.getUserId(),
                    comment.getContent(),
                    comment.getCreatedAt(),
                    comment.getParentIdc(),
                    comment.getStatus(),
                    user != null ? user.getUsername() : null,
                    user != null ? user.getAvatarUrl() : null);

            dtoList.add(dto);
        }

        return dtoList;
    }

    public List<CommentDetailsDTO> getCommentsByUserId(String userId) {
        List<Comments> comments = commentsRepository.findByUserId(userId);
        List<CommentDetailsDTO> dtoList = new ArrayList<>();
        for (Comments c : comments) {
            dtoList.add(convertToDTO(c));
        }
        return dtoList;
    }

    public CommentDetailsDTO toggleCommentStatus(String commentId) {
        Comments comment = commentsRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + commentId));

        String currentStatus = comment.getStatus();

        if ("ENABLE".equals(currentStatus)) {
            comment.setStatus("DISABLE");
        } else {
            comment.setStatus("ENABLE");
        }

        Comments updatedComment = commentsRepository.save(comment);

        return convertToDTO(updatedComment);
    }

    public Comments createComment(Comments comment) {
        Comments saved = commentsRepository.save(comment);

        Optional<PostDetailsDTO> postOpt = postService.getPostDetailsById(comment.getPostId());
        if (postOpt.isPresent()) {
            PostDetailsDTO post = postOpt.get();
            String ownerId = post.getUserId();
            if (!ownerId.equals(comment.getUserId())) {
                Notifications noti = new Notifications(
                        ownerId,
                        "Một người dùng đã bình luận vào bài viết của bạn.",
                        NotificationTypes.COMMENT,
                        comment.getPostId(),
                        saved.getId(),
                        null);
                Notifications savedNoti = notificationService.createNotification(noti);

                // Gửi realtime notification qua WebSocket cho user ownerId
                messagingTemplate.convertAndSendToUser(
                        ownerId,
                        "/queue/notifications",
                        savedNoti);
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
                        null);
                Notifications savedNoti = notificationService.createNotification(noti);

                // Gửi realtime notification qua WebSocket cho user ownerId (chủ comment gốc)
                messagingTemplate.convertAndSendToUser(
                        parent.getUserId(),
                        "/queue/notifications",
                        savedNoti);
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

    public Comments updateCommentStatus(String id, ReportStatus status) {
        Optional<Comments> optionalComment = commentsRepository.findById(id);
        if (optionalComment.isPresent()) {
            Comments comment = optionalComment.get();
            comment.setReportStatus(status);
            return commentsRepository.save(comment);
        }
        return null;
    }

    public List<CommentDetailsDTO> getCommentssByStatus(ReportStatus status) {
        List<Comments> comments = commentsRepository.findByReportStatus(status);
        List<CommentDetailsDTO> dtoList = new ArrayList<>();
        for (Comments c : comments) {
            dtoList.add(convertToDTO(c));
        }
        return dtoList;
    }

    public Optional<Users> getUserByCommentId(String commentId) {
        return commentsRepository.findById(commentId)
                .flatMap(comment -> {
                    String userId = comment.getUserId(); // hoặc getAuthorId(), tùy thuộc vào schema của bạn
                    if (userId == null) {
                        return Optional.empty(); // hoặc throw custom exception nếu muốn
                    }
                    return userRepo.findById(userId);
                });
    }

    public Optional<Comments> getCommentByIdd(String id) {
        return commentsRepository.findById(id);
    }

}