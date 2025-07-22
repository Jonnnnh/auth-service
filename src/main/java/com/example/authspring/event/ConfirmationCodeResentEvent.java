package com.example.authspring.event;

import com.example.authspring.enums.ConfirmationAction;
import com.example.authspring.model.User;

public record ConfirmationCodeResentEvent(User user, String confirmationCode, ConfirmationAction action) {}

