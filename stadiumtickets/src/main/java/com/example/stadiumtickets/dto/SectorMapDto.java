package com.example.stadiumtickets.dto;

import java.util.List;

import com.example.stadiumtickets.model.Sector;

public class SectorMapDto {

    private final Sector sector;
    private final List<SeatRowDto> rows;

    public SectorMapDto(Sector sector, List<SeatRowDto> rows) {
        this.sector = sector;
        this.rows = rows;
    }

    public Sector getSector() { return sector; }

    public List<SeatRowDto> getRows() { return rows; }
}
