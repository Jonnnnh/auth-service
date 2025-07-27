package com.example.authspring.dto.response;

import com.example.authspring.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Dto пользователя для отображения")
public class UserDto {
    @Schema(description = "Уникальный идентификатор пользователя", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID id;

    @Schema(description = "E-mail пользователя", example = "uses@example.com")
    private String email;

    @Schema(description = "Логин пользователя", example = "user")
    private String username;

    @Schema(description = "Набор ролей пользователя")
    private Set<Role> roles;

    @Schema(description = "Дата и время создания пользователя", example = "2025-07-21T15:30:00Z")
    private Instant createdAt;

    @Schema(description = "Дата и время последнего обновления пользователя", example = "2025-07-22T10:45:00Z")
    private Instant updatedAt;
}
