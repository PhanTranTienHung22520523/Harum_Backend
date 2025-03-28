package com.Harum.Harum.Controllers;

import com.Harum.Harum.DTO.EmailRequestDTO;
import com.Harum.Harum.Models.Users;
import com.Harum.Harum.Services.EmailService;
import com.Harum.Harum.Services.UserService;
import com.Harum.Harum.DTO.UserProfileDTO;
import com.Harum.Harum.DTO.ChangePasswordRequestDTO;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;

    // Get all users
    @GetMapping
    public ResponseEntity<List<Users>> getAllUsers() {
        List<Users> users = userService.getAllUsers();
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
//    // Create new user
//    @PostMapping
//    public ResponseEntity<Users> createUser(@RequestBody Users user) {
//        Users createdUser = userService.createUser(user);
//        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
//    }

    // Update user
    @PutMapping("/{id}")
    public ResponseEntity<Users> updateUser(@PathVariable String id, @RequestBody Users userDetails) {
        Optional<Users> updatedUser = userService.updateUser(id, userDetails);
        return updatedUser.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    //Get user profile
    @GetMapping(value = "/profile/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserProfile(@PathVariable String id) {
        Optional<UserProfileDTO> userProfile = userService.getUserProfile(id);
        return userProfile.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    //Update user profile
    @PutMapping("/profile/{id}")
    public ResponseEntity<Users> updateUserProfile(@PathVariable String id, @RequestBody Users userDetails) {
        Optional<Users> updatedUser = userService.updateUserProfile(id, userDetails);
        return updatedUser.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    //Change password
//    @PutMapping("/change-password")
//    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequestDTO changePasswordDTO,
//                                            @RequestHeader("Authorization") String token) {
//        try {
//            userService.changePassword(changePasswordDTO, token);
//            return ResponseEntity.ok("Đổi mật khẩu thành công");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        }
//    }

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

}
