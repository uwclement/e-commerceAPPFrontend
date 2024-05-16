package com.ecommerce.controller;

import com.ecommerce.dto.UserDto;
import com.ecommerce.model.User;
import com.ecommerce.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@Controller
public class AuthController {

    private final HttpServletRequest httpServletRequest;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserService userService, AuthenticationManager authenticationManager, HttpServletRequest httpServletRequest) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.httpServletRequest = httpServletRequest;
    }

    @GetMapping("/index")
    public String home(){
        return "index";
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model){
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto user,
                               @RequestParam("role") String role,
                               BindingResult result,
                               Model model) {
        try {
            User existing = userService.findByEmail(user.getEmail());
            if (existing != null) {
                model.addAttribute("error",  "There is already an account registered with that email");
            }
            if (result.hasErrors()) {
                return "register";
            }
            userService.saveUser(user, role);
            return "redirect:/login";
        } catch (Exception ex) {
            ex.printStackTrace();
            model.addAttribute("error", "Email Exits");
        }
        return "register";
    }

    @PostMapping("/login/save")
    public String login(@Valid @ModelAttribute("user") UserDto user,
                        BindingResult result,
                        Model model) {
        try {
            if (result.hasErrors()) {
                return "login";
            }

            User existingUser = userService.findByEmail(user.getEmail());
            if (existingUser == null) {
                model.addAttribute("error", "User not found with email: " );
                return "login";
            }

            boolean isPasswordCorrect = userService.authenticateUser(user);
            if (!isPasswordCorrect) {
                model.addAttribute("error", "Incorrect password for email: ");
                return "login";
            }

            Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
            Authentication authenticated = authenticationManager.authenticate(authentication);
            SecurityContextHolder.getContext().setAuthentication(authenticated);

            String email = authenticated.getName();
            Long userID = userService.findByEmail(email).getId();
            String name = userService.findByEmail(email).getName();
            String role = authenticated.getAuthorities().iterator().next().getAuthority();

            HttpSession session = httpServletRequest.getSession();
            session.setAttribute("userId", userID);
            session.setAttribute("userEmail", email);
            session.setAttribute("userName", name);
            session.setAttribute("userRole", role);

            if (role.equals("ROLE_ADMIN")) {
                return "redirect:/products";
            } else {
                return "redirect:/home";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            model.addAttribute("error", "An error occurred during authentication");
            return "login";
        }
    }

    @GetMapping("/userProfile")
    public String userProfile(Model model, Authentication authentication) {
        String email = authentication.getName();
        String name = userService.findByEmail(email).getName();
        model.addAttribute("userEmail", email);
        model.addAttribute("userName", name);
        return "userProfile";
    }
}
