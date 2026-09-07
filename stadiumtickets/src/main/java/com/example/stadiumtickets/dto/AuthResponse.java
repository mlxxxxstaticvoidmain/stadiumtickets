package com.example.stadiumtickets.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ответ с JWT токеном")
public class AuthResponse {

    @Schema(description = "JWT токен для авторизации", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String token;

    @Schema(description = "Тип токена", example = "Bearer")
    private String type = "Bearer";

    @Schema(description = "Логин пользователя", example = "ivanov")
    private String username;

    public AuthResponse() {}

    public AuthResponse(String token, String username) {
        this.token = token;
        this.username = username;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
