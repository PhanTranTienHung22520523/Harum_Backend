    package com.Harum.Harum.Models;


    import com.Harum.Harum.Enums.RoleTypes;
    import jakarta.validation.constraints.Email;
    import jakarta.validation.constraints.NotBlank;
    import jakarta.validation.constraints.Pattern;
    import jakarta.validation.constraints.Size;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;
    import org.springframework.data.mongodb.core.mapping.DBRef;
    import org.springframework.data.mongodb.core.mapping.Document;
    import org.springframework.data.annotation.Id;
    import java.time.Instant;

    @Document (collection = "users")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class Users {
        @Id
        private String id;

        @NotBlank(message = "Username cannot be empty")
        @Size(min = 6, max = 20, message = "Username must be between 6 and 20 characters")
        private String username;

        @NotBlank(message = "Email cannot be empty")
        @Email(message = "Invalid email format")
        private String email;

        @NotBlank(message = "Password cannot be empty")
        @Size(min = 8, message = "Password must be at least 6 characters")
        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                message = "Password must contain at least one uppercase letter, one number, and one special character"
        )
        private String passwordHash;
        private String avatarUrl;
        private String coverUrl;
        private String status ;
        private String bio;
        @DBRef // Liên kết đến bảng Roles
        private Roles role;
        private String createdAt;
        private String resetToken;
        private long otpExpiryTime; //



        public Users(String username, String email, String passwordHash, String avatarUrl, String coverUrl, String bio, Roles role) {
            this.username = username;
            this.email = email;
            this.passwordHash = passwordHash;
            this.avatarUrl = avatarUrl;
            this.coverUrl = coverUrl;
            this.bio = bio;
            this.role = role;
            this.createdAt = Instant.now().toString();
        }



        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getStatus() {return status;}
        public void setStatus(String status) {this.status=status;}
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPasswordHash() { return passwordHash; }
        public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
        public String getAvatarUrl() { return avatarUrl; }
        public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
        public String getCoverUrl() { return coverUrl; }
        public void setCoverUrl(String coverUrl) { this.coverUrl = coverUrl; }
        public String getBio() { return bio; }
        public void setBio(String bio) { this.bio = bio; }
        public Roles getRole() {
            return role;
        }
        public void setRole(Roles role) {
            this.role = role;
        }
        public String getCreatedAt() { return createdAt; }
        public void setCreatedAt(String string) { this.createdAt = Instant.now().toString(); }
        public String getResetToken() {
            return resetToken;
        }

        public void setResetToken(String resetToken) {
            this.resetToken = resetToken;
        }

        public long getOtpExpiryTime() {
            return otpExpiryTime;
        }

        public void setOtpExpiryTime(long otpExpiryTime) {
            this.otpExpiryTime = otpExpiryTime;
        }
    }
