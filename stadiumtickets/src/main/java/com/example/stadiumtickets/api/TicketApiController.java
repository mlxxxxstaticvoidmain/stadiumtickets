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
import com.example.stadiumtickets.model.Ticket;
import com.example.stadiumtickets.repository.TicketRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tickets")
@Tag(name = "Билеты", description = "Управление билетами на события")
public class TicketApiController {

    private final TicketRepository repository;

    public TicketApiController(TicketRepository repository) {
        this.repository = repository;
    }

    @Operation(summary = "Получить все билеты", description = "Возвращает список всех билетов")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Список билетов получен")
    })
    @GetMapping
    public ResponseEntity<List<Ticket>> getAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @Operation(summary = "Получить билет по ID", description = "Возвращает данные билета по идентификатору")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Билет найден"),
        @ApiResponse(responseCode = "404", description = "Билет не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        Ticket ticket = repository.findById(id).orElse(null);
        if (ticket == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(ticket);
    }

    @Operation(summary = "Создать билет", description = "Добавляет новый билет на событие")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Билет создан"),
        @ApiResponse(responseCode = "400", description = "Ошибка валидации или билет на это место уже существует",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<Ticket> create(@Valid @RequestBody Ticket ticket) {
        return ResponseEntity.ok(repository.save(ticket));
    }

    @Operation(summary = "Обновить билет", description = "Обновляет данные существующего билета")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Билет обновлен"),
        @ApiResponse(responseCode = "400", description = "Ошибка валидации данных",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Билет не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @Valid @RequestBody Ticket ticketData) {
        Ticket ticket = repository.findById(id).orElse(null);
        if (ticket == null) return ResponseEntity.notFound().build();
        ticket.setEvent(ticketData.getEvent());
        ticket.setSeat(ticketData.getSeat());
        ticket.setStatus(ticketData.getStatus());
        ticket.setPrice(ticketData.getPrice());
        ticket.setVipAccess(ticketData.isVipAccess());
        return ResponseEntity.ok(repository.save(ticket));
    }

    @Operation(summary = "Удалить билет", description = "Удаляет билет из системы")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Билет удален"),
        @ApiResponse(responseCode = "404", description = "Билет не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Получить билеты по событию", description = "Возвращает билеты для указанного события")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Список билетов получен")
    })
    @GetMapping("/by-event/{eventId}")
    public ResponseEntity<List<Ticket>> getByEventId(@PathVariable int eventId) {
        return ResponseEntity.ok(repository.findByEventId(eventId));
    }

    @Operation(summary = "Получить билеты по статусу", description = "Возвращает билеты с указанным статусом")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Список билетов получен")
    })
    @GetMapping("/by-status/{status}")
    public ResponseEntity<List<Ticket>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(repository.findByStatus(status));
    }
}
