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
import com.example.stadiumtickets.model.Employee;
import com.example.stadiumtickets.repository.EmployeeRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/employees")
@Tag(name = "Сотрудники", description = "Управление сотрудниками (кассирами) банков")
public class EmployeeApiController {

    private final EmployeeRepository repository;

    public EmployeeApiController(EmployeeRepository repository) {
        this.repository = repository;
    }

    @Operation(summary = "Получить всех сотрудников", description = "Возвращает список всех сотрудников")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Список сотрудников получен")
    })
    @GetMapping
    public ResponseEntity<List<Employee>> getAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @Operation(summary = "Получить сотрудника по ID", description = "Возвращает данные сотрудника по идентификатору")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Сотрудник найден"),
        @ApiResponse(responseCode = "404", description = "Сотрудник не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        Employee employee = repository.findById(id).orElse(null);
        if (employee == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(employee);
    }

    @Operation(summary = "Создать сотрудника", description = "Добавляет нового сотрудника в банк")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Сотрудник создан"),
        @ApiResponse(responseCode = "400", description = "Ошибка валидации или сотрудник с таким аккаунтом уже существует",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Employee employee) {
        if (employee.getAccount() != null
                && repository.findByAccountId(employee.getAccount().getId()).isPresent()) {
            return ResponseEntity.badRequest()
                .body(new ErrorResponse(400, "Сотрудник с таким аккаунтом уже существует"));
        }
        return ResponseEntity.ok(repository.save(employee));
    }

    @Operation(summary = "Обновить сотрудника", description = "Обновляет данные существующего сотрудника")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Сотрудник обновлен"),
        @ApiResponse(responseCode = "400", description = "Ошибка валидации данных",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Сотрудник не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @Valid @RequestBody Employee employeeData) {
        Employee employee = repository.findById(id).orElse(null);
        if (employee == null) return ResponseEntity.notFound().build();
        Employee byAccount = employeeData.getAccount() != null
            ? repository.findByAccountId(employeeData.getAccount().getId()).orElse(null)
            : null;
        if (byAccount != null && byAccount.getId() != id) {
            return ResponseEntity.badRequest()
                .body(new ErrorResponse(400, "Сотрудник с таким аккаунтом уже существует"));
        }
        employee.setBank(employeeData.getBank());
        employee.setAccount(employeeData.getAccount());
        employee.setHireDate(employeeData.getHireDate());
        employee.setSalary(employeeData.getSalary());
        return ResponseEntity.ok(repository.save(employee));
    }

    @Operation(summary = "Удалить сотрудника", description = "Удаляет сотрудника из системы")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Сотрудник удален"),
        @ApiResponse(responseCode = "404", description = "Сотрудник не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Получить сотрудников банка", description = "Возвращает сотрудников для указанного банка")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Список сотрудников получен")
    })
    @GetMapping("/by-bank/{bankId}")
    public ResponseEntity<List<Employee>> getByBankId(@PathVariable int bankId) {
        return ResponseEntity.ok(repository.findByBankId(bankId));
    }
}
