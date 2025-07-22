package com.example.authspring.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Запрос на подтверждение восстановления пароля")
public class PasswordResetConfirmRequest {
    @NotBlank(message = "Email должен быть указан")
    @Email(message = "Email должен быть корректным")
    @Schema(description = "E-mail пользователя")
    private String email;

    @JsonAlias({"resetCode", "confirmationCode"})
    @NotBlank(message = "Код восстановления не может быть пустым")
    @Size(min = 4, max = 4, message = "Код восстановления должен состоять из 4 символов")
    @Schema(description = "Код восстановления")
    private String resetCode;

    @NotBlank(message = "Новый пароль не может быть пустым")
    @Size(min = 8, message = "Новый пароль должен содержать минимум 8 символов")
    @Schema(description = "Новый пароль")
    private String newPassword;
}
