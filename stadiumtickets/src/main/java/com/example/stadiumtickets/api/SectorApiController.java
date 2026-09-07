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

import com.example.stadiumtickets.model.Sector;
import com.example.stadiumtickets.repository.SectorRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/sectors")
@Tag(name = "Секторы", description = "Управление секторами стадионов")
public class SectorApiController {

    private final SectorRepository repository;

    public SectorApiController(SectorRepository repository) {
        this.repository = repository;
    }

    @Operation(summary = "Получить все секторы", description = "Возвращает список всех секторов")
    @GetMapping
    public ResponseEntity<List<Sector>> getAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @Operation(summary = "Получить сектор по ID", description = "Возвращает данные сектора по идентификатору")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        Sector sector = repository.findById(id).orElse(null);
        if (sector == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(sector);
    }

    @Operation(summary = "Создать сектор", description = "Добавляет новый сектор для стадиона")
    @PostMapping
    public ResponseEntity<Sector> create(@Valid @RequestBody Sector sector) {
        return ResponseEntity.ok(repository.save(sector));
    }

    @Operation(summary = "Обновить сектор", description = "Обновляет данные существующего сектора")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @Valid @RequestBody Sector sectorData) {
        Sector sector = repository.findById(id).orElse(null);
        if (sector == null) return ResponseEntity.notFound().build();
        sector.setVenue(sectorData.getVenue());
        sector.setSectorName(sectorData.getSectorName());
        sector.setZone(sectorData.getZone());
        return ResponseEntity.ok(repository.save(sector));
    }

    @Operation(summary = "Удалить сектор", description = "Удаляет сектор из системы")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Получить секторы по стадиону", description = "Возвращает секторы для указанного стадиона")
    @GetMapping("/by-venue/{venueId}")
    public ResponseEntity<List<Sector>> getByVenueId(@PathVariable int venueId) {
        return ResponseEntity.ok(repository.findByVenueId(venueId));
    }
}
