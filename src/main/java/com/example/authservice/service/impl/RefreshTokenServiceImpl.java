package com.example.authservice.service.impl;

import com.example.authservice.config.jwt.JwtConfigProperties;
import com.example.authservice.exception.ResourceNotFoundException;
import com.example.authservice.model.RefreshToken;
import com.example.authservice.model.User;
import com.example.authservice.repository.RefreshTokenRepository;
import com.example.authservice.service.RefreshTokenService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository repo;
    private final JwtConfigProperties jwtProperties;

    @Override
    public RefreshToken createRefreshToken(User user) {
        String token = UUID.randomUUID().toString();
        Instant expiry = Instant.now().plusMillis(jwtProperties.refreshExpiration().toMillis());
        RefreshToken refreshToken = RefreshToken.builder()
                .token(token)
                .user(user)
                .expiryDate(expiry)
                .revoked(false)
                .build();
        return repo.save(refreshToken);
    }

    @Override
    public RefreshToken verifyAndGet(String token) {
        RefreshToken refreshToken = repo.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Refresh токен", token));
        if (refreshToken.isRevoked() || refreshToken.getExpiryDate().isBefore(Instant.now())) {
            throw new JwtException("Refresh токен недействителен или истек");
        }
        return refreshToken;
    }

    @Override
    public void revoke(RefreshToken token) {
        token.setRevoked(true);
        repo.save(token);
    }

    @Override
    public void revokeAllForUser(User user) {
        List<RefreshToken> tokens = repo.findAllByUser(user);
        tokens.forEach(t -> t.setRevoked(true));
        repo.saveAll(tokens);
    }

    @Override
    public RefreshToken rotateRefreshToken(RefreshToken refreshToken) {
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(jwtProperties.refreshExpiration().toMillis()));
        refreshToken.setRevoked(false);
        return repo.save(refreshToken);
    }
}

