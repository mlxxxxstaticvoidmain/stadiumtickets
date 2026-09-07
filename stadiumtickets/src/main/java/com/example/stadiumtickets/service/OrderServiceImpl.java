package com.example.stadiumtickets.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.stadiumtickets.model.Order;
import com.example.stadiumtickets.repository.OrderRepository;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public Page<Order> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.findAll(pageable);
    }

    @Override
    public Order findById(int id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public List<Order> findByTicketId(int ticketId) {
        return orderRepository.findByTicketId(ticketId);
    }

    @Override
    public Page<Order> findByTicketId(int ticketId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.findByTicketId(ticketId, pageable);
    }

    @Override
    public List<Order> findByEmployeeId(int employeeId) {
        return orderRepository.findByEmployeeId(employeeId);
    }

    @Override
    public Page<Order> findByEmployeeId(int employeeId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.findByEmployeeId(employeeId, pageable);
    }

    @Override
    public long getTotalCount() {
        return orderRepository.count();
    }

    @Override
    public long getFilteredCountByTicketId(int ticketId) {
        return orderRepository.findByTicketId(ticketId).size();
    }

    @Override
    public long getFilteredCountByEmployeeId(int employeeId) {
        return orderRepository.findByEmployeeId(employeeId).size();
    }

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public void deleteOrder(int id) {
        orderRepository.deleteById(id);
    }
}