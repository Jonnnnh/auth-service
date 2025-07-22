package com.example.authspring.service.impl;

import com.example.authspring.dto.request.RegistrationRequest;
import com.example.authspring.dto.response.RegistrationResponse;
import com.example.authspring.enums.ConfirmationAction;
import com.example.authspring.enums.Role;
import com.example.authspring.event.ConfirmationCodeResentEvent;
import com.example.authspring.exception.BadRequestException;
import com.example.authspring.model.User;
import com.example.authspring.repository.UserRepository;
import com.example.authspring.service.ConfirmationService;
import com.example.authspring.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
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
    private final ConfirmationService confirmationService;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public RegistrationResponse register(RegistrationRequest request) {
        log.info("Регистрация пользователя с email: {}", request.getEmail());
        String normalizedEmail = request.getEmail().trim().toLowerCase(Locale.ROOT);
        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new BadRequestException("Пользователь с таким email уже зарегистрирован");
        }
        User user = User.builder()
                .username(request.getUsername())
                .email(normalizedEmail)
                .roles(Set.of(Role.GUEST))
                .isVerified(false)
                .isActive(true)
                .build();
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user = userRepository.save(user);
        log.info("Пользователь сохранён с id={}", user.getId());
        var confirmation = confirmationService.createConfirmationFor(user, ConfirmationAction.REGISTRATION);
        log.info("Создана запись Confirmation(id={}), код={}",
                confirmation.getId(), confirmation.getConfirmationCode());
        eventPublisher.publishEvent(new ConfirmationCodeResentEvent(
                user, confirmation.getConfirmationCode(), ConfirmationAction.REGISTRATION
        ));
        log.info("Опубликовано событие ConfirmationCodeResentEvent для {}", user.getEmail());
        log.info("JWT сформирован для {}", user.getEmail());
        return new RegistrationResponse(user.getId(), "Регистрация прошла успешно. Проверьте почту для подтверждения");
    }
}
