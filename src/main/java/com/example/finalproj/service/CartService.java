//package com.example.finalproj.service;
//
//import com.example.finalproj.entity.Cart;
//import com.example.finalproj.entity.CartItem;
//import com.example.finalproj.entity.Product;
//import com.example.finalproj.entity.User;
//import com.example.finalproj.repository.CartItemRepository;
//import com.example.finalproj.repository.CartRepository;
//import com.example.finalproj.repository.ProductRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class CartService {
//
//    @Autowired
//    private CartRepository cartRepository;
//
//    @Autowired
//    private CartItemRepository cartItemRepository;
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    public void addToCart(User user, Long productId, int quantity) {
//        Cart cart = cartRepository.findByUserAndIsOrderedFalse(user)
//                .orElseGet(() -> new Cart(user));
//        Product product = productRepository.findById(Math.toIntExact(productId))
//                .orElseThrow(() -> new RuntimeException("Product not found"));
//
//        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product)
//                .orElse(new CartItem(cart, product));
//
//        cartItem.setQuantity(cartItem.getQuantity() + quantity);
//
//        cart.getItems().add(cartItem);
//        cartRepository.save(cart);
//    }
//
//    public List<CartItem> getCartItems(User user) {
//        Cart cart = cartRepository.findByUserAndIsOrderedFalse(user)
//                .orElseThrow(() -> new RuntimeException("Cart not found"));
//        return cart.getItems();
//    }
//    public void updateCartItemQuantity(User user, Long productId, int quantity) {
//        Cart cart = cartRepository.findByUserAndIsOrderedFalse(user)
//                .orElseThrow(() -> new RuntimeException("Cart not found"));
//
//        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product)
//                .orElseThrow(() -> new RuntimeException("Cart item not found"));
//
//        cartItem.setQuantity(quantity);
//        cartItemRepository.save(cartItem);
//    }
//
//    public void removeFromCart(User user, Long productId) {
//        Cart cart = cartRepository.findByUserAndIsOrderedFalse(user)
//                .orElseThrow(() -> new RuntimeException("Cart not found"));
//
//        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product)
//                .orElseThrow(() -> new RuntimeException("Cart item not found"));
//
//        cart.getItems().remove(cartItem);
//        cartItemRepository.delete(cartItem);  // Удаление из базы данных
//    }

//    public void checkout(User user) {
//        Cart cart = cartRepository.findByUserAndIsOrderedFalse(user)
//                .orElseThrow(() -> new RuntimeException("Cart not found"));
//
//        cart.setOrdered(true);  // Помечаем корзину как заказанную
//        cartRepository.save(cart);
//
//        // Логика для создания заказа
//    }


//}
