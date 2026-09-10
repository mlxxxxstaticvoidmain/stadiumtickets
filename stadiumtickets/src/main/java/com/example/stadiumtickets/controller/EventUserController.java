package com.example.stadiumtickets.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.stadiumtickets.dto.SeatCellDto;
import com.example.stadiumtickets.dto.SeatRowDto;
import com.example.stadiumtickets.dto.SectorMapDto;
import com.example.stadiumtickets.model.Account;
import com.example.stadiumtickets.model.Event;
import com.example.stadiumtickets.model.Order;
import com.example.stadiumtickets.model.Seat;
import com.example.stadiumtickets.model.Sector;
import com.example.stadiumtickets.model.Ticket;
import com.example.stadiumtickets.service.AccountService;
import com.example.stadiumtickets.service.EventService;
import com.example.stadiumtickets.service.OrderService;
import com.example.stadiumtickets.service.SeatService;
import com.example.stadiumtickets.service.SectorService;
import com.example.stadiumtickets.service.TicketService;

@Controller
@RequestMapping("/myevents")
public class EventUserController {

    @Autowired
    private EventService eventService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private SectorService sectorService;

    @Autowired
    private SeatService seatService;

    @Autowired
    private OrderService orderService;

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
    public String listEvents(Model model) {
        model.addAttribute("events", eventService.findAll());
        return "events/list";
    }

    @GetMapping("/{id}")
    public String eventDetails(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
        Event event = eventService.findById(id);
        if (event == null) {
            redirectAttributes.addFlashAttribute("error", "Мероприятие не найдено");
            return "redirect:/myevents";
        }

        List<Ticket> tickets = ticketService.findByEventId(id);
        Map<Integer, Ticket> ticketBySeat = new HashMap<>();
        for (Ticket ticket : tickets) {
            if (ticket.getSeat() != null) {
                ticketBySeat.put(ticket.getSeat().getId(), ticket);
            }
        }

        List<Sector> sectors = event.getVenue() != null
                ? sectorService.findByVenueId(event.getVenue().getId())
                : List.of();

        List<SectorMapDto> sectorMaps = new ArrayList<>();
        long availableCount = 0;
        long soldCount = 0;

        for (Sector sector : sectors) {
            List<Seat> seats = seatService.findBySectorId(sector.getId());
            Map<Integer, List<SeatCellDto>> seatsByRow = new TreeMap<>();
            for (Seat seat : seats) {
                Ticket ticket = ticketBySeat.get(seat.getId());
                seatsByRow.computeIfAbsent(seat.getRowNumber(), row -> new ArrayList<>())
                        .add(new SeatCellDto(seat, ticket));
            }

            List<SeatRowDto> rows = new ArrayList<>();
            for (Map.Entry<Integer, List<SeatCellDto>> entry : seatsByRow.entrySet()) {
                entry.getValue().sort(Comparator.comparingInt(cell -> cell.getSeat().getSeatNumber()));
                rows.add(new SeatRowDto(entry.getKey(), entry.getValue()));
                for (SeatCellDto cell : entry.getValue()) {
                    if (cell.getTicket() != null) {
                        if ("available".equals(cell.getTicket().getStatus())) {
                            availableCount++;
                        } else if ("sold".equals(cell.getTicket().getStatus())) {
                            soldCount++;
                        }
                    }
                }
            }

            sectorMaps.add(new SectorMapDto(sector, rows));
        }

        model.addAttribute("event", event);
        model.addAttribute("sectorMaps", sectorMaps);
        model.addAttribute("ticketsCount", tickets.size());
        model.addAttribute("availableCount", availableCount);
        model.addAttribute("soldCount", soldCount);
        model.addAttribute("currentUser", getCurrentUser());

        return "events/details";
    }

    @PostMapping("/{id}/buy")
    public String buyTicket(@PathVariable int id,
                            @RequestParam int ticketId,
                            RedirectAttributes redirectAttributes) {

        Account user = getCurrentUser();
        if (user == null) {
            return "redirect:/login";
        }

        Event event = eventService.findById(id);
        Ticket ticket = ticketService.findById(ticketId);

        if (event == null || ticket == null
                || ticket.getEvent() == null || ticket.getEvent().getId() != id) {
            redirectAttributes.addFlashAttribute("error", "Билет не найден");
            return "redirect:/myevents/" + id;
        }

        if (!"available".equals(ticket.getStatus())) {
            redirectAttributes.addFlashAttribute("error", "Это место уже занято. Выберите другое.");
            return "redirect:/myevents/" + id;
        }

        try {
            double amount = ticket.getPrice() > 0 ? ticket.getPrice() : event.getPrice();

            Order order = new Order();
            order.setAccount(user);
            order.setTicket(ticket);
            order.setEmployee(null);
            order.setPaymentMethod("card");
            order.setTotalAmount(amount);
            order.setOrderDate(LocalDateTime.now());
            orderService.save(order);

            ticket.setStatus("sold");
            ticketService.save(ticket);

            String place = ticket.getSeat() != null
                    ? "Ряд " + ticket.getSeat().getRowNumber() + ", место " + ticket.getSeat().getSeatNumber()
                    : "билет #" + ticket.getId();
            redirectAttributes.addFlashAttribute("success",
                    "Билет куплен! " + place + " — " + amount + " ₽. Оплата: карта.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при покупке: " + e.getMessage());
        }

        return "redirect:/myevents/" + id;
    }
}
