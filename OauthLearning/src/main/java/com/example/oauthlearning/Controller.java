package com.example.oauthlearning;

import com.example.oauthlearning.security.TokenService;
import com.example.oauthlearning.security.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    private final TokenService tokenService;

    public Controller(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/hello/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String helloAdmin() {
        return "hello admin";
    }

    @PostMapping("/hello/user")
    public String helloUser(@RequestBody(required = true) User user) {
        return tokenService.generateToken(user);
    }
}
