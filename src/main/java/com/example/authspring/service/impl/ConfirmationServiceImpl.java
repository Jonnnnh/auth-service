package com.example.authspring.service.impl;

import com.example.authspring.config.confirm.ConfirmationProperties;
import com.example.authspring.enums.ConfirmationAction;
import com.example.authspring.exception.BadRequestException;
import com.example.authspring.model.Confirmation;
import com.example.authspring.model.User;
import com.example.authspring.repository.ConfirmationRepository;
import com.example.authspring.service.ConfirmationService;
import com.example.authspring.until.ConfirmationCodeGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConfirmationServiceImpl implements ConfirmationService {
    private final ConfirmationRepository repo;
    private final ConfirmationProperties props;

    @Override
    @Transactional
    public Confirmation createConfirmationFor(User user, ConfirmationAction action) {
        repo.findFirstByUserEmailAndActionAndUsedFalseOrderByExpiresAtDesc(user.getEmail(), action)
                .ifPresent(repo::delete);
        var code = ConfirmationCodeGenerator.generateNumericCode(props.codeLength());
        var conf = Confirmation.builder()
                .user(user)
                .action(action)
                .confirmationCode(code)
                .expiresAt(LocalDateTime.now().plusMinutes(props.expirationMinutes()))
                .used(false)
                .build();
        return repo.save(conf);
    }

    @Override
    @Transactional
    public Confirmation validateAndUse(String email, String code, ConfirmationAction action) {
        var conf = repo.findFirstByUserEmailAndActionAndUsedFalseOrderByExpiresAtDesc(email, action)
                .orElseThrow(() -> new BadRequestException("Код не найден или уже использован"));
        if (conf.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Срок действия кода истёк");
        }
        if (!conf.getConfirmationCode().equals(code)) {
            throw new BadRequestException("Неверный код подтверждения");
        }
        conf.setUsed(true);
        return repo.save(conf);
    }

    @Override
    @Transactional
    public List<Confirmation> deleteExpired(ConfirmationAction action) {
        var now = LocalDateTime.now();
        var expired = repo.findAllByActionAndUsedFalseAndExpiresAtBefore(action, now);
        repo.deleteAll(expired);
        return expired;
    }

    @Override
    public Optional<Confirmation> findActiveByEmail(String email, ConfirmationAction action) {
        return repo.findFirstByUserEmailAndActionAndUsedFalseOrderByExpiresAtDesc(email, action)
                .filter(c -> c.getExpiresAt().isAfter(LocalDateTime.now()));
    }

    @Override
    public void validateOnly(String email, String code, ConfirmationAction action) {
        Confirmation conf = repo.findFirstByUserEmailAndActionAndUsedFalseOrderByExpiresAtDesc(email, action)
                .orElseThrow(() -> new BadRequestException("Код не найден или уже использован"));
        if (conf.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Срок действия кода истёк");
        }
        if (!conf.getConfirmationCode().equals(code)) {
            throw new BadRequestException("Неверный код подтверждения");
        }
    }
}
