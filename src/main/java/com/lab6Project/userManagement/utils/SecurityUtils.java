package com.lab6Project.userManagement.utils;

import com.lab6Project.userManagement.entities.UserEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    // Method to retrieve the authenticated User from the security context
    //public static UserDetails getCurrentUser() { // If you have a custom User class you can cast the
        // to your custom user class
    @Nullable
    public static UserEntity getCurrentUser() {

        // Retrieve the authentication object from the security context
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            // Return the authenticated User (cast it to UserDetails or custom User extended entity)
            return (UserEntity) authentication.getPrincipal();
        }

        return null; // Return null if no authenticated user is found
    }

    @Nullable
    public static String getUsername() {
        UserEntity user = getCurrentUser();
        if (user != null) {
            return user.getUsername(); // Return the username of the authenticated user
        }
        return null;
    }
}