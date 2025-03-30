package com.Harum.Harum.Controllers;

import com.Harum.Harum.Constants.StatusCodes;
import com.Harum.Harum.DTO.ChangePasswordRequestDTO;
import com.Harum.Harum.DTO.VerifyOTPRequestDTO;
import com.Harum.Harum.Enums.RoleTypes;
import com.Harum.Harum.Models.Roles;
import com.Harum.Harum.Models.Users;
import com.Harum.Harum.Repository.RoleRepo;
import com.Harum.Harum.Repository.UserRepo;
import com.Harum.Harum.Security.HarumUserDetailServices;
import com.Harum.Harum.Security.JwtUtil;
import com.Harum.Harum.Services.EmailOTPService;
import com.Harum.Harum.Services.EmailService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private RoleRepo roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private HarumUserDetailServices harumUserDetailServices;

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmailOTPService emailOTPService;

    // Đăng nhập
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Users user) {
        Optional<Users> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isEmpty()) {
            return ResponseEntity.status(StatusCodes.UNAUTHORIZED.getCode())
                    .body(Map.of("message", "Invalid credentials"));
        }

        Users foundUser = existingUser.get();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPasswordHash())
        );

        // Lấy role từ user
        String role = foundUser.getRole().getRoleName().name();
        String token = jwtUtil.generateToken(foundUser.getEmail(), role);

        // Trả về token và role
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("role", role);
        response.put("email", foundUser.getEmail());
        response.put("username", foundUser.getUsername());

        return ResponseEntity.status(StatusCodes.OK.getCode()).body(response);
    }

    // Đăng ký
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody Users user) {
        // Kiểm tra email đã tồn tại chưa
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.status(StatusCodes.BAD_REQUEST.getCode())
                    .body(Map.of("message", "Email already exists"));
        }

        // Kiểm tra username đã tồn tại chưa
        if (userRepository.existsByUsername(user.getUsername())) {
            return ResponseEntity.status(StatusCodes.BAD_REQUEST.getCode())
                    .body(Map.of("message", "Username already exists"));
        }

        // Mã hóa mật khẩu trước khi lưu vào database
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));

        // Gán quyền mặc định là USER nếu không có
        Roles defaultRole = roleRepository.findByRoleName(RoleTypes.USER)
                .orElseGet(() -> roleRepository.save(new Roles(RoleTypes.USER)));

        user.setRole(defaultRole);
        user.setCreatedAt(Instant.now().toString());

        // Lưu user vào database
        Users savedUser = userRepository.save(user);

        return ResponseEntity.status(StatusCodes.CREATED.getCode()).body(savedUser);
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestBody ChangePasswordRequestDTO request) {
        Optional<Users> optionalUser = userRepository.findById(request.getUserId());

        if (optionalUser.isEmpty()) {
            return "User not found";
        }

        Users user = optionalUser.get();

        // Kiểm tra mật khẩu cũ có đúng không
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPasswordHash())) {
            return "Old password is incorrect";
        }

        // Kiểm tra mật khẩu mới và xác nhận mật khẩu có trùng nhau không
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            return "New password and confirmation do not match";
        }

        // Mã hóa và cập nhật mật khẩu mới
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return "Password changed successfully";
    }

    @PostMapping("/send-otp")
    public String sendOTP(@RequestParam String email) {
        try {
            emailOTPService.sendOTP(email);
            return "OTP has been sent to your email";
        } catch (MessagingException e) {
            return "Failed to send OTP";
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOTP(@RequestBody VerifyOTPRequestDTO request) {
        if (!emailOTPService.verifyOTP(request.getEmail(), request.getOtp())) {
            System.out.println("Received OTP: " + request.getOtp());
            System.out.println("Stored OTP: " + emailOTPService.getStoredOTP(request.getEmail()));
            return ResponseEntity.badRequest().body("Invalid OTP");
        }

        Optional<Users> userOptional = userRepository.findByEmail(request.getEmail());
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        Users user = userOptional.get();

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("Passwords do not match");
        }

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return ResponseEntity.ok("Password reset successfully");
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        Optional<Users> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        Users user = userOptional.get();
        String newPassword = generateRandomPassword();
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        try {
            emailService.sendEmail(email, "Your New Password", "Your new password is: " + newPassword);
            return ResponseEntity.ok("A new password has been sent to your email");
        } catch (MessagingException e) {
            return ResponseEntity.status(500).body("Failed to send new password email");
        }
    }

    private String generateRandomPassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%&*";
        StringBuilder password = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            password.append(characters.charAt(random.nextInt(characters.length())));
        }
        return password.toString();
    }
}
