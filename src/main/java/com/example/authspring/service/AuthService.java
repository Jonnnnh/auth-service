package com.example.authspring.service;

import com.example.authspring.dto.request.AuthenticationRequest;
import com.example.authspring.dto.request.TokenRefreshRequest;
import com.example.authspring.dto.response.AuthenticationResponse;
import com.example.authspring.dto.response.MessageResponse;
import com.example.authspring.dto.response.TokenRefreshResponse;

public interface AuthService {
    AuthenticationResponse login(AuthenticationRequest request);

    TokenRefreshResponse refresh(TokenRefreshRequest request);

    MessageResponse logout();
}
