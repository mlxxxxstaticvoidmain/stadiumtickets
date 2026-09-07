package com.example.stadiumtickets.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.stadiumtickets.model.Cart;
import com.example.stadiumtickets.repository.CartRepository;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Override
    public List<Cart> findAll() {
        return cartRepository.findAll();
    }

    @Override
    public Page<Cart> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return cartRepository.findAll(pageable);
    }

    @Override
    public Cart findById(int id) {
        return cartRepository.findById(id).orElse(null);
    }

    @Override
    public List<Cart> findByAccountId(int accountId) {
        return cartRepository.findByAccountId(accountId);
    }

    @Override
    public Page<Cart> findByAccountId(int accountId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return cartRepository.findByAccountId(accountId, pageable);
    }

    @Override
    public Page<Cart> findByAccountLastName(String lastName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return cartRepository.findByAccount_LastNameContainingIgnoreCase(lastName, pageable);
    }

    @Override
    public List<Cart> findByTicketId(int ticketId) {
        return cartRepository.findByTicketId(ticketId);
    }

    @Override
    public Page<Cart> findByTicketId(int ticketId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return cartRepository.findByTicketId(ticketId, pageable);
    }

    @Override
    public long getTotalCount() {
        return cartRepository.count();
    }

    @Override
    public long getFilteredCountByAccountId(int accountId) {
        return cartRepository.findByAccountId(accountId).size();
    }

    @Override
    public long getFilteredCountByTicketId(int ticketId) {
        return cartRepository.findByTicketId(ticketId).size();
    }

    @Override
    public Cart save(Cart cart) {
        return cartRepository.save(cart);
    }

    @Override
    public void deleteCart(int id) {
        cartRepository.deleteById(id);
    }


    @Override
    public void deleteByAccountId(int accountId) {
        List<Cart> carts = cartRepository.findByAccountId(accountId);
        cartRepository.deleteAll(carts);
    }

    @Override
    public Optional<Cart> findByAccountIdAndTicketId(int accountId, int ticketId) {
        return cartRepository.findByAccountIdAndTicketId(accountId, ticketId);
    }
}