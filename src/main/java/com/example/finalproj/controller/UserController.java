package com.example.finalproj.controller;


import com.example.finalproj.entity.User;
import com.example.finalproj.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.finalproj.service.EmailService;

import java.util.List;

@Controller
@RequestMapping("/bakery")
public class UserController {

    private final UserService userService;

    private final EmailService emailService;



    public UserController(EmailService emailService, UserService userService ) {
        this.emailService = emailService;
        this.userService = userService;
    }

    @PostMapping("/send-welcome-email")
    public String sendWelcomeEmail(@RequestParam String email) {
        String subject = "Welcome!";
        String body = "Welcome to our service.";
        emailService.sendEmail(email, subject, body);
        return "Email sent!";
    }


    @GetMapping("/users")
    public String users(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "users";
    }


}
