package com.example.stadiumtickets.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.stadiumtickets.model.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    
    List<Ticket> findByEventId(int eventId);
    Page<Ticket> findByEventId(int eventId, Pageable pageable);
    Page<Ticket> findByEvent_Id(int eventId, Pageable pageable);
    List<Ticket> findByEvent_TitleContaining(String eventTitle);
    Page<Ticket> findByEvent_TitleContaining(String eventTitle, Pageable pageable);
    
    List<Ticket> findByStatus(String status);
    Page<Ticket> findByStatus(String status, Pageable pageable);
    
    List<Ticket> findByEventIdAndStatus(int eventId, String status);
}