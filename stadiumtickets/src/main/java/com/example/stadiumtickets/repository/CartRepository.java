package com.example.stadiumtickets.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.stadiumtickets.model.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    List<Cart> findByAccountId(int accountId);
    List<Cart> findByTicketId(int ticketId);
    Optional<Cart> findByAccountIdAndTicketId(int accountId, int ticketId);
    Page<Cart> findByAccountId(int accountId, Pageable pageable);
    Page<Cart> findByTicketId(int ticketId, Pageable pageable);
    Page<Cart> findByAccount_LastNameContainingIgnoreCase(String lastName, Pageable pageable);
    Page<Cart> findByAccount_Id(int accountId, Pageable pageable);
    Page<Cart> findByTicket_Id(int ticketId, Pageable pageable);
}