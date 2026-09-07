package com.example.stadiumtickets.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.stadiumtickets.model.Seat;

public interface SeatService {
    List<Seat> findAll();
    Page<Seat> findAll(int page, int size);
    Seat findById(int id);
    List<Seat> findBySectorId(int sectorId);
    Page<Seat> findBySectorId(int sectorId, int page, int size);
    List<Seat> findByRowNumber(int rowNumber);
    Page<Seat> findByRowNumber(int rowNumber, int page, int size);
    List<Seat> findBySeatNumber(int seatNumber);
    Page<Seat> findBySeatNumber(int seatNumber, int page, int size);
    long getTotalCount();
    long getFilteredCountBySectorId(int sectorId);
    long getFilteredCountByRowNumber(int rowNumber);
    long getFilteredCountBySeatNumber(int seatNumber);
    Seat save(Seat seat);
    void deleteSeat(int id);
}