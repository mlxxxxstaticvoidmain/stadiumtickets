package com.example.stadiumtickets.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.stadiumtickets.model.Account;
import com.example.stadiumtickets.model.Cart;
import com.example.stadiumtickets.model.Ticket;
import com.example.stadiumtickets.service.AccountService;
import com.example.stadiumtickets.service.CartService;
import com.example.stadiumtickets.service.TicketService;

@Controller
@RequestMapping("/admin/cart")
public class CartController {

    @Autowired
    private CartService cartService;
    
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private TicketService ticketService;

    @GetMapping
    public String listCart(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) Integer ticketId,
            Model model) {
        
        Page<Cart> cartPage;
        
        if (id != null) {
            Cart cart = cartService.findById(id);
            List<Cart> cartList = cart != null ? List.of(cart) : List.of();
            cartPage = new PageImpl<>(cartList);
            model.addAttribute("search", "ID: " + id);
        } else if (lastName != null && !lastName.isEmpty()) {
            cartPage = cartService.findByAccountLastName(lastName, page, size);
            model.addAttribute("search", "Фамилия: " + lastName);
        } else if (ticketId != null) {
            cartPage = cartService.findByTicketId(ticketId, page, size);
            model.addAttribute("search", "Билет ID: " + ticketId);
        } else {
            cartPage = cartService.findAll(page, size);
        }
        
        List<Cart> carts = cartPage.getContent();
        for (Cart cart : carts) {
            if (cart.getAccount() != null) {
                Account account = cart.getAccount();
                String fullName = account.getLastName() + " " + account.getFirstName();
                if (account.getMiddleName() != null && !account.getMiddleName().isEmpty()) {
                    fullName += " " + account.getMiddleName();
                }
                cart.setAccountName(fullName);
            }
            if (cart.getTicket() != null) {
                Ticket ticket = cart.getTicket();
                String ticketInfo = "Билет #" + ticket.getId();
                if (ticket.getEvent() != null) {
                    ticketInfo += " (" + ticket.getEvent().getTitle() + ")";
                }
                cart.setTicketInfo(ticketInfo);
            }
        }
        model.addAttribute("carts", carts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", cartPage.getTotalPages());
        model.addAttribute("totalItems", cartPage.getTotalElements());
        model.addAttribute("size", size);
        
        return "admin/cart/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("cart", new Cart());
        model.addAttribute("accounts", accountService.findAll());
        List<Ticket> availableTickets = ticketService.findByStatus("available");
        model.addAttribute("tickets", availableTickets);
        return "admin/cart/add";
    }

    @PostMapping("/add")
    public String addCart(@RequestParam int accountId, @RequestParam int ticketId) {
        
        Ticket ticket = ticketService.findById(ticketId);
        if (ticket == null) {
            return "redirect:/admin/cart?error=Билет не найден";
        }
        
        List<Cart> existingCarts = cartService.findByAccountId(accountId);
        boolean alreadyInCart = existingCarts.stream()
                .anyMatch(cart -> (cart.getTicket() != null ? cart.getTicket().getId() : -1) == ticketId);
        
        if (alreadyInCart) {
            return "redirect:/admin/cart?error=Этот билет уже в корзине пользователя";
        }
        
        if ("reserved".equals(ticket.getStatus())) {
            return "redirect:/admin/cart?error=Билет уже в корзине";
        }
        if ("sold".equals(ticket.getStatus())) {
            return "redirect:/admin/cart?error=Билет уже продан";
        }
        
        Cart cart = new Cart();
        cart.setAccount(accountService.findById(accountId));
        cart.setTicket(ticketService.findById(ticketId));
        cart.setAddedAt(LocalDateTime.now());
        
        cartService.save(cart);
        ticket.setStatus("reserved");
        ticketService.save(ticket);
        
        return "redirect:/admin/cart";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Cart cart = cartService.findById(id);
        model.addAttribute("cart", cart);
        model.addAttribute("accounts", accountService.findAll());
        List<Ticket> allTickets = ticketService.findAll();
        model.addAttribute("tickets", allTickets);
        return "admin/cart/edit";
    }
    
    @PostMapping("/edit")
    public String updateCart(@RequestParam int id, @RequestParam int accountId, @RequestParam int ticketId) {
        
        Cart cart = cartService.findById(id);
        int oldTicketId = cart.getTicket() != null ? cart.getTicket().getId() : -1;
        
        if (oldTicketId != ticketId) {
            List<Cart> existingCarts = cartService.findByAccountId(accountId);
            boolean alreadyInCart = existingCarts.stream()
                .anyMatch(c -> c.getTicket() != null && c.getTicket().getId() == ticketId && c.getId() != id);
            
            if (alreadyInCart) {
                return "redirect:/admin/cart?error=Этот билет уже в корзине пользователя";
            }
            
            Ticket oldTicket = ticketService.findById(oldTicketId);
            if (oldTicket != null && "reserved".equals(oldTicket.getStatus())) {
                oldTicket.setStatus("available");
                ticketService.save(oldTicket);
            }
            
            Ticket newTicket = ticketService.findById(ticketId);
            if (newTicket != null && !"available".equals(newTicket.getStatus())) {
                return "redirect:/admin/cart?error=Билет недоступен";
            }
            
            if (newTicket != null) {
                newTicket.setStatus("reserved");
                ticketService.save(newTicket);
            }
        }
        
        cart.setAccount(accountService.findById(accountId));
        cart.setTicket(ticketService.findById(ticketId));
        
        cartService.save(cart);
        return "redirect:/admin/cart";
    }

    @GetMapping("/delete/{id}")
    public String deleteCart(@PathVariable int id) {
        Cart cart = cartService.findById(id);
        if (cart != null) {
            Ticket ticket = ticketService.findById(cart.getTicket() != null ? cart.getTicket().getId() : -1);
            if (ticket != null && "reserved".equals(ticket.getStatus())) {
                ticket.setStatus("available");
                ticketService.save(ticket);
            }
        }
        cartService.deleteCart(id);
        return "redirect:/admin/cart";
    }
    
    @GetMapping("/clear/{accountId}")
    public String clearCart(@PathVariable int accountId) {
        List<Cart> carts = cartService.findByAccountId(accountId);
        for (Cart cart : carts) {
            Ticket ticket = ticketService.findById(cart.getTicket() != null ? cart.getTicket().getId() : -1);
            if (ticket != null && "reserved".equals(ticket.getStatus())) {
                ticket.setStatus("available");
                ticketService.save(ticket);
            }
        }
        cartService.deleteByAccountId(accountId);
        return "redirect:/admin/cart";
    }
}