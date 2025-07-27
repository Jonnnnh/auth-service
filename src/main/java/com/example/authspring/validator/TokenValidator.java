package com.example.authspring.validator;

import com.example.authspring.exception.TokenValidationException;

public interface TokenValidator {

    String extractSubject(String token) throws TokenValidationException;
}
