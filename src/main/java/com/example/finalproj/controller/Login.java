package com.example.finalproj.controller;


import com.example.finalproj.entity.User;
import com.example.finalproj.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RequestMapping("/bakery")
@Controller
public class Login {

    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, Model model) {
        User user = userService.findByEmail(email);
        if (user != null) {
//            if (passwordEncoder.matches(password.trim(), user.getPassword())) {
                if (user.getPassword().equals(password)) {
                if (user.getRole().equals("ROLE_ADMIN")) {
                    return "mainAdmin";
                } else {
                    return "ma in";
                }
            }
        }
        return "login";
    }

    @GetMapping("/addUser")
    public String addUser(Model model) {
        model.addAttribute("user", new User());
        return "addUser";
    }

    @PostMapping("/addUser")
    public String addUser(@ModelAttribute("user") User user , Model model) {
        userService.add(user);
        model.addAttribute("message","User added successfully!");
        return "login";
    }

}
