package com.example.stadiumtickets.controller;

import java.time.LocalDateTime;
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

import com.example.stadiumtickets.model.Order;
import com.example.stadiumtickets.model.Ticket;
import com.example.stadiumtickets.service.AccountService;
import com.example.stadiumtickets.service.EmployeeService;
import com.example.stadiumtickets.service.OrderService;
import com.example.stadiumtickets.service.TicketService;

@Controller
@RequestMapping("/admin/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;
    
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private TicketService ticketService;
    
    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public String listOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) Integer ticketId,
            @RequestParam(required = false) Integer employeeId,
            Model model) {
        
        Page<Order> orderPage;
        
        if (id != null) {
            Order order = orderService.findById(id);
            List<Order> orderList = order != null ? List.of(order) : List.of();
            orderPage = new PageImpl<>(orderList);
            model.addAttribute("search", "ID: " + id);
            model.addAttribute("searchId", id);
        } else if (ticketId != null) {
            orderPage = orderService.findByTicketId(ticketId, page, size);
            model.addAttribute("search", "ID билета: " + ticketId);
            model.addAttribute("searchTicketId", ticketId);
        } else if (employeeId != null) {
            orderPage = orderService.findByEmployeeId(employeeId, page, size);
            model.addAttribute("search", "Продавец ID: " + employeeId);
            model.addAttribute("searchEmployeeId", employeeId);
        } else {
            orderPage = orderService.findAll(page, size);
        }
        
        model.addAttribute("orders", orderPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", orderPage.getTotalPages());
        model.addAttribute("totalItems", orderPage.getTotalElements());
        model.addAttribute("size", size);
        
        return "admin/orders/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("order", new Order());
        model.addAttribute("accounts", accountService.findAll());
        List<Ticket> availableTickets = ticketService.findByStatus("available");
        model.addAttribute("tickets", availableTickets);
        model.addAttribute("employees", employeeService.findAll());
        
        model.addAttribute("availableTicketsCount", (long) availableTickets.size());
        
        return "admin/orders/add";
    }

    @PostMapping("/add")
    public String addOrder(@RequestParam int accountId, 
                           @RequestParam int ticketId, 
                           @RequestParam int employeeId, 
                           @RequestParam String paymentMethod, 
                           @RequestParam double totalAmount,
                           RedirectAttributes redirectAttributes) {
        
        try {
            Ticket ticket = ticketService.findById(ticketId);
            if (ticket == null) {
                redirectAttributes.addFlashAttribute("error", "Билет не найден!");
                return "redirect:/admin/orders/add";
            }
            
            if ("sold".equals(ticket.getStatus())) {
                redirectAttributes.addFlashAttribute("error", "Билет уже продан!");
                return "redirect:/admin/orders/add";
            }
            
            Order order = new Order();
            order.setAccount(accountService.findById(accountId));
            order.setTicket(ticketService.findById(ticketId));
            order.setEmployee(employeeService.findById(employeeId));
            order.setPaymentMethod(paymentMethod);
            order.setTotalAmount(totalAmount);
            order.setOrderDate(LocalDateTime.now());
            
            orderService.save(order);
            
            ticket.setStatus("sold");
            ticketService.save(ticket);
            
            redirectAttributes.addFlashAttribute("success", "Заказ успешно создан!");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при создании заказа: " + e.getMessage());
        }
        
        return "redirect:/admin/orders";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
        Order order = orderService.findById(id);
        if (order == null) {
            redirectAttributes.addFlashAttribute("error", "Заказ не найден!");
            return "redirect:/admin/orders";
        }
        
        model.addAttribute("order", order);
        model.addAttribute("accounts", accountService.findAll());
        
        List<Ticket> allTickets = ticketService.findAll();
        model.addAttribute("tickets", allTickets);
        model.addAttribute("employees", employeeService.findAll());
        
        long availableTicketsCount = allTickets.stream()
                .filter(t -> "available".equals(t.getStatus()) || t.getId() == (order.getTicket() != null ? order.getTicket().getId() : -1))
                .count();
        model.addAttribute("availableTicketsCount", availableTicketsCount);
        
        return "admin/orders/edit";
    }

    @PostMapping("/edit")
    public String updateOrder(@RequestParam int id, 
                              @RequestParam int accountId, 
                              @RequestParam int ticketId, 
                              @RequestParam int employeeId, 
                              @RequestParam String paymentMethod, 
                              @RequestParam double totalAmount, 
                              @RequestParam String orderDate,
                              RedirectAttributes redirectAttributes) {
        
        try {
            Order order = orderService.findById(id);
            if (order == null) {
                redirectAttributes.addFlashAttribute("error", "Заказ не найден!");
                return "redirect:/admin/orders";
            }
            
            int oldTicketId = order.getTicket() != null ? order.getTicket().getId() : -1;
            
            if (oldTicketId != ticketId) {
                Ticket oldTicket = ticketService.findById(oldTicketId);
                if (oldTicket != null && "sold".equals(oldTicket.getStatus())) {
                    oldTicket.setStatus("available");
                    ticketService.save(oldTicket);
                }
                
                Ticket newTicket = ticketService.findById(ticketId);
                if (newTicket == null) {
                    redirectAttributes.addFlashAttribute("error", "Новый билет не найден!");
                    return "redirect:/admin/orders/edit/" + id;
                }
                
                if ("sold".equals(newTicket.getStatus())) {
                    redirectAttributes.addFlashAttribute("error", "Новый билет уже продан!");
                    return "redirect:/admin/orders/edit/" + id;
                }
                
                newTicket.setStatus("sold");
                ticketService.save(newTicket);
            }
            
            order.setAccount(accountService.findById(accountId));
            order.setTicket(ticketService.findById(ticketId));
            order.setEmployee(employeeService.findById(employeeId));
            order.setPaymentMethod(paymentMethod);
            order.setTotalAmount(totalAmount);
            order.setOrderDate(LocalDateTime.parse(orderDate));
            
            orderService.save(order);
            
            redirectAttributes.addFlashAttribute("success", "Заказ успешно обновлен!");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при обновлении заказа: " + e.getMessage());
        }
        
        return "redirect:/admin/orders";
    }

    @PostMapping("/delete/{id}")
    public String deleteOrder(@PathVariable int id, 
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size,
                              @RequestParam(required = false) Integer searchId,
                              @RequestParam(required = false) Integer searchTicketId,
                              @RequestParam(required = false) Integer searchEmployeeId,
                              RedirectAttributes redirectAttributes) {
        
        try {
            Order order = orderService.findById(id);
            if (order != null) {
                Ticket ticket = ticketService.findById(order.getTicket() != null ? order.getTicket().getId() : -1);
                if (ticket != null && "sold".equals(ticket.getStatus())) {
                    ticket.setStatus("available");
                    ticketService.save(ticket);
                }
                orderService.deleteOrder(id);
                redirectAttributes.addFlashAttribute("success", "Заказ успешно удален!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Заказ не найден!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при удалении заказа: " + e.getMessage());
        }
        
        StringBuilder redirect = new StringBuilder("redirect:/admin/orders?page=" + page + "&size=" + size);
        if (searchId != null) {
            redirect.append("&id=").append(searchId);
        }
        if (searchTicketId != null) {
            redirect.append("&ticketId=").append(searchTicketId);
        }
        if (searchEmployeeId != null) {
            redirect.append("&employeeId=").append(searchEmployeeId);
        }
        
        return redirect.toString();
    }
    
    @GetMapping("/delete/{id}")
    public String deleteOrderGet(@PathVariable int id, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "Используйте POST запрос для удаления!");
        return "redirect:/admin/orders";
    }
}