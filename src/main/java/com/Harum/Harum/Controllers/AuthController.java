package com.Harum.Harum.Controllers;

import com.Harum.Harum.Constants.StatusCodes;
import com.Harum.Harum.DTO.ChangePasswordRequestDTO;
import com.Harum.Harum.DTO.VerifyOtpRequestDTO;
import com.Harum.Harum.Enums.RoleTypes;
import com.Harum.Harum.Models.Roles;
import com.Harum.Harum.Models.Users;
import com.Harum.Harum.Repository.RoleRepo;
import com.Harum.Harum.Repository.UserRepo;
import com.Harum.Harum.Security.HarumUserDetailServices;
import com.Harum.Harum.Security.JwtUtil;
import com.Harum.Harum.Services.EmailService;

import com.Harum.Harum.Services.OtpService;
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
    private OtpService otpService; // Dịch vụ lưu OTP

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

        // Tạo và lưu OTP vào bộ nhớ

        String otp = otpService.generateOtp(user.getEmail());

        // Gửi OTP qua email
        try {
            emailService.sendEmail(user.getEmail(), "Harum OTP", "Mã OTP này của bạn sẽ có hiệu lực trong 10 phút:  " + otp);
            return ResponseEntity.ok("OTP has been sent to your email");
        } catch (MessagingException e) {
            return ResponseEntity.status(500).body("Failed to send OTP email");
        }
    }

    // xác thực OTP để tạo tài khoản
    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOTP(@RequestBody VerifyOtpRequestDTO request) {
        String email = request.getEmail();
        String otp = request.getOtp();
        Users user = request.getUser();
        if (!otpService.validateOtp(email, otp)) {
            return ResponseEntity.badRequest().body("Invalid or expired OTP");
        }

        // Mã hóa mật khẩu trước khi lưu vào database
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));

        // Gán quyền mặc định là USER nếu không có
        Roles defaultRole = roleRepository.findByRoleName(RoleTypes.USER)
                .orElseGet(() -> roleRepository.save(new Roles(RoleTypes.USER)));

        user.setRole(defaultRole);
        user.setCreatedAt(Instant.now().toString());

        // Lưu user vào database
        userRepository.save(user);

        // Xóa OTP sau khi xác thực thành công
        otpService.removeOtp(email);

        return ResponseEntity.status(StatusCodes.CREATED.getCode()).body("Account created successfully!");
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

//    @PostMapping("/send-otp")
//    public String sendOTP(@RequestParam String email) {
//        try {
//            emailOTPService.sendOTP(email);
//            return "OTP has been sent to your email";
//        } catch (MessagingException e) {
//            return "Failed to send OTP";
//        }
//    }


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
    // random otp có  6 số
    private String generateRandomOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // Tạo số từ 100000 - 999999
        return String.valueOf(otp);
    }
}
