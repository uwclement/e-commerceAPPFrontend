package com.ecommerce.service.impl;

import com.ecommerce.dto.UserDto;
import com.ecommerce.model.Role;
import com.ecommerce.model.User;
import com.ecommerce.repository.RoleRepository;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.service.UserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;


    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveUser(UserDto userDto, String role) {
        User user = new User();
        user.setName(userDto.getFirstName() + " " + userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        // Fetch the role based on the selected role string
        Role userRole = roleRepository.findByName(role);
        if (userRole == null) {
            // If role does not exist, create it
            userRole = new Role();
            userRole.setName(role);
            roleRepository.save(userRole);
        }

        // Set user's roles
        user.setRoles(Collections.singletonList(userRole));

        userRepository.save(user);
    }


    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map((user) -> convertEntityToDto(user))
                .collect(Collectors.toList());
    }


    private UserDto convertEntityToDto(User user){
        UserDto userDto = new UserDto();
        String[] name = user.getName().split(" ");
        userDto.setFirstName(name[0]);
        userDto.setLastName(name[1]);
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    private Role checkRoleExist() {
        Role role = new Role();
        role.setName("ROLE_ADMIN");
        return roleRepository.save(role);
    }

    @Override
    public boolean authenticateUser(UserDto userDto) {
        User user = userRepository.findByEmail(userDto.getEmail());
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + userDto.getEmail());
        }

        String encodedPassword = user.getPassword();
        boolean isPasswordCorrect = passwordEncoder.matches(userDto.getPassword(), encodedPassword);
        if (!isPasswordCorrect) {
            System.out.println("Incorrect password for email: " + userDto.getEmail());
        }

        return isPasswordCorrect;
    }
}
