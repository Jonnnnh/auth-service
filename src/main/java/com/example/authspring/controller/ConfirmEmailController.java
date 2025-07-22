package com.example.authspring.controller;

import com.example.authspring.dto.request.ConfirmationEmailRequest;
import com.example.authspring.dto.request.ResendConfirmationRequest;
import com.example.authspring.dto.response.ErrorResponse;
import com.example.authspring.dto.response.MessageResponse;
import com.example.authspring.service.ConfirmationEmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "auth", description = "Аутентификация и подтверждение")
public class ConfirmEmailController {

    private final ConfirmationEmailService confirmEmailService;

    @Operation(
            summary = "Подтверждение e-mail",
            description = "Проверяет код, отправленный на e-mail пользователя при регистрации"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Почта успешно подтверждена",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MessageResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неверный или просроченный код подтверждения",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PostMapping("/confirm-email")
    public ResponseEntity<MessageResponse> confirmEmail(
            @Valid @RequestBody ConfirmationEmailRequest request) {
        confirmEmailService.confirmEmail(request.getEmail(), request.getConfirmationCode());
        return ResponseEntity.ok(new MessageResponse("Почта успешно подтверждена"));
    }

    @Operation(
            summary = "Повторная отправка кода подтверждения",
            description = "Удаляет старую запись и шлёт новый код, если пользователь ещё не подтвердил e-mail"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Новый код отправлен",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MessageResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Пользователь не найден или уже подтверждён",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PostMapping("/resend-confirmation")
    public ResponseEntity<MessageResponse> resendConfirmation(
            @Valid @RequestBody ResendConfirmationRequest request) {
        confirmEmailService.resendConfirmationCode(request.getEmail());
        return ResponseEntity.ok(new MessageResponse("Код подтверждения успешно выслан"));
    }
}