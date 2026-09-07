package com.example.stadiumtickets.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.stadiumtickets.model.Sector;

@Repository
public interface SectorRepository extends JpaRepository<Sector, Integer> {
    
    List<Sector> findByVenueId(int venueId);
    Page<Sector> findByVenueId(int venueId, Pageable pageable);
    Page<Sector> findByVenue_Id(int venueId, Pageable pageable);
    List<Sector> findByVenue_NameVenueContainingIgnoreCase(String venueName);
    Page<Sector> findByVenue_NameVenueContainingIgnoreCase(String venueName, Pageable pageable);
    
    List<Sector> findBySectorNameContainingIgnoreCase(String sectorName);
    Page<Sector> findBySectorNameContainingIgnoreCase(String sectorName, Pageable pageable);
    
}