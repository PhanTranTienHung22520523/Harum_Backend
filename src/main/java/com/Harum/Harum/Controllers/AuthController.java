package com.Harum.Harum.Controllers;

import com.Harum.Harum.Constants.StatusCodes;
import com.Harum.Harum.Enums.RoleTypes;
import com.Harum.Harum.Models.Roles;
import com.Harum.Harum.Models.Users;
import com.Harum.Harum.Repository.RoleRepo;
import com.Harum.Harum.Repository.UserRepo;
import com.Harum.Harum.Security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
}
