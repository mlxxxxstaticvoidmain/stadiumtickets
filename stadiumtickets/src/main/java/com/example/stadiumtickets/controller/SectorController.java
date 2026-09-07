package com.example.stadiumtickets.controller;

import com.example.stadiumtickets.model.Sector;
import com.example.stadiumtickets.service.SectorService;
import com.example.stadiumtickets.service.VenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/sectors")
public class SectorController {

    @Autowired
    private SectorService sectorService;
    
    @Autowired
    private VenueService venueService;

    @GetMapping
    public String listSectors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String venueName,
            @RequestParam(required = false) String name,
            Model model) {
        
        Page<Sector> sectorPage;
        
        if (id != null) {
            Sector sector = sectorService.findById(id);
            List<Sector> sectorList = sector != null ? List.of(sector) : List.of();
            sectorPage = new PageImpl<>(sectorList);
            model.addAttribute("search", "ID: " + id);
        } else if (venueName != null && !venueName.isEmpty()) {
            sectorPage = sectorService.findByVenueName(venueName, page, size);
            model.addAttribute("search", "Площадка: " + venueName);
        } else if (name != null && !name.isEmpty()) {
            sectorPage = sectorService.findBySectorName(name, page, size);
            model.addAttribute("search", "Название: " + name);
        } else {
            sectorPage = sectorService.findAll(page, size);
        }
        
        // Заполняем venueName для каждого сектора
        List<Sector> sectors = sectorPage.getContent();
        sectors.forEach(sector -> {
            if (sector.getVenue() != null) {
                sector.setVenueName(sector.getVenue().getNameVenue());
            }
        });
        
        model.addAttribute("sectors", sectors);
        model.addAttribute("venues", venueService.findAll());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", sectorPage.getTotalPages());
        model.addAttribute("totalItems", sectorPage.getTotalElements());
        model.addAttribute("size", size);
        
        return "admin/sectors/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("sector", new Sector());
        model.addAttribute("venues", venueService.findAll());
        return "admin/sectors/add";
    }

    @PostMapping("/add")
    public String addSector(@RequestParam int venueId,
                            @RequestParam String sectorName,
                            @RequestParam String zone) {
        Sector sector = new Sector();
        sector.setVenue(venueService.findById(venueId));
        sector.setSectorName(sectorName);
        sector.setZone(zone);
        sectorService.save(sector);
        return "redirect:/admin/sectors";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Sector sector = sectorService.findById(id);
        model.addAttribute("sector", sector);
        model.addAttribute("venues", venueService.findAll());
        return "admin/sectors/edit";
    }

    @PostMapping("/edit")
    public String updateSector(@RequestParam int id,
                               @RequestParam int venueId,
                               @RequestParam String sectorName,
                               @RequestParam String zone) {
        Sector sector = sectorService.findById(id);
        sector.setVenue(venueService.findById(venueId));
        sector.setSectorName(sectorName);
        sector.setZone(zone);
        sectorService.save(sector);
        return "redirect:/admin/sectors";
    }

    @GetMapping("/delete/{id}")
    public String deleteSector(@PathVariable int id) {
        sectorService.deleteSector(id);
        return "redirect:/admin/sectors";
    }
}