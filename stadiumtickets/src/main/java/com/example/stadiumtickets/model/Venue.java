package com.example.stadiumtickets.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Venue")
public class Venue {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @NotBlank(message = "Название стадиона обязательно")
    @Size(min = 3, message = "Название стадиона должно быть не менее 3 символов")
    @Column(unique = true, nullable = false)
    private String nameVenue;
    
    @NotBlank(message = "Адрес обязателен")
    @Size(min = 5, message = "Адрес должен быть не менее 5 символов")
    @Column(nullable = false)
    private String address;

    @JsonIgnore
    @OneToMany(mappedBy = "venue")
    private List<Event> events;

    @JsonIgnore
    @OneToMany(mappedBy = "venue")
    private List<Sector> sectors;
    
    public Venue() {}
    
    public Venue(int id, String nameVenue, String address) {
        this.id = id;
        this.nameVenue = nameVenue;
        this.address = address;
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNameVenue() { return nameVenue; }
    public void setNameVenue(String nameVenue) { this.nameVenue = nameVenue; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}