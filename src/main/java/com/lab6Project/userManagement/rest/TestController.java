package com.lab6Project.userManagement.rest;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/noAuth")
    public String noAuth() {
        return "Anyone can see this";
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('USER')")
    public String user() {
        return "Only users can see this";
    }

    @GetMapping("/admins")
    @PreAuthorize("hasRole('ADMIN')")
    public String admin() {
        return "Only admins can see this";
    }

    @GetMapping("/roles")
    @PreAuthorize("hasRole('ADMIN', 'USER')")
    public String role() {
        return "Either users or admins can see this";
    }
}
