package com.example.authspring.service.impl;

import com.example.authspring.dto.request.AuthenticationRequest;
import com.example.authspring.dto.request.TokenRefreshRequest;
import com.example.authspring.dto.response.AuthenticationResponse;
import com.example.authspring.dto.response.MessageResponse;
import com.example.authspring.dto.response.TokenRefreshResponse;
import com.example.authspring.dto.response.UserDto;
import com.example.authspring.exception.ResourceNotFoundException;
import com.example.authspring.exception.TokenValidationException;
import com.example.authspring.mapper.UserMapper;
import com.example.authspring.model.RefreshToken;
import com.example.authspring.model.User;
import com.example.authspring.provider.JweTokenProvider;
import com.example.authspring.repository.UserRepository;
import com.example.authspring.service.AuthService;
import com.example.authspring.service.RefreshTokenService;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JweTokenProvider jweTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public AuthenticationResponse login(AuthenticationRequest request) {
        String normalized = request.getEmail().trim().toLowerCase(Locale.ROOT);
        log.info("Попытка входа пользователя с email='{}'", normalized);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        normalized,
                        request.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("Успешная аутентификация для email='{}'", normalized);

        User user = userRepository.findByEmail(normalized)
                .orElseThrow(() -> {
                    log.error("Пользователь не найден после аутентификации: email='{}'", normalized);
                    return new ResourceNotFoundException("Пользователь");
                });

        String accessToken;
        try {
           accessToken = jweTokenProvider.generateEncryptedToken(user.getEmail());
            log.info("Сформирован JWE-токен для пользователя с id='{}'", user.getId());
        } catch (JOSEException e) {
            log.error("Ошибка при генерации JWE-токена для email='{}'", normalized, e);
            throw new TokenValidationException("Не удалось создать JWE-токен");
        }
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
        log.debug("Создан refresh-токен с id='{}' для пользователя '{}'", refreshToken.getId(), user.getEmail());

        UserDto userDto = userMapper.toDto(user);
        log.info("Процесс входа завершён для email='{}'", normalized);
        return new AuthenticationResponse(
                accessToken,
                refreshToken.getToken(),
                refreshToken.getExpiryDate(),
                userDto
        );
    }

    @Override
    public TokenRefreshResponse refresh(TokenRefreshRequest request) {
        log.info("Запрос обновления access-токена по refresh-токену='{}'", request.getRefreshToken());
        RefreshToken existing = refreshTokenService.verifyAndGet(request.getRefreshToken());
        log.info("Refresh-токен проверен для пользователя '{}'", existing.getUser().getEmail());

        String newAccessToken;
        try {
            newAccessToken = jweTokenProvider.generateEncryptedToken(existing.getUser().getEmail());
            log.info("Сформирован новый JWE-токен для пользователя '{}'", existing.getUser().getEmail());
        } catch (JOSEException e) {
            log.error("Ошибка при генерации нового JWE-токена для пользователя '{}'",
                    existing.getUser().getEmail(), e);
            throw new TokenValidationException("Не удалось создать новый JWE-токен");
        }
        RefreshToken rotated = refreshTokenService.rotateRefreshToken(existing);
        log.debug("Обновлён refresh-токен с id='{}' для пользователя '{}'",rotated.getId(), existing.getUser().getEmail());

        log.info("Процесс обновления токена завершён для пользователя '{}'", existing.getUser().getEmail());
        return new TokenRefreshResponse(
                newAccessToken,
                rotated.getToken(),
                rotated.getExpiryDate()
        );
    }

    @Override
    public MessageResponse logout() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Попытка выхода пользователя '{}'", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь", "email", email));
        refreshTokenService.revokeAllForUser(user);
        SecurityContextHolder.clearContext();
        log.info("Пользователь '{}' вышел и все refresh-токены отозваны", email);
        return new MessageResponse("Вы успешно вышли и все refresh-токены отозваны");
    }
}
