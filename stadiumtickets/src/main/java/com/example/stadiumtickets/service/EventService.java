package com.example.stadiumtickets.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.stadiumtickets.model.Event;

public interface EventService {
    List<Event> findAll();
    Page<Event> findAll(int page, int size);
    Event findById(int id);
    List<Event> findByTitle(String title);
    Page<Event> findByTitle(String title, int page, int size);
    List<Event> findByVenueId(int venueId);
    Page<Event> findByVenueId(int venueId, int page, int size);
    List<Event> findByGametypeId(int gametypeId);
    Page<Event> findByGametypeId(int gametypeId, int page, int size);
    long getTotalCount();
    long getFilteredCount(String title);
    long getFilteredCountByVenueId(int venueId);
    long getFilteredCountByGametypeId(int gametypeId);
    Event save(Event event);
    void deleteEvent(int id);
}