package com.example.stadiumtickets.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.stadiumtickets.dto.ErrorResponse;
import com.example.stadiumtickets.model.Account;
import com.example.stadiumtickets.repository.AccountRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Пользователи", description = "Управление учетными записями пользователей")
public class AccountApiController {

    private final AccountRepository repository;
    private final PasswordEncoder passwordEncoder;

    public AccountApiController(AccountRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Operation(summary = "Получить всех пользователей", description = "Возвращает список всех учетных записей")
    @GetMapping
    public ResponseEntity<List<Account>> getAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @Operation(summary = "Получить пользователя по ID", description = "Возвращает данные пользователя по его идентификатору")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Пользователь найден"),
        @ApiResponse(responseCode = "404", description = "Пользователь не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        Account account = repository.findById(id).orElse(null);
        if (account == null) {
            return ResponseEntity.notFound().build();
        }
        account.setPassword(null);
        return ResponseEntity.ok(account);
    }

    @Operation(summary = "Создать пользователя", description = "Добавляет нового пользователя в систему")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Account account) {
        if (repository.existsByUsername(account.getUsername())) {
            return ResponseEntity.badRequest()
                .body(new ErrorResponse(400, "Логин уже занят"));
        }
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setActive(true);
        return ResponseEntity.ok(repository.save(account));
    }

    @Operation(summary = "Обновить пользователя", description = "Обновляет данные существующего пользователя")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @Valid @RequestBody Account accountData) {
        Account account = repository.findById(id).orElse(null);
        if (account == null) {
            return ResponseEntity.notFound().build();
        }
        account.setUsername(accountData.getUsername());
        account.setEmail(accountData.getEmail());
        account.setFirstName(accountData.getFirstName());
        account.setLastName(accountData.getLastName());
        account.setMiddleName(accountData.getMiddleName());
        account.setPhoneNumber(accountData.getPhoneNumber());
        account.setRole(accountData.getRole());
        account.setBankName(accountData.getBankName());
        if (accountData.getPassword() != null && !accountData.getPassword().isEmpty()) {
            account.setPassword(passwordEncoder.encode(accountData.getPassword()));
        }
        return ResponseEntity.ok(repository.save(account));
    }

    @Operation(summary = "Удалить пользователя", description = "Удаляет пользователя из системы")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
