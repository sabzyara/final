package com.example.finalproj.controller;


import com.example.finalproj.entity.User;
import com.example.finalproj.service.EmailService;
import com.example.finalproj.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static org.hibernate.sql.ast.SqlTreeCreationLogger.LOGGER;

@AllArgsConstructor
@RequestMapping("/bakery")
@Controller
public class Login {

    private final UserService userService;

   private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;




    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model) {
        LOGGER.info("Attempting login for username: " + username);

        User user = userService.findByUsername(username);
        if (user != null) {
            LOGGER.info("Password entered: " + password);
            LOGGER.info("Password stored (hashed): " + user.getPassword());

            if (passwordEncoder.matches(password.trim(), user.getPassword())) {
                LOGGER.info("Password matched for user: " + username);

                if (user.getRole().equals("ROLE_ADMIN")) {
                    return "mainAdmin";
                } else {
                    return "main";
                }

            } else {
                LOGGER.warn("Invalid password for user: " + username);
            }
        } else {
            LOGGER.warn("User not found: " + username);
        }

        model.addAttribute("loginError", "Invalid username or password!");
        return "login";
    }

    @GetMapping("/addUser")
    public String addUser(Model model) {
        model.addAttribute("user", new User());
        return "addUser";
    }

    @PostMapping("/addUser")
    public String addUser(@ModelAttribute("user") User user , Model model) {
        user.setProfileImage("/images/olaf-default-avatar.jpg");
        userService.add(user);
        model.addAttribute("message","User added successfully!");
        return "login";
    }


}
