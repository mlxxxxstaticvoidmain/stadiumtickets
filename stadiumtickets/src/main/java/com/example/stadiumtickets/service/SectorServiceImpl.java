package com.example.stadiumtickets.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.stadiumtickets.model.Sector;
import com.example.stadiumtickets.repository.SectorRepository;

@Service
public class SectorServiceImpl implements SectorService {

    @Autowired
    private SectorRepository sectorRepository;

    @Override
    public List<Sector> findAll() {
        return sectorRepository.findAll();
    }

    @Override
    public Page<Sector> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return sectorRepository.findAll(pageable);
    }

    @Override
    public Sector findById(int id) {
        return sectorRepository.findById(id).orElse(null);
    }

    @Override
    public List<Sector> findByVenueId(int venueId) {
        return sectorRepository.findByVenueId(venueId);
    }

    @Override
    public Page<Sector> findByVenueId(int venueId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return sectorRepository.findByVenueId(venueId, pageable);
    }

    @Override
    public List<Sector> findByVenueName(String venueName) {
        return sectorRepository.findByVenue_NameVenueContainingIgnoreCase(venueName);
    }

    @Override
    public Page<Sector> findByVenueName(String venueName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return sectorRepository.findByVenue_NameVenueContainingIgnoreCase(venueName, pageable);
    }

    @Override
    public List<Sector> findBySectorName(String sectorName) {
        return sectorRepository.findBySectorNameContainingIgnoreCase(sectorName);
    }

    @Override
    public Page<Sector> findBySectorName(String sectorName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return sectorRepository.findBySectorNameContainingIgnoreCase(sectorName, pageable);
    }

    @Override
    public long getTotalCount() {
        return sectorRepository.count();
    }

    @Override
    public long getFilteredCountByVenueId(int venueId) {
        return sectorRepository.findByVenueId(venueId).size();
    }


    @Override
    public long getFilteredCountBySectorName(String sectorName) {
        if (sectorName == null || sectorName.isEmpty()) {
            return sectorRepository.count();
        }
        return sectorRepository.findBySectorNameContainingIgnoreCase(sectorName).size();
    }

    @Override
    public Sector save(Sector sector) {
        return sectorRepository.save(sector);
    }

    @Override
    public void deleteSector(int id) {
        sectorRepository.deleteById(id);
    }
}