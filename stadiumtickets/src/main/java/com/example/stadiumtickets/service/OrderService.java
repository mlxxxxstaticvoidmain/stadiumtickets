package com.example.stadiumtickets.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.stadiumtickets.model.Order;

public interface OrderService {
    List<Order> findAll();
    Page<Order> findAll(int page, int size);
    Order findById(int id);
    List<Order> findByTicketId(int ticketId);
    Page<Order> findByTicketId(int ticketId, int page, int size);
    List<Order> findByEmployeeId(int employeeId);
    Page<Order> findByEmployeeId(int employeeId, int page, int size);
    long getTotalCount();
    long getFilteredCountByTicketId(int ticketId);
    long getFilteredCountByEmployeeId(int employeeId);
    Order save(Order order);
    void deleteOrder(int id);
}