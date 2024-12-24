package com.example.finalproj.controller;

import com.example.finalproj.entity.Product;
import com.example.finalproj.entity.User;
import com.example.finalproj.service.ProductService;
import com.example.finalproj.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RequestMapping("/bakery")
@AllArgsConstructor
@Controller
public class MainController {

    private final ProductService productService;
    private final UserService userService;

    @GetMapping("/main")
    public String mainPage(Model model) {
        List<Product> products = productService.findAll();
        model.addAttribute("products", products.subList(0, Math.min(products.size(), 4)));
        User authenticatedUser = userService.getAuthenticatedUser();
        if ("ROLE_ADMIN".equals(authenticatedUser.getRole())) {
            return "mainAdmin";
        } else {
            return "main";
        }
    }
}
