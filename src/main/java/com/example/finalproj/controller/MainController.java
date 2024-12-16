package com.example.finalproj.controller;

import com.example.finalproj.entity.User;
import com.example.finalproj.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RequestMapping("/bakery")
@AllArgsConstructor
@Controller
public class MainController {

    private final UserService usersService;
//
//    @GetMapping("/main")
//    public String mainPage(Principal principal, Model model) {
//        String email = principal.getName();
//        User user = usersService.findByEmail(email);
//        model.addAttribute("user", user);
//        return "main";
//    }

    @GetMapping("/main")
    public String main() {
        return "main";
    }
}