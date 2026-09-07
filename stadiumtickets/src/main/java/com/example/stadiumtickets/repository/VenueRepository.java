package com.example.stadiumtickets.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.stadiumtickets.model.Venue;

@Repository
public interface VenueRepository extends JpaRepository<Venue, Integer> {
    
    Optional<Venue> findByNameVenue(String nameVenue);
    
    List<Venue> findByNameVenueContainingIgnoreCase(String nameVenue);
    
    Page<Venue> findByNameVenueContainingIgnoreCase(String nameVenue, Pageable pageable);
}