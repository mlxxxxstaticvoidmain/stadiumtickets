package com.example.stadiumtickets.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.stadiumtickets.model.Gametype;

public interface GametypeService {
    List<Gametype> findAll();
    Page<Gametype> findAll(int page, int size);
    Gametype findById(int id);
    List<Gametype> findByName(String name);
    Page<Gametype> findByName(String name, int page, int size);
    long getTotalCount();
    long getFilteredCount(String name);
    Gametype save(Gametype gametype);
    void deleteGametype(int id);
    boolean canDeleteGametype(int id);
    String getEventsWithGametype(int id);
}