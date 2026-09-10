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
import com.example.stadiumtickets.model.Venue;
import com.example.stadiumtickets.repository.VenueRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/venues")
@Tag(name = "Стадионы", description = "Управление стадионами")
public class VenueApiController {

    private final VenueRepository repository;

    public VenueApiController(VenueRepository repository) {
        this.repository = repository;
    }

    @Operation(summary = "Получить все стадионы", description = "Возвращает список всех стадионов")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Список стадионов получен")
    })
    @GetMapping
    public ResponseEntity<List<Venue>> getAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @Operation(summary = "Получить стадион по ID", description = "Возвращает данные стадиона по идентификатору")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Стадион найден"),
        @ApiResponse(responseCode = "404", description = "Стадион не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        Venue venue = repository.findById(id).orElse(null);
        if (venue == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(venue);
    }

    @Operation(summary = "Создать стадион", description = "Добавляет новый стадион в систему")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Стадион создан"),
        @ApiResponse(responseCode = "400", description = "Стадион с таким названием уже существует",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Venue venue) {
        if (repository.findByNameVenue(venue.getNameVenue()).isPresent()) {
            return ResponseEntity.badRequest()
                .body(new ErrorResponse(400, "Стадион с таким названием уже существует"));
        }
        return ResponseEntity.ok(repository.save(venue));
    }

    @Operation(summary = "Обновить стадион", description = "Обновляет данные существующего стадиона")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Стадион обновлен"),
        @ApiResponse(responseCode = "400", description = "Стадион с таким названием уже существует",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Стадион не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @Valid @RequestBody Venue venueData) {
        Venue venue = repository.findById(id).orElse(null);
        if (venue == null) return ResponseEntity.notFound().build();
        Venue byName = repository.findByNameVenue(venueData.getNameVenue()).orElse(null);
        if (byName != null && byName.getId() != id) {
            return ResponseEntity.badRequest()
                .body(new ErrorResponse(400, "Стадион с таким названием уже существует"));
        }
        venue.setNameVenue(venueData.getNameVenue());
        venue.setAddress(venueData.getAddress());
        return ResponseEntity.ok(repository.save(venue));
    }

    @Operation(summary = "Удалить стадион", description = "Удаляет стадион из системы")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Стадион удален"),
        @ApiResponse(responseCode = "404", description = "Стадион не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
