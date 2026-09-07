package com.example.stadiumtickets.controller;

import java.util.List;

import com.example.stadiumtickets.model.Ticket;
import com.example.stadiumtickets.model.Seat;
import com.example.stadiumtickets.service.TicketService;
import com.example.stadiumtickets.service.EventService;
import com.example.stadiumtickets.service.SeatService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;
    
    @Autowired
    private EventService eventService;
    
    @Autowired
    private SeatService seatService;

    @GetMapping
    public String listTickets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String eventTitle,
            @RequestParam(required = false) String status,
            Model model) {
        
        Page<Ticket> ticketsPage;
        
        if (id != null) {
            Ticket ticket = ticketService.findById(id);
            List<Ticket> ticketList = ticket != null ? List.of(ticket) : List.of();
            ticketsPage = new PageImpl<>(ticketList);
            model.addAttribute("search", "ID: " + id);
        } else if (eventTitle != null && !eventTitle.isEmpty()) {
            ticketsPage = ticketService.findByEventTitle(eventTitle, page, size);
            model.addAttribute("search", "Мероприятие: " + eventTitle);
        } else if (status != null && !status.isEmpty()) {
            ticketsPage = ticketService.findByStatus(status, page, size);
            model.addAttribute("search", "Статус: " + status);
        } else {
            ticketsPage = ticketService.findAll(page, size);
        }
        
        List<Ticket> tickets = ticketsPage.getContent();
        for (Ticket ticket : tickets) {
            if (ticket.getEvent() != null) {
                ticket.setEventTitle(ticket.getEvent().getTitle());
            }
            if (ticket.getSeat() != null) {
                ticket.setSeatNumber(ticket.getSeat().getSeatNumber());
                ticket.setRowNumber(ticket.getSeat().getRowNumber());
                if (ticket.getSeat().getSector() != null) {
                    ticket.setSectorName(ticket.getSeat().getSector().getSectorName());
                }
            }
        }
        model.addAttribute("tickets", tickets);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", ticketsPage.getTotalPages());
        model.addAttribute("totalItems", ticketsPage.getTotalElements());
        model.addAttribute("size", size);
        
        return "admin/tickets/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("ticket", new Ticket());
        model.addAttribute("events", eventService.findAll());
        List<Seat> seats = seatService.findAll();
        for (Seat seat : seats) {
            if (seat.getSector() != null) {
                seat.setSectorName(seat.getSector().getSectorName());
            }
        }
        model.addAttribute("seats", seats);
        return "admin/tickets/add";
    }

    @PostMapping("/add")
    public String saveTicket(@RequestParam int eventId, 
                             @RequestParam int seatId,
                             @RequestParam double price,
                             @RequestParam String status,
                             @RequestParam(defaultValue = "false") boolean vipAccess,
                             Model model) {
        Ticket ticket = new Ticket();
        ticket.setEvent(eventService.findById(eventId));
        ticket.setSeat(seatService.findById(seatId));
        ticket.setPrice(price);
        ticket.setStatus(status);
        ticket.setVipAccess(vipAccess);

        ticketService.save(ticket);
        return "redirect:/admin/tickets";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Ticket ticket = ticketService.findById(id);
        if (ticket != null) {
            model.addAttribute("ticket", ticket);
            model.addAttribute("events", eventService.findAll());
            List<Seat> seats = seatService.findAll();
            for (Seat seat : seats) {
                if (seat.getSector() != null) {
                    seat.setSectorName(seat.getSector().getSectorName());
                }
            }
            model.addAttribute("seats", seats);
            return "admin/tickets/edit";
        }
        return "redirect:/admin/tickets";
    }

    @PostMapping("/edit")
    public String updateTicket(@RequestParam int id,
                               @RequestParam int eventId,
                               @RequestParam int seatId,
                               @RequestParam double price,
                               @RequestParam String status,
                               @RequestParam(defaultValue = "false") boolean vipAccess,
                               Model model) {
        Ticket existingTicket = ticketService.findById(id);
        if (existingTicket == null) {
            return "redirect:/admin/tickets";
        }

        existingTicket.setEvent(eventService.findById(eventId));
        existingTicket.setSeat(seatService.findById(seatId));
        existingTicket.setPrice(price);
        existingTicket.setStatus(status);
        existingTicket.setVipAccess(vipAccess);

        ticketService.save(existingTicket);
        return "redirect:/admin/tickets";
    }

    @GetMapping("/delete/{id}")
    public String deleteTicket(@PathVariable int id) {
        ticketService.deleteTicket(id);
        return "redirect:/admin/tickets";
    }
}