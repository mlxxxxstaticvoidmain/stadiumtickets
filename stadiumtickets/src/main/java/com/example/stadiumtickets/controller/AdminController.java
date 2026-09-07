package com.example.stadiumtickets.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.stadiumtickets.model.Account;
import com.example.stadiumtickets.model.Event;
import com.example.stadiumtickets.service.AccountService;
import com.example.stadiumtickets.service.BankService;
import com.example.stadiumtickets.service.CartService;
import com.example.stadiumtickets.service.EmployeeService;
import com.example.stadiumtickets.service.EventService;
import com.example.stadiumtickets.service.GametypeService;
import com.example.stadiumtickets.service.OrderService;
import com.example.stadiumtickets.service.RoleService;
import com.example.stadiumtickets.service.SeatService;
import com.example.stadiumtickets.service.SectorService;
import com.example.stadiumtickets.service.TicketService;
import com.example.stadiumtickets.service.VenueService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAnyAuthority('Администратор', 'Менеджер')")
public class AdminController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private EventService eventService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private BankService bankService;

    @Autowired
    private VenueService venueService;

    @Autowired
    private GametypeService gametypeService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private SeatService seatService;

    @Autowired
    private SectorService sectorService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @GetMapping
    public String admin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(">>> ADMIN PAGE: user=" + auth.getName() + ", authorities=" + auth.getAuthorities());
        return "admin";
    }


}
