package com.example.authspring.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Ответ при успешной аутентификации пользователя")
public class AuthenticationResponse {
    @Schema(description = "jwt access-токен для авторизации")
    private String accessToken;

    @Schema(description = "jwt refresh-токен для обновления access-токена")
    private String refreshToken;

    @Schema(description = "Дата и время истечения действия refresh-токена")
    private Instant refreshTokenExpiry;

    @Schema(description = "Информация о пользователе")
    private UserDto user;
}
