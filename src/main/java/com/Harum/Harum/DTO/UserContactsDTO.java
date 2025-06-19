package com.Harum.Harum.DTO;



public class UserContactsDTO {
    private String id;
    private String username;
    private String avatarUrl;

    // Constructor có tham số
    public UserContactsDTO(String id, String username, String avatarUrl) {
        this.id = id;
        this.username = username;
        this.avatarUrl = avatarUrl;
    }



    // Getter & Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
