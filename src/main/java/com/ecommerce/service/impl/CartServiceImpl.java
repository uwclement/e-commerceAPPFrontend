package com.ecommerce.service.impl;

import com.ecommerce.model.Cart;
import com.ecommerce.model.Orders;
import com.ecommerce.model.Product;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.service.CartService;
import com.ecommerce.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final OrderService orderService;



    public CartServiceImpl(CartRepository cartRepository, OrderService orderService) {
        this.cartRepository = cartRepository;
        this.orderService = orderService;
    }

    @Override
    public List<Cart> getAllCarts() {
        return cartRepository.findAll();
    }

    @Override
    public Cart getCartsById(Long id) {
        return cartRepository.findById(id).orElse(null);
    }

    @Override
    public Cart saveCart(Cart cart) {
        return cartRepository.save(cart);
    }

    @Override
    public List<Cart> getCartByEmail(String email) {
        return cartRepository.findByEmail(email);
    }

    @Override
    public Cart deleteCart(Product product) {
        return null;
    }

    @Override
    public String deleteCartByEmail(String email, Product product) {
        cartRepository.deleteCartBy(email, product);
        return "Cart Item Deleted Successfully";
    }

    @Override
    public Cart getCartByEmailAndProduct(String email, Product product) {
        return cartRepository.findByEmailAndProduct(email, product);
    }

    @Transactional
    public void deleteCartByEmail(String email) {
        try {
            List<Cart> cartItems = cartRepository.findByEmail(email);
                for (Cart cart : cartItems) {
                    // Save the cart item as an order before deleting it
                    Orders order = new Orders();
                    order.setProduct(cart.getProduct());
                    order.setEmail(cart.getEmail());
                    order.setQuantity(cart.getQuantity());
                    order.setTotalPrice(cart.getTotalPrice());
                    orderService.save(order);

                    // Delete the cart item
                    cartRepository.delete(cart);
                }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public Cart getCartItemQuantity(Long cartItemId, int quantity) {
        return cartRepository.findByIdAndQuantity(cartItemId,quantity);
    }

    public Cart addOrUpdateCart(String email, Product product) {
        Cart cart = getCartByEmailAndProduct(email, product);
        if (cart == null) {
            cart = new Cart();
            cart.setEmail(email);
            cart.setProduct(product);
            cart.setQuantity(1);
            cart.setTotalPrice(product.getPrice());
        } else {
            cart.setQuantity(cart.getQuantity() + 1);
            cart.setTotalPrice(cart.getTotalPrice()+ product.getPrice());;
        }
        return saveCart(cart);
    }


    public Cart removeOrUpdateCart(String email, Product product) {
        Cart cart = getCartByEmailAndProduct(email, product);
        if (cart == null) {
            cart = new Cart();
            cart.setEmail(email);
            cart.setProduct(product);
            cart.setQuantity(1);
            cart.setTotalPrice(product.getPrice());
        } else {
            cart.setQuantity(cart.getQuantity() - 1);
            cart.setTotalPrice(cart.getTotalPrice()- product.getPrice());;
        }
        return saveCart(cart);
    }

//    public Void deleteOrUpdateCart(String email, Product product) {
////        Cart cart = getCartByEmailAndProduct(email, product);
//        return deleteCartByEmail(email,product);
//    }



    public Double getTotalAmountByEmail(String email) {
        List<Cart> cartItems = getCartByEmail(email);
        Double totalAmount = 0.0;
        for (Cart cart : cartItems) {
            totalAmount += cart.getTotalPrice();
        }
        return totalAmount;
    }



    public Cart updateCartItemQuantity(Long cartItemId, int quantity) {
        Cart cartItem = getCartItemQuantity(cartItemId, quantity);
        if (cartItem != null) {
            cartItem.setQuantity(quantity);
            // Calculate the total price based on the product price and the new quantity
            Product product = cartItem.getProduct();
            double totalPrice = product.getPrice() * quantity;
            cartItem.setTotalPrice(totalPrice);
            return saveCart(cartItem);
        }
        return null;
    }

}
