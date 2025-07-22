package com.example.authspring.service;

import com.example.authspring.model.RefreshToken;
import com.example.authspring.model.User;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(User user);
    RefreshToken verifyAndGet(String token);
    void revoke(RefreshToken token);
    void revokeAllForUser(User user);
    RefreshToken rotateRefreshToken(RefreshToken refreshToken);
}
