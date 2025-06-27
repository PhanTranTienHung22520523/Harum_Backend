package com.Harum.Harum.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserProfileDTO {
    private String id;
    private String username;
    private String email;
    private String avatarUrl;
    private String coverUrl;
    private String bio;
    private long followers;
    private long followings;
    private String status;
    List<FavoriteTopicDTO> favoriteTopics;
    private long postCount;

    public UserProfileDTO(
            String id, String username, String email, String avatarUrl,
            String coverUrl, String bio, String status,
            long followers, long followings,
            List<FavoriteTopicDTO> favoriteTopics // thêm dòng này

    ) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.avatarUrl = avatarUrl;
        this.coverUrl = coverUrl;
        this.bio = bio;
        this.status = status;
        this.followers = followers;
        this.followings = followings;
        this.favoriteTopics = favoriteTopics; // gán vào
    }

    public long getPostCount() {
        return postCount;
    }

    public void setPostCount(long postCount) {
        this.postCount = postCount;
    }
    public List<FavoriteTopicDTO> getFavoriteTopics() {
        return favoriteTopics;
    }

    public void setFavoriteTopics(List<FavoriteTopicDTO> favoriteTopics) {
        this.favoriteTopics = favoriteTopics;
    }
    public String getId() { return id; }

    public String getStatus() {
        return status;
    }

    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getAvatarUrl() { return avatarUrl; }
    public String getCoverUrl() { return coverUrl; }
    public String getBio() { return bio; }
    public long getFollowers() { return followers; }
    public long getFollowings() { return followings; }
}
