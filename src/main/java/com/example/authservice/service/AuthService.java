package com.example.authservice.service;

import com.example.authservice.dto.request.AuthenticationRequest;
import com.example.authservice.dto.request.TokenRefreshRequest;
import com.example.authservice.dto.response.AuthenticationResponse;
import com.example.authservice.dto.response.MessageResponse;
import com.example.authservice.dto.response.TokenRefreshResponse;

public interface AuthService {
    AuthenticationResponse login(AuthenticationRequest request);

    TokenRefreshResponse refresh(TokenRefreshRequest request);

    MessageResponse logout();
}
