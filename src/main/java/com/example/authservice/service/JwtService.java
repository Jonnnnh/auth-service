package com.example.authservice.service;

public interface JwtService {
    String generateToken(String email);

    boolean validateToken(String token);

    String getUsernameFromToken(String token);
}
