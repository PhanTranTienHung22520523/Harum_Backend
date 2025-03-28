package com.Harum.Harum.Services;

import com.Harum.Harum.Models.Users;
import com.Harum.Harum.Repository.UserRepo;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
public class EmailOTPService {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private EmailService emailService;

    private final Map<String, String> otpStorage = new HashMap<>();
    private final Random random = new Random();

    public String generateOTP() {
        return String.valueOf(100000 + random.nextInt(900000)); // Tạo mã 6 chữ số
    }

    public void sendOTP(String to) throws MessagingException {

        Optional<Users> userOptional = userRepository.findByEmail(to);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("Email không tồn tại!");
        }

        Users user = userOptional.get();
        String otp = generateOTP();
        user.setResetToken(otp);
        user.setOtpExpiryTime(System.currentTimeMillis() + (5 * 60 * 1000)); // Hết hạn sau 5 phút
        userRepository.save(user);

        // Gửi OTP qua email
        String subject = "Your Password Reset OTP";
        String body = "Your OTP code is: " + otp;
        emailService.sendEmail(to, subject, body);
    }

    public String getStoredOTP(String email) {
        return otpStorage.get(email);
    }
    public boolean verifyOTP(String email, String otp) {
        Optional<Users> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            return false;
        }

        Users user = userOptional.get();

        // Kiểm tra OTP đúng và chưa hết hạn
        if (otp.equals(user.getResetToken()) && System.currentTimeMillis() <= user.getOtpExpiryTime()) {
            return true;
        }
        return false;
    }
}
