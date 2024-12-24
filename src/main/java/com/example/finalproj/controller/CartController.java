package com.example.finalproj.controller;

import com.example.finalproj.entity.Product;
import com.example.finalproj.entity.User;
import com.example.finalproj.service.CartService;
import com.example.finalproj.service.ProductService;
import com.example.finalproj.service.UserService;  // Предполагается, что есть сервис для работы с пользователями
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@AllArgsConstructor
@RequestMapping("/bakery")
@Controller
public class CartController {

    private final ProductService productService;
    private final CartService cartService;
    private final JavaMailSender mailSender;
    private final UserService userService;

    @Autowired
    public CartController(JavaMailSender mailSender, ProductService productService, CartService cartService, UserService userService) {
        this.mailSender = mailSender;
        this.productService = productService;
        this.cartService = cartService;
        this.userService = userService;
    }

    private User getAuthenticatedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            return userService.findByUsername(username);
        } else {
            throw new RuntimeException("Invalid authentication");
        }
    }

    @PostMapping("/buy")
    public String buy(Model model) {
        List<Product> cartItems = cartService.getCartItems();

        if (cartItems.isEmpty()) {
            return "redirect:/bakery/cart";
        }

        StringBuilder receipt = new StringBuilder(" ❄Your order has been successfully placed!❄ \n\nShopping list:\n");
        double totalPrice = 0;
        for (Product item : cartItems) {
            receipt.append(item.getName())
                    .append(" - $")
                    .append(item.getPrice())
                    .append("\n");
            totalPrice += item.getPrice();
        }
        receipt.append("\nTotal sum: $").append(totalPrice);
        receipt.append("\n\n ❄ Thank you for your purchase! ❄");

        User authenticatedUser = getAuthenticatedUser();
        String userEmail = authenticatedUser.getEmail();


        sendEmail(userEmail, "Purchase receipt", receipt.toString());

        cartService.clearCart();
        return "redirect:/bakery/cart";
    }

    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    @PostMapping("/addToCart")
    public String addToCart(@RequestParam("prodId") Long prodId, Model model) {
        Product product = productService.getProductById(prodId);
        if (product != null) {
            cartService.addProductToCart(product);
        }
        return "redirect:/bakery/productsUser";
    }


    @GetMapping("/cart")
    public String showCart(Model model) {
        model.addAttribute("cartItems", cartService.getCartItems());

        double totalPrice = cartService.getCartItems().stream()
                .mapToDouble(Product::getPrice)
                .sum();
        model.addAttribute("totalPrice", totalPrice);

        return "cart";
    }

    @PostMapping("/clearCart")
    public String clearCart() {
        cartService.clearCart();
        return "redirect:/bakery/cart";
    }
}





