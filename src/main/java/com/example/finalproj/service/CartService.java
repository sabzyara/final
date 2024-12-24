package com.example.finalproj.service;

import com.example.finalproj.entity.Product;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;


@AllArgsConstructor
@Service
public class CartService {

    private List<Product> cart = new ArrayList<>();

    public void addProductToCart(Product product) {
        cart.add(product);
    }

    public List<Product> getCartItems() {
        return cart;
    }

    public void clearCart() {
        cart.clear();
    }
}

