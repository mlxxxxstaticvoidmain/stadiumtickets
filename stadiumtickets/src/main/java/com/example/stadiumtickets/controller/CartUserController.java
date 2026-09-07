package com.example.stadiumtickets.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
@RequestMapping("/cart")
public class CartUserController {

    @Autowired
    private CartService cartService;
    
    @Autowired
    private TicketService ticketService;
    
    @Autowired
    private AccountService accountService;

    private Account getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            return accountService.findByUsername(auth.getName());
        }
        return null;
    }

    @GetMapping
    public String viewCart(Model model) {
        Account user = getCurrentUser();
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("cartItems", cartService.findByAccountId(user.getId()));
        return "cart/view";
    }
    
    @PostMapping("/add")
    public String addToCart(@RequestParam int ticketId) {
        Account user = getCurrentUser();
        if (user == null) {
            return "redirect:/login";
        }
        
        Ticket ticket = ticketService.findById(ticketId);
        if (ticket == null) {
            return "redirect:/cart?error=Билет не найден";
        }
        
        List<Cart> userCart = cartService.findByAccountId(user.getId());
        boolean alreadyInCart = userCart.stream()
            .anyMatch(cart -> cart.getTicket() != null && cart.getTicket().getId() == ticketId);
        
        if (alreadyInCart) {
            return "redirect:/cart?error=Билет уже в корзине";
        }
        
        if (!"available".equals(ticket.getStatus())) {
            return "redirect:/cart?error=Билет недоступен";
        }
        
        Cart cart = new Cart();
        cart.setAccount(user);
        cart.setTicket(ticketService.findById(ticketId));
        cartService.save(cart);
        ticket.setStatus("reserved");
        ticketService.save(ticket);
        
        return "redirect:/cart";
    }
    
    @GetMapping("/remove/{id}")
    public String removeFromCart(@PathVariable int id) {
        Cart cart = cartService.findById(id);
        if (cart != null) {
            Ticket ticket = ticketService.findById(cart.getTicket() != null ? cart.getTicket().getId() : -1);
            if (ticket != null && "reserved".equals(ticket.getStatus())) {
                ticket.setStatus("available");
                ticketService.save(ticket);
            }
        }
        cartService.deleteCart(id);
        return "redirect:/cart";
    }
}
