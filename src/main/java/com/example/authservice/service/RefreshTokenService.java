package com.example.authservice.service;

import com.example.authservice.model.RefreshToken;
import com.example.authservice.model.User;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(User user);
    RefreshToken verifyAndGet(String token);
    void revoke(RefreshToken token);
    void revokeAllForUser(User user);
    RefreshToken rotateRefreshToken(RefreshToken refreshToken);
}
