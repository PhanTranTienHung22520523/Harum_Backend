package com.Harum.Harum.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Objects;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String text) throws MessagingException {
        System.out.println("Sending email to: " + to);
        System.out.println("Subject: " + subject);
        System.out.println("Text: " + text);

        to = to.trim();  // Loại bỏ khoảng trắng đầu/cuối
        if (to.contains(" ")) {
            throw new IllegalArgumentException("Email address cannot contain spaces");
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("emperorhp31@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true); // true if want to send html

        mailSender.send(message);
    }

    //send Email with file
    public void sendEmailWithAttachment(String to, String subject, String text, MultipartFile file) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("emperorhp31@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true);

        if (!file.isEmpty()) {
            helper.addAttachment(Objects.requireNonNull(file.getOriginalFilename()), file);
        }

        mailSender.send(message);
    }
}
