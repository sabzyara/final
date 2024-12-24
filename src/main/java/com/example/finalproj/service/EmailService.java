package com.example.finalproj.service;


import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("whycookiesaresad@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    public void sendCode(String toEmail, String code) {
        String resetPasswordLink = "http://localhost:8080/auth/reset-password?email=" + toEmail + "&code=" + code;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("whycookiesaresad@gmail.com");
        message.setTo(toEmail);
        message.setSubject("Your password reset code");
        message.setText("Hello, please click on the following link to reset your password:\n\n" +
                resetPasswordLink + "\n\n" +
                "Enter your code: " + code + " on the password reset page.");

        mailSender.send(message);
    }
}
