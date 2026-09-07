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

import com.example.stadiumtickets.model.Employee;
import com.example.stadiumtickets.repository.EmployeeRepository;

import io.swagger.v3.oas.annotations.Operation;
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
    @GetMapping
    public ResponseEntity<List<Employee>> getAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @Operation(summary = "Получить сотрудника по ID", description = "Возвращает данные сотрудника по идентификатору")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        Employee employee = repository.findById(id).orElse(null);
        if (employee == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(employee);
    }

    @Operation(summary = "Создать сотрудника", description = "Добавляет нового сотрудника в банк")
    @PostMapping
    public ResponseEntity<Employee> create(@Valid @RequestBody Employee employee) {
        return ResponseEntity.ok(repository.save(employee));
    }

    @Operation(summary = "Обновить сотрудника", description = "Обновляет данные существующего сотрудника")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @Valid @RequestBody Employee employeeData) {
        Employee employee = repository.findById(id).orElse(null);
        if (employee == null) return ResponseEntity.notFound().build();
        employee.setBank(employeeData.getBank());
        employee.setAccount(employeeData.getAccount());
        employee.setHireDate(employeeData.getHireDate());
        employee.setSalary(employeeData.getSalary());
        return ResponseEntity.ok(repository.save(employee));
    }

    @Operation(summary = "Удалить сотрудника", description = "Удаляет сотрудника из системы")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Получить сотрудников банка", description = "Возвращает сотрудников для указанного банка")
    @GetMapping("/by-bank/{bankId}")
    public ResponseEntity<List<Employee>> getByBankId(@PathVariable int bankId) {
        return ResponseEntity.ok(repository.findByBankId(bankId));
    }
}
