package com.example.stadiumtickets.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.stadiumtickets.model.Gametype;

@Repository
public interface GametypeRepository extends JpaRepository<Gametype, Integer> {
    
    Optional<Gametype> findByTournamentName(String tournamentName);
    
    List<Gametype> findByTournamentNameContainingIgnoreCase(String tournamentName);
    
    Page<Gametype> findByTournamentNameContainingIgnoreCase(String tournamentName, Pageable pageable);
}