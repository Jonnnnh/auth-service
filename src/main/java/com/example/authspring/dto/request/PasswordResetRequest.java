package com.example.authspring.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Запрос на восстановление пароля")
public class PasswordResetRequest {
    @NotBlank(message = "E-mail не может быть пустым")
    @Email(message = "Неверный формат e-mail")
    @Schema(description = "E-mail пользователя")
    private String email;
}
