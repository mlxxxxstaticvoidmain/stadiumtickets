package com.example.stadiumtickets.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.stadiumtickets.model.Venue;

public interface VenueService {
    List<Venue> findAll();
    Page<Venue> findAll(int page, int size);
    Venue findById(int id);
    List<Venue> findByNameVenue(String nameVenue);
    Page<Venue> findByNameVenue(String nameVenue, int page, int size);
    long getTotalCount();
    long getFilteredCount(String nameVenue);
    Venue save(Venue venue);
    void deleteVenue(int id);
}