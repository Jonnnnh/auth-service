package com.example.authspring.service;

public interface ConfirmationEmailService {
    void confirmEmail(String email, String confirmationCode);

    void resendConfirmationCode(String email);
}
