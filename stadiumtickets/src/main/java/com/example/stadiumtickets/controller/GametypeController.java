package com.example.stadiumtickets.controller;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.stadiumtickets.model.Gametype;
import com.example.stadiumtickets.service.GametypeService;

@Controller
@RequestMapping("/admin/gametypes")
public class GametypeController {

    @Autowired
    private GametypeService gametypeService;

    @GetMapping
    public String listGametypes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String name,
            Model model) {
        
        Page<Gametype> gametypePage;
        
        if (id != null) {
            Gametype gametype = gametypeService.findById(id);
            List<Gametype> gametypeList = gametype != null ? List.of(gametype) : List.of();
            gametypePage = new PageImpl<>(gametypeList);
            model.addAttribute("search", "ID: " + id);
            model.addAttribute("searchId", id);
        } else if (name != null && !name.isEmpty()) {
            gametypePage = gametypeService.findByName(name, page, size);
            model.addAttribute("search", "Название: " + name);
            model.addAttribute("searchName", name);
        } else {
            gametypePage = gametypeService.findAll(page, size);
        }
        
        model.addAttribute("gametypes", gametypePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", gametypePage.getTotalPages());
        model.addAttribute("totalItems", gametypePage.getTotalElements());
        model.addAttribute("size", size);
        
        return "admin/gametypes/list";
    }

    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("gametype", new Gametype());
        return "admin/gametypes/add";
    }

    @PostMapping("/new")
    public String addGametype(@RequestParam String tournamentName, RedirectAttributes redirectAttributes) {
        try {
            Gametype gametype = new Gametype();
            gametype.setTournamentName(tournamentName);
            gametypeService.save(gametype);
            redirectAttributes.addFlashAttribute("success", "Тип игры успешно добавлен!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при добавлении: " + e.getMessage());
        }
        return "redirect:/admin/gametypes";
    }

    @GetMapping("/add")
    public String showAddFormAlt(Model model) {
        model.addAttribute("gametype", new Gametype());
        return "admin/gametypes/add";
    }

    @PostMapping("/add")
    public String addGametypeAlt(@RequestParam String tournamentName, RedirectAttributes redirectAttributes) {
        try {
            Gametype gametype = new Gametype();
            gametype.setTournamentName(tournamentName);
            gametypeService.save(gametype);
            redirectAttributes.addFlashAttribute("success", "Тип игры успешно добавлен!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при добавлении: " + e.getMessage());
        }
        return "redirect:/admin/gametypes";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
        Gametype gametype = gametypeService.findById(id);
        if (gametype == null) {
            redirectAttributes.addFlashAttribute("error", "Тип игры не найден!");
            return "redirect:/admin/gametypes";
        }
        model.addAttribute("gametype", gametype);
        return "admin/gametypes/edit";
    }

    @PostMapping("/edit")
    public String updateGametype(@RequestParam int id, @RequestParam String tournamentName, RedirectAttributes redirectAttributes) {
        try {
            Gametype gametype = gametypeService.findById(id);
            if (gametype == null) {
                redirectAttributes.addFlashAttribute("error", "Тип игры не найден!");
                return "redirect:/admin/gametypes";
            }
            gametype.setTournamentName(tournamentName);
            gametypeService.save(gametype);
            redirectAttributes.addFlashAttribute("success", "Тип игры успешно обновлен!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при обновлении: " + e.getMessage());
        }
        return "redirect:/admin/gametypes";
    }

    @GetMapping("/delete/{id}")
    public String deleteGametype(@PathVariable int id, 
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size,
                                  @RequestParam(required = false) Integer searchId,
                                  @RequestParam(required = false) String searchName,
                                  RedirectAttributes redirectAttributes) {
        
        boolean canDelete = gametypeService.canDeleteGametype(id);
        
        if (!canDelete) {
            String events = gametypeService.getEventsWithGametype(id);
            redirectAttributes.addFlashAttribute("error", 
                "Невозможно удалить тип игры! Он используется в мероприятиях: " + events);
        } else {
            try {
                gametypeService.deleteGametype(id);
                redirectAttributes.addFlashAttribute("success", "Тип игры успешно удален!");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", "Ошибка при удалении: " + e.getMessage());
            }
        }
        
        StringBuilder redirect = new StringBuilder("redirect:/admin/gametypes?page=" + page + "&size=" + size);
        if (searchId != null) {
            redirect.append("&id=").append(searchId);
        }
        if (searchName != null && !searchName.isEmpty()) {
            redirect.append("&name=").append(searchName);
        }
        
        return redirect.toString();
    }
    
    @PostMapping("/delete/{id}")
    public String deleteGametypePost(@PathVariable int id,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size,
                                      @RequestParam(required = false) Integer searchId,
                                      @RequestParam(required = false) String searchName,
                                      RedirectAttributes redirectAttributes) {
        
        boolean canDelete = gametypeService.canDeleteGametype(id);
        
        if (!canDelete) {
            String events = gametypeService.getEventsWithGametype(id);
            redirectAttributes.addFlashAttribute("error", 
                "Невозможно удалить тип игры! Он используется в мероприятиях: " + events);
        } else {
            try {
                gametypeService.deleteGametype(id);
                redirectAttributes.addFlashAttribute("success", "Тип игры успешно удален!");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", "Ошибка при удалении: " + e.getMessage());
            }
        }
        
        StringBuilder redirect = new StringBuilder("redirect:/admin/gametypes?page=" + page + "&size=" + size);
        if (searchId != null) {
            redirect.append("&id=").append(searchId);
        }
        if (searchName != null && !searchName.isEmpty()) {
            redirect.append("&name=").append(searchName);
        }
        
        return redirect.toString();
    }
}