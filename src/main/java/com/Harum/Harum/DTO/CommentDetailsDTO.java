package com.Harum.Harum.DTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class CommentDetailsDTO {
    private String id;
    private String postId;
    private String userId;
    private String content;
    private String createdAt;
    private String parentId;

    // Bổ sung thông tin người dùng
    private String username;
    private String avatarUrl;


    public CommentDetailsDTO(String id, String postId, String userId, String content, String createdAt, String s, String s1, String parentId) {
    }
}
