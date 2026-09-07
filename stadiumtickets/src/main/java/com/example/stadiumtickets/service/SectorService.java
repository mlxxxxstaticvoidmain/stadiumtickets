package com.example.stadiumtickets.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.stadiumtickets.model.Sector;

public interface SectorService {
    List<Sector> findAll();
    Page<Sector> findAll(int page, int size);
    Sector findById(int id);
    List<Sector> findByVenueId(int venueId);
    Page<Sector> findByVenueId(int venueId, int page, int size);
    List<Sector> findByVenueName(String venueName);
    Page<Sector> findByVenueName(String venueName, int page, int size);
    List<Sector> findBySectorName(String sectorName);
    Page<Sector> findBySectorName(String sectorName, int page, int size);
    long getTotalCount();
    long getFilteredCountByVenueId(int venueId);
    long getFilteredCountBySectorName(String sectorName);
    Sector save(Sector sector);
    void deleteSector(int id);
}