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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.stadiumtickets.dto.ErrorResponse;
import com.example.stadiumtickets.model.Bank;
import com.example.stadiumtickets.repository.BankRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/banks")
@Tag(name = "Банки", description = "Управление банками для оплаты заказов")
public class BankApiController {

    private final BankRepository repository;

    public BankApiController(BankRepository repository) {
        this.repository = repository;
    }

    @Operation(summary = "Получить все банки", description = "Возвращает список всех банков")
    @GetMapping
    public ResponseEntity<List<Bank>> getAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @Operation(summary = "Получить банк по ID", description = "Возвращает данные банка по идентификатору")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Банк найден"),
        @ApiResponse(responseCode = "404", description = "Банк не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        Bank bank = repository.findById(id).orElse(null);
        if (bank == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(bank);
    }

    @Operation(summary = "Получить банк по названию", description = "Возвращает банк с указанным названием")
    @GetMapping("/by-name")
    public ResponseEntity<?> getByName(@RequestParam String bankName) {
        Bank bank = repository.findByBankName(bankName).orElse(null);
        if (bank == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(bank);
    }

    @Operation(summary = "Создать банк", description = "Добавляет новый банк в систему")
    @PostMapping
    public ResponseEntity<Bank> create(@Valid @RequestBody Bank bank) {
        return ResponseEntity.ok(repository.save(bank));
    }

    @Operation(summary = "Обновить банк", description = "Обновляет данные существующего банка")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @Valid @RequestBody Bank bankData) {
        Bank bank = repository.findById(id).orElse(null);
        if (bank == null) return ResponseEntity.notFound().build();
        bank.setBankName(bankData.getBankName());
        return ResponseEntity.ok(repository.save(bank));
    }

    @Operation(summary = "Удалить банк", description = "Удаляет банк из системы")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
