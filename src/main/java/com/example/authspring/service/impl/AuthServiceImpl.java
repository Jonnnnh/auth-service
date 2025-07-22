package com.example.authspring.service.impl;

import com.example.authspring.dto.request.AuthenticationRequest;
import com.example.authspring.dto.request.TokenRefreshRequest;
import com.example.authspring.dto.response.AuthenticationResponse;
import com.example.authspring.dto.response.MessageResponse;
import com.example.authspring.dto.response.TokenRefreshResponse;
import com.example.authspring.dto.response.UserDto;
import com.example.authspring.exception.ResourceNotFoundException;
import com.example.authspring.mapper.UserMapper;
import com.example.authspring.model.RefreshToken;
import com.example.authspring.model.User;
import com.example.authspring.repository.UserRepository;
import com.example.authspring.service.AuthService;
import com.example.authspring.service.JwtService;
import com.example.authspring.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public AuthenticationResponse login(AuthenticationRequest request) {
        String normalized = request.getEmail().trim().toLowerCase(Locale.ROOT);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        normalized,
                        request.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = userRepository.findByEmail(normalized)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь"));
        String accessToken = jwtService.generateToken(user.getEmail());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
        UserDto userDto = userMapper.toDto(user);
        return new AuthenticationResponse(
                accessToken,
                refreshToken.getToken(),
                refreshToken.getExpiryDate(),
                userDto
        );
    }

    @Override
    public TokenRefreshResponse refresh(TokenRefreshRequest request) {
        RefreshToken existing = refreshTokenService.verifyAndGet(request.getRefreshToken());

        String newAccessToken = jwtService.generateToken(existing.getUser().getEmail());
        RefreshToken rotated = refreshTokenService.rotateRefreshToken(existing);

        return new TokenRefreshResponse(
                newAccessToken,
                rotated.getToken(),
                rotated.getExpiryDate()
        );
    }

    @Override
    public MessageResponse logout() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь", "email", email));
        refreshTokenService.revokeAllForUser(user);
        SecurityContextHolder.clearContext();
        return new MessageResponse("Вы успешно вышли и все refresh-токены отозваны");
    }
}
