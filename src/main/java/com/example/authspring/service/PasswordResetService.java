package com.example.authspring.service;

public interface PasswordResetService {
    void initiatePasswordReset(String email);

    void confirmPasswordReset(String email, String resetCode, String newPassword);
}
