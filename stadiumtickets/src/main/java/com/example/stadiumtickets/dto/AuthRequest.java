package com.example.stadiumtickets.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Запрос на вход в систему")
public class AuthRequest {

    @Schema(description = "Логин", example = "ivanov")
    @NotBlank(message = "Логин обязателен")
    private String username;

    @Schema(description = "Пароль", example = "password123")
    @NotBlank(message = "Пароль обязателен")
    private String password;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
