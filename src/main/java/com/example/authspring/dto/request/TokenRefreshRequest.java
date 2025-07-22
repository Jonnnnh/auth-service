package com.example.authspring.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Запрос на обновление токена доступа")
public class TokenRefreshRequest {
    @Schema(description = "Refresh-токен для получения нового access-токена", example = "eyJhbGciOi...")
    @NotBlank(message = "RefreshToken не может быть пустым")
    private String refreshToken;
}
