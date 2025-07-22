package com.example.authspring.service;

import com.example.authspring.enums.ConfirmationAction;
import com.example.authspring.model.Confirmation;
import com.example.authspring.model.User;

import java.util.List;
import java.util.Optional;

public interface ConfirmationService {
    Confirmation createConfirmationFor(User user, ConfirmationAction action);

    Confirmation validateAndUse(String email, String code, ConfirmationAction action);

    List<Confirmation> deleteExpired(ConfirmationAction action);

    Optional<Confirmation> findActiveByEmail(String email, ConfirmationAction action);

    void validateOnly(String email, String code, ConfirmationAction action);
}
