package com.Harum.Harum.DTO;

import com.Harum.Harum.Models.Users;

public class UserStatusUpdateRequest {
    private Users user;
    private String emailContent;

    // Getters and Setters
    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public String getEmailContent() {
        return emailContent;
    }

    public void setEmailContent(String emailContent) {
        this.emailContent = emailContent;
    }
}