package com.ecommerce.service;

import com.ecommerce.dto.UserDto;
import com.ecommerce.model.Role;
import com.ecommerce.model.User;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto, String role);

    User findByEmail(String email);

    List<UserDto> findAllUsers();

     boolean authenticateUser(UserDto userDto);
}
