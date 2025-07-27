package com.example.authspring.exception;

import org.springframework.http.HttpStatus;

public class TokenValidationException extends AbstractException {
    public TokenValidationException(String message) {
        super(message, HttpStatus.UNAUTHORIZED.value());
    }
}
