package com.example.stadiumtickets.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.stadiumtickets.model.Ticket;

public interface TicketService {
    List<Ticket> findAll();
    Page<Ticket> findAll(int page, int size);
    Ticket findById(int id);
    List<Ticket> findByEventId(int eventId);
    Page<Ticket> findByEventId(int eventId, int page, int size);
    List<Ticket> findByEventTitle(String eventTitle);
    Page<Ticket> findByEventTitle(String eventTitle, int page, int size);
    List<Ticket> findByStatus(String status);
    Page<Ticket> findByStatus(String status, int page, int size);
    long getTotalCount();
    long getFilteredCountByEventId(int eventId);
    long getFilteredCountByStatus(String status);
    Ticket save(Ticket ticket);
    void deleteTicket(int id);
}