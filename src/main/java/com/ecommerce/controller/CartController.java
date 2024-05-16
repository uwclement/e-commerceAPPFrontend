package com.ecommerce.controller;


import com.ecommerce.model.Cart;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import com.ecommerce.service.CartService;
import com.ecommerce.service.ProductService;
import com.ecommerce.service.UserService;
import com.ecommerce.service.impl.CartServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpRequest;
import java.util.List;
import java.util.Objects;

@Controller
@SessionAttributes({"userId","userEmail","userName"})
@RequestMapping("/cart")
public class CartController {

    private final CartServiceImpl cartServiceImpl;
    private final ProductService productService;
    private final UserService userService;
    private final HttpServletRequest httpServletRequest;

    public CartController(CartServiceImpl cartServiceImpl, ProductService productService, UserService userService, HttpServletRequest httpServletRequest) {
        this.cartServiceImpl = cartServiceImpl;
        this.productService = productService;
        this.userService = userService;
        this.httpServletRequest = httpServletRequest;
    }

    @GetMapping("/add/{productId}")
    public String addToCart(@PathVariable("productId") Long productId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userService.findByEmail(email);
        Product product = productService.getProductById(productId);

        HttpSession session = httpServletRequest.getSession();
        Long userId = (Long) session.getAttribute("userId");
        String userEmail = (String) session.getAttribute("userEmail");
        String name = (String) session.getAttribute("userName");
        Cart cart = cartServiceImpl.addOrUpdateCart(userEmail, product);
        Double TotalAmount = cartServiceImpl.getTotalAmountByEmail(userEmail);
        session.setAttribute("Amount",TotalAmount);
        System.out.println(TotalAmount);
        return "redirect:/home";
    }

    @GetMapping
    public String viewCart(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userService.findByEmail(email);
        HttpSession session = httpServletRequest.getSession();

        String userEmail = (String) session.getAttribute("userEmail");
        List<Cart> cartItems = cartServiceImpl.getCartByEmail(userEmail);
        model.addAttribute("cartItems", cartItems);

        return "cart";
    }

    @GetMapping("/all")
    public String viewAllCarts(Model model) {
        List<Cart> carts = cartServiceImpl.getAllCarts();
        model.addAttribute("carts", carts);

        return "cart-list";
    }



    @GetMapping("/checkout")
    public String checkout() {
        HttpSession session = httpServletRequest.getSession();
        String userEmail = (String) session.getAttribute("userEmail");
        User user = userService.findByEmail(userEmail);

        cartServiceImpl.deleteCartByEmail(userEmail);

        return "redirect:/home";
    }


    @PostMapping("/update/{cartItemId}")
    public String updateCartItem(@PathVariable("cartItemId") Long cartItemId, @RequestParam("quantity") int quantity) {
       try {
           cartServiceImpl.updateCartItemQuantity(cartItemId, quantity);
           System.out.println(quantity);
           System.out.println(cartItemId);

           return "redirect:/cart";

       }catch (Exception ex)
       {
           ex.printStackTrace();
       }
       return null;
    }

    @GetMapping("/delete/{cartId}")
    public String deleteCartItem(@PathVariable Long cartId) {
       try {
           cartServiceImpl.deleteCart(cartId);
           return "redirect:/cart";
       }catch (Exception ex)
       {
           ex.printStackTrace();
       }
       return null;
    }



}
