package com.example.stadiumtickets.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.stadiumtickets.model.Seat;
import com.example.stadiumtickets.repository.SeatRepository;

@Service
public class SeatServiceImpl implements SeatService {

    @Autowired
    private SeatRepository seatRepository;

    @Override
    public List<Seat> findAll() {
        return seatRepository.findAll();
    }

    @Override
    public Page<Seat> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return seatRepository.findAll(pageable);
    }

    @Override
    public Seat findById(int id) {
        return seatRepository.findById(id).orElse(null);
    }

    @Override
    public List<Seat> findBySectorId(int sectorId) {
        return seatRepository.findBySectorId(sectorId);
    }

    @Override
    public Page<Seat> findBySectorId(int sectorId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return seatRepository.findBySectorId(sectorId, pageable);
    }

    @Override
    public List<Seat> findByRowNumber(int rowNumber) {
        return seatRepository.findByRowNumber(rowNumber);
    }

    @Override
    public Page<Seat> findByRowNumber(int rowNumber, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return seatRepository.findByRowNumber(rowNumber, pageable);
    }

    @Override
    public List<Seat> findBySeatNumber(int seatNumber) {
        return seatRepository.findBySeatNumber(seatNumber);
    }

    @Override
    public Page<Seat> findBySeatNumber(int seatNumber, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return seatRepository.findBySeatNumber(seatNumber, pageable);
    }

    @Override
    public long getTotalCount() {
        return seatRepository.count();
    }

    @Override
    public long getFilteredCountBySectorId(int sectorId) {
        return seatRepository.findBySectorId(sectorId).size();
    }

    @Override
    public long getFilteredCountByRowNumber(int rowNumber) {
        return seatRepository.findByRowNumber(rowNumber).size();
    }

    @Override
    public long getFilteredCountBySeatNumber(int seatNumber) {
        return seatRepository.findBySeatNumber(seatNumber).size();
    }

    @Override
    public Seat save(Seat seat) {
        return seatRepository.save(seat);
    }

    @Override
    public void deleteSeat(int id) {
        seatRepository.deleteById(id);
    }
}