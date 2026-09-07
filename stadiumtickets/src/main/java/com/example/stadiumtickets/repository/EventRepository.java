package com.example.stadiumtickets.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.stadiumtickets.model.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    
    List<Event> findByTitleContainingIgnoreCase(String title);
    Page<Event> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    
    List<Event> findByVenueId(int venueId);
    Page<Event> findByVenueId(int venueId, Pageable pageable);
    Page<Event> findByVenue_Id(int venueId, Pageable pageable);
    
    List<Event> findByGametypeId(int gametypeId);
    Page<Event> findByGametypeId(int gametypeId, Pageable pageable);
    Page<Event> findByGametype_Id(int gametypeId, Pageable pageable);
}