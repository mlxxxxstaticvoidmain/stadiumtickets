package com.example.stadiumtickets.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.example.stadiumtickets.model.Cart;

public interface CartService {
    List<Cart> findAll();
    Page<Cart> findAll(int page, int size);
    Cart findById(int id);
    List<Cart> findByAccountId(int accountId);
    Page<Cart> findByAccountId(int accountId, int page, int size);
    Page<Cart> findByAccountLastName(String lastName, int page, int size);
    List<Cart> findByTicketId(int ticketId);
    Page<Cart> findByTicketId(int ticketId, int page, int size);
    long getTotalCount();
    long getFilteredCountByAccountId(int accountId);
    long getFilteredCountByTicketId(int ticketId);
    Cart save(Cart cart);
    void deleteCart(int id);
    void deleteByAccountId(int accountId);
    Optional<Cart> findByAccountIdAndTicketId(int accountId, int ticketId);
}