package com.Harum.Harum.Controllers;

import com.Harum.Harum.DTO.EmailRequestDTO;
import com.Harum.Harum.DTO.UserStatusUpdateRequest;
import com.Harum.Harum.Models.Users;
import com.Harum.Harum.Services.CloudinaryService;
import com.Harum.Harum.Services.EmailService;
import com.Harum.Harum.Services.UserService;
import com.Harum.Harum.DTO.UserProfileDTO;
import com.Harum.Harum.DTO.UserStatusPatchRequest;
import com.Harum.Harum.DTO.ChangePasswordRequestDTO;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private CloudinaryService cloudinaryService;

    // Get all users
    @GetMapping
    public ResponseEntity<Page<Users>> getAllUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Users> users = userService.getAllUsers(page, size);
        return ResponseEntity.ok(users);
    }

    // get user account being disable
    @GetMapping("/disabled")
    public ResponseEntity<Page<Users>> getDisabledUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Users> users = userService.getDisabledUsers(page, size);
        return ResponseEntity.ok(users);
    }

    // get user account NOT being disable
    @GetMapping("/enabled")
    public ResponseEntity<Page<Users>> getEnabledUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Users> users = userService.getEnabledUsers(page, size);
        return ResponseEntity.ok(users);
    }

    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable String id) {
        Optional<Users> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
    //
    // // Create new user
    // @PostMapping
    // public ResponseEntity<Users> createUser(@RequestBody Users user) {
    // Users createdUser = userService.createUser(user);
    // return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    // }

    // Update user
    @PutMapping("/{id}")
    public ResponseEntity<Users> updateUser(@PathVariable String id, @RequestBody Users userDetails) {
        Optional<Users> updatedUser = userService.updateUser(id, userDetails);
        return updatedUser.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Get user profile
    @GetMapping(value = "/profile/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserProfile(@PathVariable String id) {
        Optional<UserProfileDTO> userProfile = userService.getUserProfile(id);
        return userProfile.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Update user profile
    @PutMapping(value = "/profile/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateUserProfileWithImages(
            @PathVariable String id,
            @RequestPart("user") Users userDetails,
            @RequestPart(value = "avatar", required = false) MultipartFile avatar,
            @RequestPart(value = "cover", required = false) MultipartFile cover) {

        try {
            // Upload ảnh avatar nếu có
            if (avatar != null && !avatar.isEmpty()) {
                String avatarUrl = cloudinaryService.uploadFile(avatar);
                userDetails.setAvatarUrl(avatarUrl);
            }

            // Upload ảnh cover nếu có
            if (cover != null && !cover.isEmpty()) {
                String coverUrl = cloudinaryService.uploadFile(cover);
                userDetails.setCoverUrl(coverUrl);
            }

            Optional<Users> updatedUser = userService.updateUserProfile(id, userDetails);
            return updatedUser.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Lỗi khi cập nhật profile: " + e.getMessage());
        }
    }
    // Change password
    // @PutMapping("/change-password")
    // public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequestDTO
    // changePasswordDTO,
    // @RequestHeader("Authorization") String token) {
    // try {
    // userService.changePassword(changePasswordDTO, token);
    // return ResponseEntity.ok("Đổi mật khẩu thành công");
    // } catch (Exception e) {
    // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    // }
    // }

    @PreAuthorize("hasAuthority('ADMIN')")
    // Delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        boolean deleted = userService.deleteUser(id);
        return deleted ? ResponseEntity.noContent().build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("/send")
    public String sendEmailWithJson(@RequestBody EmailRequestDTO request) {
        try {
            emailService.sendEmail(request.getTo(), request.getSubject(), request.getText());
            return "Email sent successfully!";
        } catch (MessagingException e) {
            return "Error while sending email: " + e.getMessage();
        }
    }

    // Cập nhật trạng thái tài khoản người dùng và gửi email thông báo trước đó
    @PutMapping("/status/{id}")
    public ResponseEntity<?> updateUserStatus(
            @PathVariable String id,
            @RequestBody UserStatusUpdateRequest request) {
        try {
            Users updatedUser = request.getUser();
            String subject = "Thông báo thay đổi trạng thái tài khoản";

            emailService.sendEmail(updatedUser.getEmail(), subject, request.getEmailContent());

            Optional<Users> updated = userService.updateUserStatus(id, updatedUser);
            if (updated.isPresent()) {
                return ResponseEntity.ok(updated.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MessagingException e) {
            return ResponseEntity.status(500).body("Lỗi khi gửi email: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi hệ thống: " + e.getMessage());
        }
    }

    // hàm mới:
    @PutMapping("/put-status/{id}")
    public ResponseEntity<?> updateUserStatus(
            @PathVariable String id,
            @RequestBody UserStatusPatchRequest request) { // <-- Dùng DTO mới
        try {
            // Tìm user để lấy email
            Optional<Users> userOptional = userService.getUserById(id); // Giả sử có hàm này
            if (userOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            Users user = userOptional.get();

            // Gửi email
            String subject = "Thông báo thay đổi trạng thái tài khoản";
            emailService.sendEmail(user.getEmail(), subject, request.getEmailContent());

            // Cập nhật status
            // Truyền newStatus vào service thay vì cả object
            Optional<Users> updated = userService.patchUserStatus(id, request.getStatus());

            return ResponseEntity.ok(updated.get());

        } catch (MessagingException e) {
            return ResponseEntity.status(500).body("Lỗi khi gửi email: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi hệ thống: " + e.getMessage());
        }
    }

}
