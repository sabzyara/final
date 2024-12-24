package com.example.finalproj.controller;

import com.example.finalproj.entity.ResetPasswordRequest;
import com.example.finalproj.entity.User;
import com.example.finalproj.repository.UserRepository;
import com.example.finalproj.service.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class ResetPasswordController {

    private final UserRepository userRepository;

    public ResetPasswordController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @GetMapping("/reset-password")
    public ResponseEntity<String> getResetPasswordPage(@RequestParam("email") String email, @RequestParam("code") String code) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User with email not found"));

        if (user.getResetCode() == null || !user.getResetCode().equals(code)) {
            return ResponseEntity.badRequest().body("Invalid or missing reset code.");
        }

        return ResponseEntity.ok("Code is valid. Please proceed with setting a new password.");
    }


    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User with email not found"));

        if (!user.getResetCode().equals(request.getResetCode())) {
            return ResponseEntity.badRequest().body("Invalid reset code.");
        }

        user.setPassword(request.getNewPassword());
        user.setResetCode(null);
        userRepository.save(user);

        return ResponseEntity.ok("Password updated successfully.");
    }
}
