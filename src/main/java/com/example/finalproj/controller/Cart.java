//package com.example.finalproj.controller;
//
//import com.example.finalproj.entity.CartItem;
//import com.example.finalproj.entity.User;
//import com.example.finalproj.service.CartService;
//import lombok.AllArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RequestMapping("/bakery")
//@AllArgsConstructor
//@Controller
//public class Cart {
//
//    @Autowired
//    private CartService cartService;
//
//    @PostMapping("/addCart")
//    public ResponseEntity<Void> addToCart(User user, @RequestParam Long productId, @RequestParam int quantity) {
//        cartService.addToCart(user, productId, quantity);
//        return ResponseEntity.ok().build();
//    }
//
//    @GetMapping("/carts")
//    public List<CartItem> viewCart( User user) {
//        return cartService.getCartItems(user);
//    }
//
//    @PostMapping("/updateCart")
//    public ResponseEntity<Void> updateCartItem( User user, @RequestParam Long productId, @RequestParam int quantity) {
//        cartService.updateCartItemQuantity(user, productId, quantity);
//        return ResponseEntity.ok().build();
//    }
//
//    @DeleteMapping("/removeCart")
//    public ResponseEntity<Void> removeFromCart( User user, @RequestParam Long productId) {
//        cartService.removeFromCart(user, productId);
//        return ResponseEntity.ok().build();
//    }
//
//    @PostMapping("/checkout")
//    public ResponseEntity<Void> checkout( User user) {
//        cartService.checkout(user);
//        return ResponseEntity.ok().build();
//    }
//}


