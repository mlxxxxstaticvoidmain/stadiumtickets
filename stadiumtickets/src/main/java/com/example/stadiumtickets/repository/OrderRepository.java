package com.example.stadiumtickets.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.stadiumtickets.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByAccountId(int accountId);
    List<Order> findByTicketId(int ticketId);
    List<Order> findByEmployeeId(int employeeId);
    Page<Order> findByAccountId(int accountId, Pageable pageable);
    Page<Order> findByTicketId(int ticketId, Pageable pageable);
    Page<Order> findByEmployeeId(int employeeId, Pageable pageable);
    Page<Order> findByAccount_Id(int accountId, Pageable pageable);
    Page<Order> findByTicket_Id(int ticketId, Pageable pageable);
    Page<Order> findByEmployee_Id(int employeeId, Pageable pageable);
}