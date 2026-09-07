package com.example.stadiumtickets.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.stadiumtickets.model.Seat;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Integer> {
    List<Seat> findBySectorId(int sectorId);
    List<Seat> findByRowNumber(int rowNumber);
    List<Seat> findBySeatNumber(int seatNumber);
    
    Page<Seat> findBySectorId(int sectorId, Pageable pageable);
    Page<Seat> findBySector_Id(int sectorId, Pageable pageable);
    Page<Seat> findByRowNumber(int rowNumber, Pageable pageable);
    Page<Seat> findBySeatNumber(int seatNumber, Pageable pageable);
}