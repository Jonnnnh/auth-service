package com.example.authspring.service.impl;

import com.example.authspring.enums.ConfirmationAction;
import com.example.authspring.event.ConfirmationCodeResentEvent;
import com.example.authspring.event.UserVerifiedEvent;
import com.example.authspring.exception.BadRequestException;
import com.example.authspring.model.Confirmation;
import com.example.authspring.model.User;
import com.example.authspring.repository.UserRepository;
import com.example.authspring.service.ConfirmationEmailService;
import com.example.authspring.service.ConfirmationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConfirmationEmailServiceImpl implements ConfirmationEmailService {

    private final ConfirmationService confirmationService;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;


    @Override
    @Transactional
    public void confirmEmail(String email, String confirmationCode) {
        log.info("Подтверждаем email={} с кодом={}", email, confirmationCode);
        Confirmation confirmation = confirmationService.validateAndUse(email, confirmationCode, ConfirmationAction.REGISTRATION);
        User user = confirmation.getUser();
        user.setIsVerified(true);
        userRepository.save(user);
        log.info("Пользователь {} успешно верифицирован, публикуем событие", email);
        eventPublisher.publishEvent(new UserVerifiedEvent(user));
    }

    @Override
    @Transactional
    public void resendConfirmationCode(String email) {
        log.info("Повторная генерация кода для {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Пользователь с email " + email + " не найден"));
        if (Boolean.TRUE.equals(user.getIsVerified())) {
            throw new BadRequestException("Email уже подтверждён");
        }
        confirmationService.findActiveByEmail(email, ConfirmationAction.REGISTRATION)
                .ifPresent(conf -> {
                    long minutesLeft = Duration.between(LocalDateTime.now(), conf.getExpiresAt()).toMinutes();
                    throw new BadRequestException(
                            "Текущий код ещё действителен (" + minutesLeft + " мин) " + "Попробуйте позже или используйте старый код"
                    );
                });
        Confirmation newConfirmation = confirmationService.createConfirmationFor(user, ConfirmationAction.REGISTRATION);
        log.info("Сгенерирован новый код {}, публикуем событие", newConfirmation.getConfirmationCode());
        eventPublisher.publishEvent(
                new ConfirmationCodeResentEvent(user, newConfirmation.getConfirmationCode(), ConfirmationAction.REGISTRATION)
        );
    }
}
