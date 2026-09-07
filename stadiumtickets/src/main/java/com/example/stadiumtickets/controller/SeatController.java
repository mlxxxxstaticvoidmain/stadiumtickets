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

import com.example.stadiumtickets.model.Seat;
import com.example.stadiumtickets.model.Sector;
import com.example.stadiumtickets.model.Venue;
import com.example.stadiumtickets.service.SeatService;
import com.example.stadiumtickets.service.SectorService;
import com.example.stadiumtickets.service.VenueService;

@Controller
@RequestMapping("/admin/seats")
public class SeatController {

    @Autowired
    private SeatService seatService;
    
    @Autowired
    private SectorService sectorService;
    
    @Autowired
    private VenueService venueService;

    @GetMapping
    public String listSeats(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) Integer sectorId,
            @RequestParam(required = false) Integer rowNumber,
            @RequestParam(required = false) Integer seatNumber,
            Model model) {
        
        Page<Seat> seatPage;
        
        if (id != null) {
            Seat seat = seatService.findById(id);
            List<Seat> seatList = seat != null ? List.of(seat) : List.of();
            seatPage = new PageImpl<>(seatList);
            model.addAttribute("search", "ID: " + id);
        } else if (sectorId != null) {
            seatPage = seatService.findBySectorId(sectorId, page, size);
            model.addAttribute("search", "Сектор ID: " + sectorId);
        } else if (rowNumber != null) {
            seatPage = seatService.findByRowNumber(rowNumber, page, size);
            model.addAttribute("search", "Ряд: " + rowNumber);
        } else if (seatNumber != null) {
            seatPage = seatService.findBySeatNumber(seatNumber, page, size);
            model.addAttribute("search", "Номер места: " + seatNumber);
        } else {
            seatPage = seatService.findAll(page, size);
        }
        
        List<Seat> seats = seatPage.getContent();
        for (Seat seat : seats) {
            Sector sector = seat.getSector();
            if (sector != null) {
                seat.setSectorName(sector.getSectorName());
                Venue venue = sector.getVenue();
                if (venue != null) {
                    seat.setVenueName(venue.getNameVenue());
                }
                seat.setFullSeatInfo(sector.getSectorName() + ", ряд " + seat.getRowNumber() + ", место " + seat.getSeatNumber());
            }
        }
        
        model.addAttribute("seats", seats);
        model.addAttribute("sectors", sectorService.findAll());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", seatPage.getTotalPages());
        model.addAttribute("totalItems", seatPage.getTotalElements());
        model.addAttribute("size", size);
        
        return "admin/seats/list";
    }
    
    @GetMapping("/search/id")
    public String searchById(@RequestParam int id,
                             @RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "10") int size,
                             Model model) {
        Seat seat = seatService.findById(id);
        List<Seat> seatList = seat != null ? List.of(seat) : List.of();
        Page<Seat> seatPage = new PageImpl<>(seatList);
        
        for (Seat s : seatList) {
            Sector sector = s.getSector();
            if (sector != null) {
                s.setSectorName(sector.getSectorName());
                Venue venue = sector.getVenue();
                if (venue != null) {
                    s.setVenueName(venue.getNameVenue());
                }
                s.setFullSeatInfo(sector.getSectorName() + ", ряд " + s.getRowNumber() + ", место " + s.getSeatNumber());
            }
        }
        
        model.addAttribute("seats", seatList);
        model.addAttribute("sectors", sectorService.findAll());
        model.addAttribute("currentPage", 0);
        model.addAttribute("totalPages", 1);
        model.addAttribute("totalItems", seatList.size());
        model.addAttribute("size", size);
        model.addAttribute("search", "ID: " + id);
        return "admin/seats/list";
    }
    
    @GetMapping("/search/sector")
    public String searchBySector(@RequestParam int sectorId,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 Model model) {
        Page<Seat> seatPage = seatService.findBySectorId(sectorId, page, size);
        
        List<Seat> seats = seatPage.getContent();
        for (Seat seat : seats) {
            Sector sector = seat.getSector();
            if (sector != null) {
                seat.setSectorName(sector.getSectorName());
                Venue venue = sector.getVenue();
                if (venue != null) {
                    seat.setVenueName(venue.getNameVenue());
                }
                seat.setFullSeatInfo(sector.getSectorName() + ", ряд " + seat.getRowNumber() + ", место " + seat.getSeatNumber());
            }
        }
        
        Sector sector = sectorService.findById(sectorId);
        String sectorName = sector != null ? sector.getSectorName() : String.valueOf(sectorId);
        
        model.addAttribute("seats", seats);
        model.addAttribute("sectors", sectorService.findAll());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", seatPage.getTotalPages());
        model.addAttribute("totalItems", seatPage.getTotalElements());
        model.addAttribute("size", size);
        model.addAttribute("search", "Сектор: " + sectorName);
        return "admin/seats/list";
    }
    
    @GetMapping("/search/row")
    public String searchByRow(@RequestParam int rowNumber,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size,
                              Model model) {
        Page<Seat> seatPage = seatService.findByRowNumber(rowNumber, page, size);
        
        List<Seat> seats = seatPage.getContent();
        for (Seat seat : seats) {
            Sector sector = seat.getSector();
            if (sector != null) {
                seat.setSectorName(sector.getSectorName());
                Venue venue = sector.getVenue();
                if (venue != null) {
                    seat.setVenueName(venue.getNameVenue());
                }
                seat.setFullSeatInfo(sector.getSectorName() + ", ряд " + seat.getRowNumber() + ", место " + seat.getSeatNumber());
            }
        }
        
        model.addAttribute("seats", seats);
        model.addAttribute("sectors", sectorService.findAll());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", seatPage.getTotalPages());
        model.addAttribute("totalItems", seatPage.getTotalElements());
        model.addAttribute("size", size);
        model.addAttribute("search", "Ряд: " + rowNumber);
        return "admin/seats/list";
    }
    
    @GetMapping("/search/seat")
    public String searchBySeat(@RequestParam int seatNumber,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               Model model) {
        Page<Seat> seatPage = seatService.findBySeatNumber(seatNumber, page, size);
        
        List<Seat> seats = seatPage.getContent();
        for (Seat seat : seats) {
            Sector sector = seat.getSector();
            if (sector != null) {
                seat.setSectorName(sector.getSectorName());
                Venue venue = sector.getVenue();
                if (venue != null) {
                    seat.setVenueName(venue.getNameVenue());
                }
                seat.setFullSeatInfo(sector.getSectorName() + ", ряд " + seat.getRowNumber() + ", место " + seat.getSeatNumber());
            }
        }
        
        model.addAttribute("seats", seats);
        model.addAttribute("sectors", sectorService.findAll());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", seatPage.getTotalPages());
        model.addAttribute("totalItems", seatPage.getTotalElements());
        model.addAttribute("size", size);
        model.addAttribute("search", "Номер места: " + seatNumber);
        return "admin/seats/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("seat", new Seat());
        List<Sector> sectors = sectorService.findAll();
        for (Sector sector : sectors) {
            Venue venue = sector.getVenue();
            if (venue != null) {
                sector.setVenueName(venue.getNameVenue());
            }
        }
        model.addAttribute("sectors", sectors);
        return "admin/seats/add";
    }

    @PostMapping("/add")
    public String addSeat(@RequestParam int sectorId, 
                          @RequestParam int rowNumber, 
                          @RequestParam int seatNumber) {
        Seat seat = new Seat();
        seat.setSector(sectorService.findById(sectorId));
        seat.setRowNumber(rowNumber);
        seat.setSeatNumber(seatNumber);
        seatService.save(seat);
        return "redirect:/admin/seats";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Seat seat = seatService.findById(id);
        model.addAttribute("seat", seat);
        
        List<Sector> sectors = sectorService.findAll();
        for (Sector sector : sectors) {
            Venue venue = sector.getVenue();
            if (venue != null) {
                sector.setVenueName(venue.getNameVenue());
            }
        }
        model.addAttribute("sectors", sectors);
        return "admin/seats/edit";
    }

    @PostMapping("/edit")
    public String updateSeat(@RequestParam int id, 
                             @RequestParam int sectorId, 
                             @RequestParam int rowNumber, 
                             @RequestParam int seatNumber) {
        Seat seat = seatService.findById(id);
        seat.setSector(sectorService.findById(sectorId));
        seat.setRowNumber(rowNumber);
        seat.setSeatNumber(seatNumber);
        seatService.save(seat);
        return "redirect:/admin/seats";
    }

    @GetMapping("/delete/{id}")
    public String deleteSeat(@PathVariable int id) {
        seatService.deleteSeat(id);
        return "redirect:/admin/seats";
    }
}