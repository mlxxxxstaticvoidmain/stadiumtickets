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

import com.example.stadiumtickets.model.Venue;
import com.example.stadiumtickets.repository.VenueRepository;

import io.swagger.v3.oas.annotations.Operation;
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
    @GetMapping
    public ResponseEntity<List<Venue>> getAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @Operation(summary = "Получить стадион по ID", description = "Возвращает данные стадиона по идентификатору")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        Venue venue = repository.findById(id).orElse(null);
        if (venue == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(venue);
    }

    @Operation(summary = "Создать стадион", description = "Добавляет новый стадион в систему")
    @PostMapping
    public ResponseEntity<Venue> create(@Valid @RequestBody Venue venue) {
        return ResponseEntity.ok(repository.save(venue));
    }

    @Operation(summary = "Обновить стадион", description = "Обновляет данные существующего стадиона")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @Valid @RequestBody Venue venueData) {
        Venue venue = repository.findById(id).orElse(null);
        if (venue == null) return ResponseEntity.notFound().build();
        venue.setNameVenue(venueData.getNameVenue());
        venue.setAddress(venueData.getAddress());
        return ResponseEntity.ok(repository.save(venue));
    }

    @Operation(summary = "Удалить стадион", description = "Удаляет стадион из системы")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
