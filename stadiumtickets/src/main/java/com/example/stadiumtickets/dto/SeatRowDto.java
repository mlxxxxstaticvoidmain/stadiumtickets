package com.example.stadiumtickets.dto;

import java.util.List;

public class SeatRowDto {

    private final int rowNumber;
    private final List<SeatCellDto> seats;

    public SeatRowDto(int rowNumber, List<SeatCellDto> seats) {
        this.rowNumber = rowNumber;
        this.seats = seats;
    }

    public int getRowNumber() { return rowNumber; }

    public List<SeatCellDto> getSeats() { return seats; }
}
