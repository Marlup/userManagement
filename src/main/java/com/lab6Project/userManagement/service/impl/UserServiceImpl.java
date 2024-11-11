package com.lab6Project.userManagement.service.impl;

import com.lab6Project.userManagement.entities.UserEntity;
import com.lab6Project.userManagement.repository.UserRepository;
import com.lab6Project.userManagement.service.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    //@Autowired
    private final UserRepository userRepository;

    // Saves a new user record.
    public UserEntity save(UserEntity newUser) {
        return userRepository.save(newUser);
    }

    // Check whether the user exists by email.
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    // Implement the loadUserByUsername method from UserDetailsService
    @Override
    //public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    public UserEntity loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }


}