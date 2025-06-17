package com.Harum.Harum.Services;

import com.Harum.Harum.DTO.UserProfileDTO;
import com.Harum.Harum.DTO.ChangePasswordRequestDTO;
import com.Harum.Harum.Enums.RoleTypes;
import com.Harum.Harum.Models.Comments;
import com.Harum.Harum.Models.Roles;
import com.Harum.Harum.Models.Users;
import com.Harum.Harum.Repository.CommentRepo;
import com.Harum.Harum.Repository.FollowRepo;
import com.Harum.Harum.Repository.UserRepo;
import com.Harum.Harum.Security.JwtUtil;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepository;
    @Autowired
    private FollowRepo followRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CommentRepo commentRepo;

    // Get all users
    // public List<Users> getAllUsers() {
    // return userRepository.findAll();
    // }
    public Page<Users> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return userRepository.findAll(pageable);
    }

    public Page<Users> getDisabledUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return userRepository.findDisabledUsers(pageable);
    }

    public Page<Users> getEnabledUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return userRepository.findEnabledUsers(pageable);
    }

    // Get user by ID
    public Optional<Users> getUserById(String id) {
        return userRepository.findById(id);
    }

    // get user by email
    public Optional<Users> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Create new user
    public Users createUser(Users user) {
        if (user.getAvatarUrl() == null)
            user.setAvatarUrl("default_avatar.png");
        if (user.getCoverUrl() == null)
            user.setCoverUrl("default_cover.jpg");
        if (user.getBio() == null)
            user.setBio("Hello! I'm new here.");
        if (user.getRole() == null) {
            Roles defaultRole = (new Roles(RoleTypes.USER)); // Nếu chưa có, tạo mới
            user.setRole(defaultRole);
        }

        if (user.getCreatedAt() == null)
            user.setCreatedAt(Instant.now().toString());

        return userRepository.save(user);
    }

    // Update user
    public Optional<Users> updateUser(String id, Users userDetails) {
        return userRepository.findById(id).map(user -> {
            user.setUsername(userDetails.getUsername());
            user.setEmail(userDetails.getEmail());
            user.setPasswordHash(userDetails.getPasswordHash());
            user.setAvatarUrl(userDetails.getAvatarUrl());
            user.setCoverUrl(userDetails.getCoverUrl());
            user.setBio(userDetails.getBio());
            return userRepository.save(user);
        });
    }

    // Delete user
    public boolean deleteUser(String id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Get user profile
    public Optional<UserProfileDTO> getUserProfile(String userId) {
        Optional<Users> user = userRepository.findById(userId);
        if (user.isPresent()) {
            long followings = followRepository.countByFollowerId(userId);
            long followers = followRepository.countByFollowedId(userId);
            Users u = user.get();
            return Optional.of(new UserProfileDTO(
                    u.getId(), u.getUsername(), u.getEmail(), u.getAvatarUrl(),
                    u.getCoverUrl(), u.getBio(), u.getStatus(), followers, followings));
        }
        return Optional.empty();
    }

    // Update user profile
    public Optional<Users> updateUserProfile(String id, Users updatedUser) {
        return userRepository.findById(id).map(user -> {
            user.setUsername(updatedUser.getUsername());
            user.setEmail(updatedUser.getEmail());
            user.setAvatarUrl(updatedUser.getAvatarUrl());
            user.setCoverUrl(updatedUser.getCoverUrl());
            user.setBio(updatedUser.getBio());
            user.setStatus(updatedUser.getStatus());
            return userRepository.save(user);
        });
    }

    // Update Disable user status
    public Optional<Users> updateUserStatus(String id, Users updatedUser) {

        return userRepository.findById(id).map(user -> {
            user.setStatus(updatedUser.getStatus());
            return userRepository.save(user);
        });
    }

    // hàm mới:
    public Optional<Users> patchUserStatus(String id, String newStatus) {
        Optional<Users> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            Users userToUpdate = userOptional.get();
            userToUpdate.setStatus(newStatus); // Chỉ cập nhật trường status
            return Optional.of(userRepository.save(userToUpdate)); // Lưu lại
        }
        return Optional.empty();
    }

    public Optional<Users> getUserByCommentId(String commentId) {
        return commentRepo.findById(commentId)
                .flatMap(comment -> {
                    String userId = comment.getUserId();
                    if (userId == null) {
                        System.out.println("Comment has null userId: " + commentId);
                        return Optional.empty();
                    }
                    return userRepository.findById(userId);
                });
    }
}
