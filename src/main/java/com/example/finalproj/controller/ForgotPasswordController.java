package com.example.finalproj.controller;

import com.example.finalproj.entity.User;
import com.example.finalproj.repository.UserRepository;
import com.example.finalproj.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class ForgotPasswordController {

    private final UserRepository userRepository;
    private final EmailService emailService;

    public ForgotPasswordController(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User with email not found"));

        String resetCode = String.valueOf((int)(Math.random() * 1000000));

        user.setResetCode(resetCode);
        userRepository.save(user);

        emailService.sendCode(email, resetCode);

        return ResponseEntity.ok("Reset code sent to your email.");
    }
}
