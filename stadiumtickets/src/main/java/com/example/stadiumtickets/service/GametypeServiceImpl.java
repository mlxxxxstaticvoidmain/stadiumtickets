package com.example.stadiumtickets.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.stadiumtickets.model.Event;
import com.example.stadiumtickets.model.Gametype;
import com.example.stadiumtickets.repository.EventRepository;
import com.example.stadiumtickets.repository.GametypeRepository;

@Service
public class GametypeServiceImpl implements GametypeService {

    @Autowired
    private GametypeRepository gametypeRepository;
    
    @Autowired
    private EventRepository eventRepository;

    @Override
    public List<Gametype> findAll() {
        return gametypeRepository.findAll();
    }

    @Override
    public Page<Gametype> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return gametypeRepository.findAll(pageable);
    }

    @Override
    public Gametype findById(int id) {
        return gametypeRepository.findById(id).orElse(null);
    }

    @Override
    public List<Gametype> findByName(String name) {
        return gametypeRepository.findByTournamentNameContainingIgnoreCase(name);
    }

    @Override
    public Page<Gametype> findByName(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return gametypeRepository.findByTournamentNameContainingIgnoreCase(name, pageable);
    }

    @Override
    public long getTotalCount() {
        return gametypeRepository.count();
    }

    @Override
    public long getFilteredCount(String name) {
        if (name == null || name.isEmpty()) {
            return gametypeRepository.count();
        }
        return gametypeRepository.findByTournamentNameContainingIgnoreCase(name).size();
    }

    @Override
    public Gametype save(Gametype gametype) {
        return gametypeRepository.save(gametype);
    }

    @Override
    public void deleteGametype(int id) {
        gametypeRepository.deleteById(id);
    }

    @Override
    public boolean canDeleteGametype(int id) {
        return eventRepository.findByGametypeId(id).isEmpty();
    }

    @Override
    public String getEventsWithGametype(int id) {
        List<Event> events = eventRepository.findByGametypeId(id);
        if (events.isEmpty()) {
            return "";
        }
        return events.stream()
                .map(Event::getTitle)
                .reduce((a, b) -> a + ", " + b)
                .orElse("");
    }
}