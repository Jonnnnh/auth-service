package com.example.authservice.service.impl;

import com.example.authservice.dto.request.RegistrationRequest;
import com.example.authservice.dto.response.RegistrationResponse;
import com.example.authservice.enums.Role;
import com.example.authservice.exception.BadRequestException;
import com.example.authservice.model.User;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public RegistrationResponse register(RegistrationRequest request) {
        log.info("Регистрация пользователя с email: {}", request.getEmail());
        String normalizedEmail = request.getEmail().trim().toLowerCase(Locale.ROOT);
        String normalizedUsername = request.getUsername().trim().toLowerCase(Locale.ROOT);
        if (userRepository.existsByUsername(normalizedUsername)) {
            throw new BadRequestException("Пользователь с таким usrname уже зарегистрирован");
        }
        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new BadRequestException("Пользователь с таким email уже зарегистрирован");
        }
        User user = User.builder()
                .username(normalizedUsername)
                .email(normalizedEmail)
                .roles(Set.of(Role.GUEST))
                .build();
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user = userRepository.save(user);
        log.info("Пользователь сохранён с id={}", user.getId());

        return new RegistrationResponse(user.getId(), "Регистрация прошла успешно");
    }
}
