package com.example.stadiumtickets.model;

import java.time.LocalDate;

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
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Event")
public class Event {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @ManyToOne
    @JoinColumn(name = "venue_id", nullable = false)
    private Venue venue;
    
    @ManyToOne
    @JoinColumn(name = "gametype_id", nullable = false)
    private Gametype gametype;
    
    @NotBlank(message = "Название события обязательно")
    @Size(min = 4, message = "Название события должно быть не менее 4 символов")
    private String title;
    
    @NotNull(message = "Дата события обязательна")
    @FutureOrPresent(message = "Дата события не может быть в прошлом")
    private LocalDate eventDate;
    
    @PositiveOrZero(message = "Цена не может быть отрицательной")
    private double price;
    
    @Transient
    private String venueName;
    
    @Transient
    private String gametypeName;

    @JsonIgnore
    @OneToMany(mappedBy = "event")
    private List<Ticket> tickets;
    
    public Event() {}
    
    public Event(int id, Venue venue, Gametype gametype, String title, LocalDate eventDate, double price) {
        this.id = id;
        this.venue = venue;
        this.gametype = gametype;
        this.title = title;
        this.eventDate = eventDate;
        this.price = price;
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public Venue getVenue() { return venue; }
    public void setVenue(Venue venue) { this.venue = venue; }
    
    public Gametype getGametype() { return gametype; }
    public void setGametype(Gametype gametype) { this.gametype = gametype; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public LocalDate getEventDate() { return eventDate; }
    public void setEventDate(LocalDate eventDate) { this.eventDate = eventDate; }
    
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    
    public String getVenueName() { return venueName; }
    public void setVenueName(String venueName) { this.venueName = venueName; }
    
    public String getGametypeName() { return gametypeName; }
    public void setGametypeName(String gametypeName) { this.gametypeName = gametypeName; }
}