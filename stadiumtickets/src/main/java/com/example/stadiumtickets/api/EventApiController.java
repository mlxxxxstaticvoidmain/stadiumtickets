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
import com.example.stadiumtickets.model.Event;
import com.example.stadiumtickets.repository.EventRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/events")
@Tag(name = "События", description = "Управление событиями (матчами) на стадионе")
public class EventApiController {

    private final EventRepository repository;

    public EventApiController(EventRepository repository) {
        this.repository = repository;
    }

    @Operation(summary = "Получить все события", description = "Возвращает список всех событий/матчей")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Список событий получен")
    })
    @GetMapping
    public ResponseEntity<List<Event>> getAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @Operation(summary = "Получить событие по ID", description = "Возвращает данные события по идентификатору")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Событие найдено"),
        @ApiResponse(responseCode = "404", description = "Событие не найдено",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        Event event = repository.findById(id).orElse(null);
        if (event == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(event);
    }

    @Operation(summary = "Создать событие", description = "Добавляет новое событие/матч")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Событие создано"),
        @ApiResponse(responseCode = "400", description = "Ошибка валидации или неверная ссылка на стадион/тип игры",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<Event> create(@Valid @RequestBody Event event) {
        return ResponseEntity.ok(repository.save(event));
    }

    @Operation(summary = "Обновить событие", description = "Обновляет данные существующего события")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Событие обновлено"),
        @ApiResponse(responseCode = "400", description = "Ошибка валидации данных",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Событие не найдено",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @Valid @RequestBody Event eventData) {
        Event event = repository.findById(id).orElse(null);
        if (event == null) return ResponseEntity.notFound().build();
        event.setVenue(eventData.getVenue());
        event.setGametype(eventData.getGametype());
        event.setTitle(eventData.getTitle());
        event.setEventDate(eventData.getEventDate());
        event.setPrice(eventData.getPrice());
        return ResponseEntity.ok(repository.save(event));
    }

    @Operation(summary = "Удалить событие", description = "Удаляет событие из системы")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Событие удалено"),
        @ApiResponse(responseCode = "404", description = "Событие не найдено",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Получить события по стадиону", description = "Возвращает события для указанного стадиона")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Список событий получен")
    })
    @GetMapping("/by-venue/{venueId}")
    public ResponseEntity<List<Event>> getByVenueId(@PathVariable int venueId) {
        return ResponseEntity.ok(repository.findByVenueId(venueId));
    }
}
