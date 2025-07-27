package com.example.authservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class TokenValidationException extends AbstractException {
    public TokenValidationException(String message) {
        super(message, HttpStatus.UNAUTHORIZED.value());
    }
}
