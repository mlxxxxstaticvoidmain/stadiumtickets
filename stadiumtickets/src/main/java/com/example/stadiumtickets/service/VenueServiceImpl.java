package com.example.stadiumtickets.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.stadiumtickets.model.Venue;
import com.example.stadiumtickets.repository.VenueRepository;

@Service
public class VenueServiceImpl implements VenueService {

    @Autowired
    private VenueRepository venueRepository;

    @Override
    public List<Venue> findAll() {
        return venueRepository.findAll();
    }

    @Override
    public Page<Venue> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return venueRepository.findAll(pageable);
    }

    @Override
    public Venue findById(int id) {
        return venueRepository.findById(id).orElse(null);
    }

    @Override
    public List<Venue> findByNameVenue(String nameVenue) {
        return venueRepository.findByNameVenueContainingIgnoreCase(nameVenue);
    }

    @Override
    public Page<Venue> findByNameVenue(String nameVenue, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return venueRepository.findByNameVenueContainingIgnoreCase(nameVenue, pageable);
    }

    @Override
    public long getTotalCount() {
        return venueRepository.count();
    }

    @Override
    public long getFilteredCount(String nameVenue) {
        if (nameVenue == null || nameVenue.isEmpty()) {
            return venueRepository.count();
        }
        return venueRepository.findByNameVenueContainingIgnoreCase(nameVenue).size();
    }

    @Override
    public Venue save(Venue venue) {
        return venueRepository.save(venue);
    }

    @Override
    public void deleteVenue(int id) {
        venueRepository.deleteById(id);
    }
}