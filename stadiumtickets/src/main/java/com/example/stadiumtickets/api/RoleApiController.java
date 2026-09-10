package com.example.stadiumtickets.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.stadiumtickets.dto.ErrorResponse;
import com.example.stadiumtickets.model.Role;
import com.example.stadiumtickets.repository.RoleRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/roles")
@Tag(name = "Роли", description = "Управление ролями пользователей")
public class RoleApiController {

    private final RoleRepository repository;

    public RoleApiController(RoleRepository repository) {
        this.repository = repository;
    }

    @Operation(summary = "Получить все роли", description = "Возвращает список всех ролей в системе")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Список ролей получен")
    })
    @GetMapping
    public ResponseEntity<List<Role>> getAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @Operation(summary = "Получить роль по ID", description = "Возвращает роль по её идентификатору")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Роль найдена"),
        @ApiResponse(responseCode = "404", description = "Роль не найдена",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        Role role = repository.findById(id).orElse(null);
        if (role == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(role);
    }

    @Operation(summary = "Создать новую роль", description = "Добавляет новую роль в систему")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Роль создана"),
        @ApiResponse(responseCode = "400", description = "Роль с таким названием уже существует",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Role role) {
        if (repository.findByName(role.getName()).isPresent()) {
            return ResponseEntity.badRequest()
                .body(new ErrorResponse(400, "Роль с таким названием уже существует"));
        }
        return ResponseEntity.ok(repository.save(role));
    }

    @Operation(summary = "Обновить роль", description = "Обновляет данные существующей роли")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Роль обновлена"),
        @ApiResponse(responseCode = "400", description = "Роль с таким названием уже существует",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Роль не найдена",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @Valid @RequestBody Role roleData) {
        Role role = repository.findById(id).orElse(null);
        if (role == null) {
            return ResponseEntity.notFound().build();
        }
        Role byName = repository.findByName(roleData.getName()).orElse(null);
        if (byName != null && byName.getId() != id) {
            return ResponseEntity.badRequest()
                .body(new ErrorResponse(400, "Роль с таким названием уже существует"));
        }
        role.setName(roleData.getName());
        role.setPermissions(roleData.getPermissions());
        return ResponseEntity.ok(repository.save(role));
    }

    @Operation(summary = "Удалить роль", description = "Удаляет роль из системы")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Роль удалена"),
        @ApiResponse(responseCode = "404", description = "Роль не найдена",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
