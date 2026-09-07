package com.example.stadiumtickets.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.stadiumtickets.model.Event;
import com.example.stadiumtickets.service.EventService;
import com.example.stadiumtickets.service.GametypeService;
import com.example.stadiumtickets.service.VenueService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/events")
public class EventController {

    @Autowired
    private EventService eventService;
    
    @Autowired
    private VenueService venueService;
    
    @Autowired
    private GametypeService gametypeService;

    @GetMapping
    public String listEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String title,
            Model model) {
        
        Page<Event> eventsPage;
        
        if (id != null) {
            Event event = eventService.findById(id);
            List<Event> eventList = event != null ? List.of(event) : List.of();
            eventsPage = new PageImpl<>(eventList);
            model.addAttribute("searchId", id);
        } else if (title != null && !title.isEmpty()) {
            eventsPage = eventService.findByTitle(title, page, size);
            model.addAttribute("searchTitle", title);
        } else {
            eventsPage = eventService.findAll(page, size);
        }
        
        model.addAttribute("events", eventsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", eventsPage.getTotalPages());
        model.addAttribute("totalItems", eventsPage.getTotalElements());
        model.addAttribute("size", size);
        
        return "admin/events/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("event", new Event());
        model.addAttribute("venues", venueService.findAll());
        model.addAttribute("gametypes", gametypeService.findAll());
        return "admin/events/add";
    }

    @PostMapping("/add")
    public String saveEvent(@Valid @ModelAttribute Event event, 
                            BindingResult result,
                            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("venues", venueService.findAll());
            model.addAttribute("gametypes", gametypeService.findAll());
            return "admin/events/add";
        }
        // Загружаем связанные объекты из базы данных
        if (event.getVenue() != null && event.getVenue().getId() > 0) {
            event.setVenue(venueService.findById(event.getVenue().getId()));
        }
        if (event.getGametype() != null && event.getGametype().getId() > 0) {
            event.setGametype(gametypeService.findById(event.getGametype().getId()));
        }
        eventService.save(event);
        return "redirect:/admin/events";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Event event = eventService.findById(id);
        if (event != null) {
            model.addAttribute("event", event);
            model.addAttribute("venues", venueService.findAll());
            model.addAttribute("gametypes", gametypeService.findAll());
            return "admin/events/edit";
        }
        return "redirect:/admin/events";
    }

    @PostMapping("/edit")
    public String updateEvent(@Valid @ModelAttribute Event event,
                              BindingResult result,
                              Model model) {
        if (result.hasErrors()) {
            model.addAttribute("venues", venueService.findAll());
            model.addAttribute("gametypes", gametypeService.findAll());
            return "admin/events/edit";
        }
        // Загружаем связанные объекты из базы данных
        if (event.getVenue() != null && event.getVenue().getId() > 0) {
            event.setVenue(venueService.findById(event.getVenue().getId()));
        }
        if (event.getGametype() != null && event.getGametype().getId() > 0) {
            event.setGametype(gametypeService.findById(event.getGametype().getId()));
        }
        eventService.save(event);
        return "redirect:/admin/events";
    }

    @GetMapping("/delete/{id}")
    public String deleteEvent(@PathVariable int id) {
        eventService.deleteEvent(id);
        return "redirect:/admin/events";
    }
}