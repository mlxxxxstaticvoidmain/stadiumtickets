package com.example.stadiumtickets.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "Seat", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"sector_id", "rowNumber", "seatNumber"})
})
public class Seat {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @NotNull(message = "Сектор обязателен")
    @ManyToOne
    @JoinColumn(name = "sector_id", nullable = false)
    private Sector sector;
    
    @Positive(message = "Номер ряда должен быть положительным")
    private int rowNumber;
    
    @Positive(message = "Номер места должен быть положительным")
    private int seatNumber;
    
    @Transient
    private String sectorName;
    
    @Transient
    private String venueName;
    
    @Transient
    private String fullSeatInfo;

    @JsonIgnore
    @OneToMany(mappedBy = "seat")
    private List<Ticket> tickets;
    
    public Seat() {}
    
    public Seat(int id, Sector sector, int rowNumber, int seatNumber) {
        this.id = id;
        this.sector = sector;
        this.rowNumber = rowNumber;
        this.seatNumber = seatNumber;
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public Sector getSector() { return sector; }
    public void setSector(Sector sector) { this.sector = sector; }
    
    public int getRowNumber() { return rowNumber; }
    public void setRowNumber(int rowNumber) { this.rowNumber = rowNumber; }
    
    public int getSeatNumber() { return seatNumber; }
    public void setSeatNumber(int seatNumber) { this.seatNumber = seatNumber; }
    
    public String getSectorName() { return sectorName; }
    public void setSectorName(String sectorName) { this.sectorName = sectorName; }
    
    public String getVenueName() { return venueName; }
    public void setVenueName(String venueName) { this.venueName = venueName; }
    
    public String getFullSeatInfo() { return fullSeatInfo; }
    public void setFullSeatInfo(String fullSeatInfo) { this.fullSeatInfo = fullSeatInfo; }
}