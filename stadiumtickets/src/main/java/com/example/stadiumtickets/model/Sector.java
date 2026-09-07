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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Sector")
public class Sector {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @ManyToOne
    @JoinColumn(name = "venue_id", nullable = false)
    private Venue venue;
    
    @NotBlank(message = "Название сектора обязательно")
    @Size(min = 2, message = "Название сектора должно быть не менее 2 символов")
    private String sectorName;
    
    @NotBlank(message = "Зона обязательна")
    private String zone;
    
    @Transient
    private String venueName;

    @JsonIgnore
    @OneToMany(mappedBy = "sector")
    private List<Seat> seats;
    
    public Sector() {}
    
    public Sector(int id, Venue venue, String sectorName, String zone) {
        this.id = id;
        this.venue = venue;
        this.sectorName = sectorName;
        this.zone = zone;
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public Venue getVenue() { return venue; }
    public void setVenue(Venue venue) { this.venue = venue; }
    
    public String getSectorName() { return sectorName; }
    public void setSectorName(String sectorName) { this.sectorName = sectorName; }
    
    public String getZone() { return zone; }
    public void setZone(String zone) { this.zone = zone; }
    
    public String getVenueName() { return venueName; }
    public void setVenueName(String venueName) { this.venueName = venueName; }
    
}