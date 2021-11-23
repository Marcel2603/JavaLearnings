package com.example.oauthlearning.security;

public interface TokenService {
    String generateToken(User user);

    UserPrincipal parseToken(String token);
}
