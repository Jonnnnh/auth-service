package com.example.authspring.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Ответ при успешной регистрации пользователя")
public class RegistrationResponse {

    @Schema(description = "Идентификатор пользователя", example = "1")
    private UUID userId;

    @Schema(description = "Сообщение", example = "Регистрация успешна. Проверьте свою почту для подтверждения")
    private String message;
}
