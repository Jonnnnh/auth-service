package com.example.authspring.service;

public interface JwtService {
    String generateToken(String email);

    boolean validateToken(String token);

    String getUsernameFromToken(String token);
}
