package com.example.authspring.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Запрос на регистрацию пользователя")
public class RegistrationRequest {
    @Schema(description = "Логин пользователя")
    @NotBlank(message = "Username не может быть пустым")
    @Size(min = 3, max = 50, message = "Username должен быть от 3 до 50 символов")
    private String username;

    @Schema(description = "E-mail пользователя")
    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Неверный формат e-mail")
    private String email;

    @Schema(description = "Пароль пользователя")
    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 8, message = "Пароль должен содержать минимум 8 символов")
    private String password;
}
