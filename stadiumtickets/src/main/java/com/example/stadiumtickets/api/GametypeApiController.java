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
import com.example.stadiumtickets.model.Gametype;
import com.example.stadiumtickets.repository.GametypeRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/gametypes")
@Tag(name = "Типы игр", description = "Управление типами игр и турниров")
public class GametypeApiController {

    private final GametypeRepository repository;

    public GametypeApiController(GametypeRepository repository) {
        this.repository = repository;
    }

    @Operation(summary = "Получить все типы игр", description = "Возвращает список всех типов игр/турниров")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Список типов игр получен")
    })
    @GetMapping
    public ResponseEntity<List<Gametype>> getAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @Operation(summary = "Получить тип игры по ID", description = "Возвращает данные типа игры по идентификатору")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Тип игры найден"),
        @ApiResponse(responseCode = "404", description = "Тип игры не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        Gametype gametype = repository.findById(id).orElse(null);
        if (gametype == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(gametype);
    }

    @Operation(summary = "Создать тип игры", description = "Добавляет новый тип игры/турнир")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Тип игры создан"),
        @ApiResponse(responseCode = "400", description = "Тип игры с таким названием уже существует",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Gametype gametype) {
        if (repository.findByTournamentName(gametype.getTournamentName()).isPresent()) {
            return ResponseEntity.badRequest()
                .body(new ErrorResponse(400, "Тип игры с таким названием уже существует"));
        }
        return ResponseEntity.ok(repository.save(gametype));
    }

    @Operation(summary = "Обновить тип игры", description = "Обновляет данные существующего типа игры")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Тип игры обновлен"),
        @ApiResponse(responseCode = "400", description = "Тип игры с таким названием уже существует",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Тип игры не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @Valid @RequestBody Gametype gametypeData) {
        Gametype gametype = repository.findById(id).orElse(null);
        if (gametype == null) return ResponseEntity.notFound().build();
        Gametype byName = repository.findByTournamentName(gametypeData.getTournamentName()).orElse(null);
        if (byName != null && byName.getId() != id) {
            return ResponseEntity.badRequest()
                .body(new ErrorResponse(400, "Тип игры с таким названием уже существует"));
        }
        gametype.setTournamentName(gametypeData.getTournamentName());
        return ResponseEntity.ok(repository.save(gametype));
    }

    @Operation(summary = "Удалить тип игры", description = "Удаляет тип игры из системы")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Тип игры удален"),
        @ApiResponse(responseCode = "404", description = "Тип игры не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
