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

import com.example.stadiumtickets.model.Seat;
import com.example.stadiumtickets.repository.SeatRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/seats")
@Tag(name = "Места", description = "Управление местами в секторах")
public class SeatApiController {

    private final SeatRepository repository;

    public SeatApiController(SeatRepository repository) {
        this.repository = repository;
    }

    @Operation(summary = "Получить все места", description = "Возвращает список всех мест")
    @GetMapping
    public ResponseEntity<List<Seat>> getAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @Operation(summary = "Получить место по ID", description = "Возвращает данные места по идентификатору")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        Seat seat = repository.findById(id).orElse(null);
        if (seat == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(seat);
    }

    @Operation(summary = "Создать место", description = "Добавляет новое место в сектор")
    @PostMapping
    public ResponseEntity<Seat> create(@Valid @RequestBody Seat seat) {
        return ResponseEntity.ok(repository.save(seat));
    }

    @Operation(summary = "Обновить место", description = "Обновляет данные существующего места")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @Valid @RequestBody Seat seatData) {
        Seat seat = repository.findById(id).orElse(null);
        if (seat == null) return ResponseEntity.notFound().build();
        seat.setSector(seatData.getSector());
        seat.setRowNumber(seatData.getRowNumber());
        seat.setSeatNumber(seatData.getSeatNumber());
        return ResponseEntity.ok(repository.save(seat));
    }

    @Operation(summary = "Удалить место", description = "Удаляет место из системы")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Получить места по сектору", description = "Возвращает места для указанного сектора")
    @GetMapping("/by-sector/{sectorId}")
    public ResponseEntity<List<Seat>> getBySectorId(@PathVariable int sectorId) {
        return ResponseEntity.ok(repository.findBySectorId(sectorId));
    }
}
