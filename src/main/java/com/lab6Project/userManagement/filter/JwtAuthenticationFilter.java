package com.lab6Project.userManagement.filter;

import com.lab6Project.userManagement.entities.UserEntity;
import com.lab6Project.userManagement.service.impl.JwtService;
import com.lab6Project.userManagement.service.impl.UserServiceImpl;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    //@Autowired
    private final JwtService jwtService;
    //@Autowired
    private final UserServiceImpl userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        if (StringUtils.isEmpty(authHeader)) {
            filterChain.doFilter(request, response);
            return;
        }

        // By default, JWT headers start as "Bearer XXXX", so extract it from position 7
        // , so from position 7
        jwt = authHeader.substring(7);
        // Show the jwt at console
        log.info("JWT -> {}", jwt);

        // Extract the username (email) from the JWT
        userEmail = jwtService.extractUserName(jwt);

        if (!StringUtils.isEmpty(userEmail) && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Use the userService directly as it implements UserDetailsService
            //UserDetails userDetails = userService.loadUserByUsername(userEmail);
            UserEntity userDetails = userService.loadUserByUsername(userEmail);

            if (jwtService.isTokenValid(jwt, userDetails)) {
                log.info("User - {}", userDetails);
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(authToken);
                SecurityContextHolder.setContext(context);
            }
        }
        filterChain.doFilter(request, response);
    }
}
