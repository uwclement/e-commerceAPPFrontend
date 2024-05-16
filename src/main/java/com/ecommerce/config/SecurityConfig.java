package com.ecommerce.config;

import com.ecommerce.security.UserDetailsServiceSec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private UserDetailsServiceSec userDetailsService;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests((authorize) ->
                        authorize.requestMatchers("/register/**").permitAll()
                                .requestMatchers("/login").permitAll()
                                .requestMatchers("/login/**").permitAll()
//                                .requestMatchers("/products/**").hasRole("ADMIN")
                                .requestMatchers("/products/**").permitAll()
//                                .requestMatchers("/orders/**").hasRole("ADMIN")
                                .requestMatchers("/orders/**").permitAll()
//                                .requestMatchers("/home/**").hasRole("USER")
                                .requestMatchers("/home/**").permitAll()
                                .requestMatchers("/cart/**").permitAll()
//                                .requestMatchers("/cart/**").hasRole("USER")
                                .requestMatchers("/image/**").permitAll()
                                .requestMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**").permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin(
                        form -> form
                                .loginPage("/login")
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/home", true)
                                .successHandler((request, response, authentication) -> {
                                    String role = authentication.getAuthorities().iterator().next().getAuthority();
                                    System.out.println(role);
                                    if (role.equals("ADMIN")) {
                                        response.sendRedirect("/products");
                                    } else if (role.equals("USER")) {
                                        response.sendRedirect("/home");
                                    } else {
                                        // Handle the case where the user has a different role
                                        response.sendRedirect("/access-denied");
                                    }
                                })
                                .permitAll()
                );
        return http.build();
    }
}
