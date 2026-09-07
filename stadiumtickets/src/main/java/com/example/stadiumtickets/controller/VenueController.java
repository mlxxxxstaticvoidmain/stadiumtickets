package com.example.stadiumtickets.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.stadiumtickets.model.Venue;
import com.example.stadiumtickets.service.VenueService;

@Controller
@RequestMapping("/admin/venues")
public class VenueController {

    @Autowired
    private VenueService venueService;

    @GetMapping("/new")
    public String redirectNewToAdd() {
        return "redirect:/admin/venues/add";
    }

    @GetMapping
    public String listVenues(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String nameVenue,
            Model model) {
        
        Page<Venue> venuePage;
        
        if (id != null) {
            Venue venue = venueService.findById(id);
            List<Venue> venueList = venue != null ? List.of(venue) : List.of();
            venuePage = new PageImpl<>(venueList);
            model.addAttribute("search", "ID: " + id);
            model.addAttribute("searchId", id);
        } else if (nameVenue != null && !nameVenue.isEmpty()) {
            venuePage = venueService.findByNameVenue(nameVenue, page, size);
            model.addAttribute("search", "Название: " + nameVenue);
            model.addAttribute("searchName", nameVenue);
        } else {
            venuePage = venueService.findAll(page, size);
        }
        
        model.addAttribute("venues", venuePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", venuePage.getTotalPages());
        model.addAttribute("totalItems", venuePage.getTotalElements());
        model.addAttribute("size", size);
        
        return "admin/venues/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("venue", new Venue());
        return "admin/venues/add";
    }

    @PostMapping("/add")
    public String addVenue(@ModelAttribute Venue venue, RedirectAttributes redirectAttributes) {
        try {
            venueService.save(venue);
            redirectAttributes.addFlashAttribute("success", "Площадка успешно добавлена!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при добавлении: " + e.getMessage());
        }
        return "redirect:/admin/venues";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
        Venue venue = venueService.findById(id);
        if (venue == null) {
            redirectAttributes.addFlashAttribute("error", "Площадка не найдена!");
            return "redirect:/admin/venues";
        }
        model.addAttribute("venue", venue);
        return "admin/venues/edit";
    }

    @PostMapping("/edit")
    public String updateVenue(@ModelAttribute Venue venue, RedirectAttributes redirectAttributes) {
        try {
            venueService.save(venue);
            redirectAttributes.addFlashAttribute("success", "Площадка успешно обновлена!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при обновлении: " + e.getMessage());
        }
        return "redirect:/admin/venues";
    }

    @PostMapping("/delete/{id}")
    public String deleteVenue(@PathVariable int id,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size,
                              @RequestParam(required = false) Integer searchId,
                              @RequestParam(required = false) String searchName,
                              RedirectAttributes redirectAttributes) {
        try {
            venueService.deleteVenue(id);
            redirectAttributes.addFlashAttribute("success", "Площадка успешно удалена!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при удалении: " + e.getMessage());
        }
        
        StringBuilder redirect = new StringBuilder("redirect:/admin/venues?page=" + page + "&size=" + size);
        if (searchId != null) {
            redirect.append("&id=").append(searchId);
        }
        if (searchName != null && !searchName.isEmpty()) {
            redirect.append("&nameVenue=").append(searchName);
        }
        
        return redirect.toString();
    }
}