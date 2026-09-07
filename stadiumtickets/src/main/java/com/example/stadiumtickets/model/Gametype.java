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
@Table(name = "Gametype")
public class Gametype {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @NotBlank(message = "Название турнира обязательно")
    @Size(min = 3, message = "Название турнира должно быть не менее 3 символов")
    @Column(unique = true, nullable = false)
    private String tournamentName;

    @JsonIgnore
    @OneToMany(mappedBy = "gametype")
    private List<Event> events;
    
    public Gametype() {}
    
    public Gametype(int id, String tournamentName) {
        this.id = id;
        this.tournamentName = tournamentName;
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getTournamentName() { return tournamentName; }
    public void setTournamentName(String tournamentName) { this.tournamentName = tournamentName; }
}