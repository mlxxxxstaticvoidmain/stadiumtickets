package com.example.stadiumtickets.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.stadiumtickets.model.Ticket;
import com.example.stadiumtickets.repository.TicketRepository;

@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Override
    public List<Ticket> findAll() {
        return ticketRepository.findAll();
    }

    @Override
    public Page<Ticket> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ticketRepository.findAll(pageable);
    }

    @Override
    public Ticket findById(int id) {
        return ticketRepository.findById(id).orElse(null);
    }

    @Override
    public List<Ticket> findByEventId(int eventId) {
        return ticketRepository.findByEventId(eventId);
    }

    @Override
    public Page<Ticket> findByEventId(int eventId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ticketRepository.findByEventId(eventId, pageable);
    }

    @Override
    public List<Ticket> findByEventTitle(String eventTitle) {
        return ticketRepository.findByEvent_TitleContaining(eventTitle);
    }

    @Override
    public Page<Ticket> findByEventTitle(String eventTitle, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ticketRepository.findByEvent_TitleContaining(eventTitle, pageable);
    }

    @Override
    public List<Ticket> findByStatus(String status) {
        return ticketRepository.findByStatus(status);
    }

    @Override
    public Page<Ticket> findByStatus(String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ticketRepository.findByStatus(status, pageable);
    }

    @Override
    public long getTotalCount() {
        return ticketRepository.count();
    }

    @Override
    public long getFilteredCountByEventId(int eventId) {
        return ticketRepository.findByEventId(eventId).size();
    }

    @Override
    public long getFilteredCountByStatus(String status) {
        if (status == null || status.isEmpty()) {
            return ticketRepository.count();
        }
        return ticketRepository.findByStatus(status).size();
    }

    @Override
    public Ticket save(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @Override
    public void deleteTicket(int id) {
        ticketRepository.deleteById(id);
    }
}