package com.example.stadiumtickets.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Запрос на регистрацию нового пользователя")
public class RegisterRequest {

    @Schema(description = "Логин", example = "ivanov")
    @NotBlank(message = "Логин обязателен")
    @Size(min = 3, max = 50, message = "Логин должен быть от 3 до 50 символов")
    private String username;

    @Schema(description = "Пароль", example = "password123")
    @NotBlank(message = "Пароль обязателен")
    @Size(min = 6, message = "Пароль должен быть не менее 6 символов")
    private String password;

    @Schema(description = "Email", example = "ivan@example.com")
    @NotBlank(message = "Email обязателен")
    @Email(message = "Введите корректный email")
    private String email;

    @Schema(description = "Имя", example = "Иван")
    @NotBlank(message = "Имя обязательно")
    @Size(min = 2, message = "Имя должно быть не менее 2 символов")
    private String firstName;

    @Schema(description = "Фамилия", example = "Иванов")
    @NotBlank(message = "Фамилия обязательна")
    @Size(min = 2, message = "Фамилия должна быть не менее 2 символов")
    private String lastName;

    @Schema(description = "Отчество", example = "Иванович")
    private String middleName;

    @Schema(description = "Номер телефона", example = "+7-999-123-45-67")
    @NotBlank(message = "Номер телефона обязателен")
    @Pattern(regexp = "^\\+?[0-9\\s\\-\\(\\)]{7,20}$", message = "Введите корректный номер телефона")
    private String phoneNumber;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
}
