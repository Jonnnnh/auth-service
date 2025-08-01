package com.example.authservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends AbstractException {
    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST.value());
    }
}