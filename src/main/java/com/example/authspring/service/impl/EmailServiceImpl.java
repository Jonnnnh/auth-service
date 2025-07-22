package com.example.authspring.service.impl;

import com.example.authspring.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendConfirmationEmail(String email, String confirmationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Подтверждение регистрации");
        String text = String.format(
                "Здравствуйте!\n\nВаш код подтверждения для регистрации:", confirmationCode
        );
        message.setText(text);
        mailSender.send(message);
    }

    @Override
    public void sendPasswordResetEmail(String email, String resetCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Запрос на восстановление пароля");
        String text = String.format(
                "Здравствуйте!\n\nВаш код для восстановления пароля: %s\n" +
                        "Если вы не запрашивали восстановление пароля, просто проигнорируйте это письмо.\n\n", resetCode
        );
        message.setText(text);
        mailSender.send(message);
    }

}
