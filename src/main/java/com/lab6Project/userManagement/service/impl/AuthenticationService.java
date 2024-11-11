package com.lab6Project.userManagement.service.impl;

import com.lab6Project.userManagement.utils.SecurityUtils;

import com.lab6Project.userManagement.dto.JwtResponse;
import com.lab6Project.userManagement.dto.LoginRequest;
import com.lab6Project.userManagement.dto.SignUpRequest;
import com.lab6Project.userManagement.entities.UserEntity;
import com.lab6Project.userManagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final UserServiceImpl userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public JwtResponse signup(SignUpRequest request) throws BadRequestException, UsernameNotFoundException {

        // Terminate the signup process when a user is found in db.
        if (userService.existsByEmail(request.getEmail())) {
            var emptyJwt = jwtService.returnEmptyToken();
            return JwtResponse.builder().token(emptyJwt).build();
        }

        // Declare and initialize a new userEntity instance from request data.
        //System.out.println(request);
        var user = UserEntity
                .builder() // Use static inner class, Builder to create an instance of the user
                .name(request.getName())
                .surname(request.getSurname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("ROLE_USER")
                .build();

        // Save the new user
        user = userService.save(user);
        // Generate a new jwt for the user
        var jwt = jwtService.generateToken(user);

        // Initialize a jwt response, set the jwt and actually build it to compact URL-safe
        return JwtResponse.builder().token(jwt).build();
    }

    //public JwtResponse login(LoginRequest request) throws BadRequestException {
    public JwtResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        /*
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password provided."));
        */

        var user = SecurityUtils.getCurrentUser();
        if (user == null) {
            var emptyJwt = jwtService.returnEmptyToken();
            return JwtResponse.builder().token(emptyJwt).build();
        }

        var jwt = jwtService.generateToken(user);

        return JwtResponse.builder().token(jwt).build();
    }
}