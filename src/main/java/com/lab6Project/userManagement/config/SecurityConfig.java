package com.lab6Project.userManagement.config;

import com.lab6Project.userManagement.filter.JwtAuthenticationFilter;
import com.lab6Project.userManagement.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserServiceImpl userServiceDetail; // Note inheritance: UserDetailsService ->  UserService -> UserServiceImpl
    private final PasswordEncoder passwordEncoder;

    // Returns the authentication provider which requires user details and password encoder
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticatorProvider = new DaoAuthenticationProvider();
        authenticatorProvider.setUserDetailsService(userServiceDetail);
        authenticatorProvider.setPasswordEncoder(passwordEncoder);

        return authenticatorProvider;
    }

    // Returns an AuthenticationManager given the current Authentication Configuration
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // Set a configuration of disabled CSRF, configures authorization rules to filter certain and rest URLs
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
               .sessionManagement(session -> session
                       .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
               )
                .authorizeHttpRequests(authorize -> authorize
                        //.requestMatchers(HttpMethod.GET, "/test/**").permitAll()
                        //.requestMatchers(HttpMethod.GET, "/api/v1/test/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/login", "/signup").permitAll()
                        .requestMatchers(HttpMethod.GET, "/test/noAuth").permitAll()
                        .requestMatchers(HttpMethod.GET, "/test/user").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/test/admin").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/test/role").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(toH2Console()).permitAll() // to access h2 console
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .headers().frameOptions().disable(); // to allow browser to render the application

                return http.build();
    }
}
