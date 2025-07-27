package com.example.authservice.controller;

import com.example.authservice.dto.response.UserDto;
import com.example.authservice.mapper.UserMapper;
import com.example.authservice.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;
    private final UserMapper userMapper;

    @Operation(summary = "Получить список всех пользователей")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Список пользователей получен",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403", description = "Нет прав доступа",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/ErrorResponse")
                    )
            )
    })
    @GetMapping("/users")
    public List<UserDto> listAll() {
        return adminService.getAllUsers().stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Operation(summary = "Деактивировать пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Пользователь деактивирован"),
            @ApiResponse(responseCode = "400", description = "Нельзя деактивировать себя или неверный запрос",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/ErrorResponse")
                    )
            ),
            @ApiResponse(responseCode = "403", description = "Нет прав доступа",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/ErrorResponse")
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/ErrorResponse")
                    )
            )
    })
    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivate(@PathVariable UUID id) {
        adminService.deactivateUser(id);
    }

    @Operation(summary = "Разблокировать (активировать) пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Пользователь активирован"),
            @ApiResponse(responseCode = "400", description = "Нельзя активировать себя или неверный запрос",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/ErrorResponse")
                    )
            ),
            @ApiResponse(responseCode = "403", description = "Нет прав доступа",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/ErrorResponse")
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/ErrorResponse")
                    )
            )
    })
    @PutMapping("/users/{id}/activate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void activate(@PathVariable UUID id) {
        adminService.activateUser(id);
    }

    @Operation(summary = "Изменить роль пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Роль пользователя успешно изменена",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Неверный параметр или попытка сменить свою роль",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/ErrorResponse")
                    )
            ),
            @ApiResponse(responseCode = "403", description = "Нет прав доступа",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/ErrorResponse")
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Пользователь или роль не найдены",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/ErrorResponse")
                    )
            )
    })
    @PutMapping("/users/{id}/role")
    public UserDto changeRole(
            @PathVariable UUID id, @RequestParam String role) {
        return adminService.changeUserRole(id, role);
    }
}