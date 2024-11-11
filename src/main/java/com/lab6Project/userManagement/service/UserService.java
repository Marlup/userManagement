package com.lab6Project.userManagement.service;

import com.lab6Project.userManagement.entities.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    UserEntity save(UserEntity newUser);
    boolean existsByEmail(String email);
    UserEntity loadUserByUsername(String username);
}
