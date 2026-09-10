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
import com.example.stadiumtickets.model.Order;
import com.example.stadiumtickets.model.Ticket;
import com.example.stadiumtickets.repository.OrderRepository;
import com.example.stadiumtickets.repository.TicketRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Заказы", description = "Управление заказами билетов")
public class OrderApiController {

    private final OrderRepository repository;
    private final TicketRepository ticketRepository;

    public OrderApiController(OrderRepository repository, TicketRepository ticketRepository) {
        this.repository = repository;
        this.ticketRepository = ticketRepository;
    }

    @Operation(summary = "Получить все заказы", description = "Возвращает список всех заказов")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Список заказов получен")
    })
    @GetMapping
    public ResponseEntity<List<Order>> getAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @Operation(summary = "Получить заказ по ID", description = "Возвращает данные заказа по идентификатору")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Заказ найден"),
        @ApiResponse(responseCode = "404", description = "Заказ не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        Order order = repository.findById(id).orElse(null);
        if (order == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(order);
    }

    @Operation(summary = "Создать заказ", description = "Создает новый заказ и меняет статус билета на 'sold'")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Заказ создан"),
        @ApiResponse(responseCode = "400", description = "Билет уже продан или забронирован",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Order order) {
        Ticket ticket = ticketRepository.findById(order.getTicket().getId()).orElse(null);
        if (ticket == null) {
            return ResponseEntity.badRequest()
                .body(new ErrorResponse(400, "Билет не найден"));
        }
        if (!"available".equals(ticket.getStatus())) {
            return ResponseEntity.badRequest()
                .body(new ErrorResponse(400, "Билет уже " + ticket.getStatus()));
        }
        ticket.setStatus("sold");
        ticketRepository.save(ticket);

        order.setTotalAmount(ticket.getPrice());
        return ResponseEntity.ok(repository.save(order));
    }

    @Operation(summary = "Обновить заказ", description = "Обновляет данные существующего заказа")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Заказ обновлен"),
        @ApiResponse(responseCode = "400", description = "Ошибка валидации данных",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Заказ не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @Valid @RequestBody Order orderData) {
        Order order = repository.findById(id).orElse(null);
        if (order == null) return ResponseEntity.notFound().build();
        order.setPaymentMethod(orderData.getPaymentMethod());
        order.setEmployee(orderData.getEmployee());
        return ResponseEntity.ok(repository.save(order));
    }

    @Operation(summary = "Удалить заказ", description = "Удаляет заказ и возвращает билет в статус 'available'")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Заказ удален"),
        @ApiResponse(responseCode = "404", description = "Заказ не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        Order order = repository.findById(id).orElse(null);
        if (order == null) return ResponseEntity.notFound().build();

        Ticket ticket = order.getTicket();
        ticket.setStatus("available");
        ticketRepository.save(ticket);

        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Получить заказы пользователя", description = "Возвращает заказы для указанного пользователя")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Список заказов получен")
    })
    @GetMapping("/by-account/{accountId}")
    public ResponseEntity<List<Order>> getByAccountId(@PathVariable int accountId) {
        return ResponseEntity.ok(repository.findByAccountId(accountId));
    }
}
