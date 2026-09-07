package com.example.stadiumtickets.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.stadiumtickets.dto.AuthRequest;
import com.example.stadiumtickets.dto.AuthResponse;
import com.example.stadiumtickets.dto.ErrorResponse;
import com.example.stadiumtickets.dto.RegisterRequest;
import com.example.stadiumtickets.model.Account;
import com.example.stadiumtickets.model.Role;
import com.example.stadiumtickets.repository.AccountRepository;
import com.example.stadiumtickets.repository.RoleRepository;
import com.example.stadiumtickets.util.JwtUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Авторизация", description = "Регистрация и вход в систему")
public class AuthController {

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(AccountRepository accountRepository,
                          RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder,
                          JwtUtil jwtUtil) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Operation(summary = "Регистрация нового пользователя", description = "Создает новый аккаунт с ролью 'Пользователь'")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Пользователь успешно зарегистрирован",
            content = @Content(schema = @Schema(implementation = AuthResponse.class))),
        @ApiResponse(responseCode = "400", description = "Ошибка валидации или логин/email уже занят",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        if (accountRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest()
                .body(new ErrorResponse(400, "Логин уже занят"));
        }
        if (accountRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest()
                .body(new ErrorResponse(400, "Email уже используется"));
        }

        Role userRole = roleRepository.findByName("Пользователь")
            .orElseGet(() -> roleRepository.save(new Role(0, "Пользователь", "browse,cart,myevents")));

        Account account = new Account();
        account.setUsername(request.getUsername());
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        account.setEmail(request.getEmail());
        account.setFirstName(request.getFirstName());
        account.setLastName(request.getLastName());
        account.setMiddleName(request.getMiddleName());
        account.setPhoneNumber(request.getPhoneNumber());
        account.setRole(userRole);
        account.setActive(true);

        accountRepository.save(account);

        String token = jwtUtil.generateToken(account.getUsername());
        return ResponseEntity.ok(new AuthResponse(token, account.getUsername()));
    }

    @Operation(summary = "Вход в систему", description = "Аутентификация пользователя и получение JWT токена")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Успешный вход, токен получен",
            content = @Content(schema = @Schema(implementation = AuthResponse.class))),
        @ApiResponse(responseCode = "401", description = "Неверный логин или пароль",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest request) {
        Account account = accountRepository.findByUsername(request.getUsername())
            .orElse(null);

        if (account == null || !passwordEncoder.matches(request.getPassword(), account.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse(401, "Неверный логин или пароль"));
        }

        String token = jwtUtil.generateToken(account.getUsername());
        return ResponseEntity.ok(new AuthResponse(token, account.getUsername()));
    }
}
