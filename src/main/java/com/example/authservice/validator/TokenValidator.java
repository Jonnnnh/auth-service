package com.example.authservice.validator;

import com.example.authservice.exception.TokenValidationException;

public interface TokenValidator {

    String extractSubject(String token) throws TokenValidationException;
}
