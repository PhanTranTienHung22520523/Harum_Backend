package com.Harum.Harum.Services;

import com.Harum.Harum.Models.Users;
import com.Harum.Harum.Repository.UserRepo;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final Random random = new Random();

    public String generateOTP() {
        return String.valueOf(100000 + random.nextInt(900000)); // Tạo mã 6 chữ số
    }

    @Transactional
    public void sendOTP(String to) throws MessagingException {

        Users user = userRepository.findByEmail(to)
                .orElseThrow(()->new IllegalArgumentException("Email không tồn tại!"));


        String otp = generateOTP();
        user.setOtp(otp);
        user.setOtpExpiryTime(System.currentTimeMillis() + (5 * 60 * 1000)); // Hết hạn sau 5 phút
        userRepository.save(user);

        // Gửi OTP qua email
        String subject = "Your Account OTP";
        String body = "Your OTP code is: " + otp+", it will be expired in 5 minutes!";
        emailService.sendEmail(to, subject, body);
    }

    public boolean verifyOTP(String email, String otp) {
        Users user = userRepository.findByEmail(email)
                .orElse(null);


        // Kiểm tra OTP đúng và chưa hết hạn
        if (user == null || user.getOtp() == null || user.getOtpExpiryTime() < System.currentTimeMillis()) {
            return false; // OTP không hợp lệ hoặc đã hết hạn
        }

        return otp.equals(user.getOtp());
    }
}
