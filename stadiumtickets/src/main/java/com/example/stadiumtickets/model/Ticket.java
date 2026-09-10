package com.example.stadiumtickets.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "Ticket", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"event_id", "seat_id"})
})
public class Ticket {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @NotNull(message = "Событие обязательно")
    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;
    
    @NotNull(message = "Место обязательно")
    @ManyToOne
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;
    
    @NotBlank(message = "Статус обязателен")
    @Pattern(regexp = "available|reserved|sold", message = "Статус должен быть: available, reserved или sold")
    private String status = "available";
    
    @PositiveOrZero(message = "Цена не может быть отрицательной")
    private double price;
    
    private boolean vipAccess = false;
    
    @Transient
    private String eventTitle;
    
    @Transient
    private String sectorName;
    
    @Transient
    private int rowNumber;
    
    @Transient
    private int seatNumber;

    @JsonIgnore
    @OneToOne(mappedBy = "ticket")
    private Cart cart;
    
    public Ticket() {}
    
    public Ticket(int id, Event event, Seat seat, String status, double price, boolean vipAccess) {
        this.id = id;
        this.event = event;
        this.seat = seat;
        this.status = status;
        this.price = price;
        this.vipAccess = vipAccess;
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public Event getEvent() { return event; }
    public void setEvent(Event event) { this.event = event; }
    
    public Seat getSeat() { return seat; }
    public void setSeat(Seat seat) { this.seat = seat; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    
    public boolean isVipAccess() { return vipAccess; }
    public void setVipAccess(boolean vipAccess) { this.vipAccess = vipAccess; }
    
    public String getEventTitle() { return eventTitle; }
    public void setEventTitle(String eventTitle) { this.eventTitle = eventTitle; }
    
    public String getSectorName() { return sectorName; }
    public void setSectorName(String sectorName) { this.sectorName = sectorName; }
    
    public int getRowNumber() { return rowNumber; }
    public void setRowNumber(int rowNumber) { this.rowNumber = rowNumber; }
    
    public int getSeatNumber() { return seatNumber; }
    public void setSeatNumber(int seatNumber) { this.seatNumber = seatNumber; }
}