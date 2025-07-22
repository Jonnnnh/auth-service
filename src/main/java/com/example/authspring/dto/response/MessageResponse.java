package com.example.authspring.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Ответ с сообщением")
public class MessageResponse {
    @Schema(description = "Текстовое сообщение")
    private String message;
}
