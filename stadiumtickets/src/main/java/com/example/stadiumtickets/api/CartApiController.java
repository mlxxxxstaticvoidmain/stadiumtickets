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
import com.example.stadiumtickets.model.Cart;
import com.example.stadiumtickets.model.Ticket;
import com.example.stadiumtickets.repository.CartRepository;
import com.example.stadiumtickets.repository.TicketRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cart")
@Tag(name = "Корзина", description = "Управление корзиной билетов")
public class CartApiController {

    private final CartRepository repository;
    private final TicketRepository ticketRepository;

    public CartApiController(CartRepository repository, TicketRepository ticketRepository) {
        this.repository = repository;
        this.ticketRepository = ticketRepository;
    }

    @Operation(summary = "Получить все корзины", description = "Возвращает список всех элементов корзин")
    @GetMapping
    public ResponseEntity<List<Cart>> getAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @Operation(summary = "Получить элемент корзины по ID", description = "Возвращает данные элемента корзины по идентификатору")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        Cart cart = repository.findById(id).orElse(null);
        if (cart == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(cart);
    }

    @Operation(summary = "Добавить билет в корзину", description = "Добавляет билет в корзину пользователя и меняет статус билета на 'reserved'")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Билет добавлен в корзину"),
        @ApiResponse(responseCode = "400", description = "Билет не найден, уже занят или уже в корзине",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Cart cart) {
        Ticket ticket = ticketRepository.findById(cart.getTicket().getId()).orElse(null);
        if (ticket == null) {
            return ResponseEntity.badRequest()
                .body(new ErrorResponse(400, "Билет не найден"));
        }
        if (!"available".equals(ticket.getStatus())) {
            return ResponseEntity.badRequest()
                .body(new ErrorResponse(400, "Билет уже " + ticket.getStatus()));
        }
        if (!repository.findByTicketId(ticket.getId()).isEmpty()) {
            return ResponseEntity.badRequest()
                .body(new ErrorResponse(400, "Билет уже находится в корзине"));
        }
        ticket.setStatus("reserved");
        ticketRepository.save(ticket);

        cart.setTicket(ticket);
        return ResponseEntity.ok(repository.save(cart));
    }

    @Operation(summary = "Обновить элемент корзины", description = "Обновляет пользователя, которому принадлежит элемент корзины")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @Valid @RequestBody Cart cartData) {
        Cart cart = repository.findById(id).orElse(null);
        if (cart == null) return ResponseEntity.notFound().build();
        cart.setAccount(cartData.getAccount());
        return ResponseEntity.ok(repository.save(cart));
    }

    @Operation(summary = "Удалить элемент корзины", description = "Удаляет элемент корзины и возвращает билет в статус 'available'")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        Cart cart = repository.findById(id).orElse(null);
        if (cart == null) return ResponseEntity.notFound().build();

        Ticket ticket = cart.getTicket();
        ticket.setStatus("available");
        ticketRepository.save(ticket);

        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Получить корзину пользователя", description = "Возвращает элементы корзины для указанного пользователя")
    @GetMapping("/by-account/{accountId}")
    public ResponseEntity<List<Cart>> getByAccountId(@PathVariable int accountId) {
        return ResponseEntity.ok(repository.findByAccountId(accountId));
    }
}
