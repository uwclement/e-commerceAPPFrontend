package com.ecommerce.service;

import com.ecommerce.model.Cart;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;

import java.util.List;

public interface CartService {
    List<Cart> getAllCarts();
    Cart getCartsById(Long id);
    Cart saveCart(Cart cart);
    Cart getCartByEmailAndProduct(String email, Product product);
    List<Cart> getCartByEmail(String  email);
    Cart deleteCart(Product product);
    String deleteCartByEmail(String email,Product product);
    Cart updateCartItemQuantity(Long id, int quantity);

}
