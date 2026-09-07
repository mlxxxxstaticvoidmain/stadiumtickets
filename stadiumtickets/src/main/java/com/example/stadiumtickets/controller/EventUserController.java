package com.example.stadiumtickets.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.stadiumtickets.model.Event;
import com.example.stadiumtickets.service.EventService;
import com.example.stadiumtickets.service.TicketService;

@Controller
@RequestMapping("/myevents")
public class EventUserController {

    @Autowired
    private EventService eventService;
    
    @Autowired
    private TicketService ticketService;

    @GetMapping
    public String listEvents(Model model) {
        model.addAttribute("events", eventService.findAll());
        return "events/list";
    }
    
    @GetMapping("/{id}")
    public String eventDetails(@PathVariable int id, Model model) {
        Event event = eventService.findById(id);
        model.addAttribute("event", event);
        model.addAttribute("tickets", ticketService.findByEventId(id));
        return "events/details";
    }
    
    @GetMapping("/{id}/buy")
    public String buyTicket(@PathVariable int id, @RequestParam int ticketId, Model model) { 
        return "redirect:/cart";
    }
}