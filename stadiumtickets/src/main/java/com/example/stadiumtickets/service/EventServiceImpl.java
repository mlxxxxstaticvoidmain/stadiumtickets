package com.example.stadiumtickets.service;

import com.example.stadiumtickets.model.Event;
import com.example.stadiumtickets.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Override
    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    @Override
    public Page<Event> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return eventRepository.findAll(pageable);
    }

    @Override
    public Event findById(int id) {
        return eventRepository.findById(id).orElse(null);
    }

    @Override
    public List<Event> findByTitle(String title) {
        return eventRepository.findByTitleContainingIgnoreCase(title);
    }

    @Override
    public Page<Event> findByTitle(String title, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return eventRepository.findByTitleContainingIgnoreCase(title, pageable);
    }

    @Override
    public List<Event> findByVenueId(int venueId) {
        return eventRepository.findByVenueId(venueId);
    }

    @Override
    public Page<Event> findByVenueId(int venueId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return eventRepository.findByVenueId(venueId, pageable);
    }

    @Override
    public List<Event> findByGametypeId(int gametypeId) {
        return eventRepository.findByGametypeId(gametypeId);
    }

    @Override
    public Page<Event> findByGametypeId(int gametypeId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return eventRepository.findByGametypeId(gametypeId, pageable);
    }

    @Override
    public long getTotalCount() {
        return eventRepository.count();
    }

    @Override
    public long getFilteredCount(String title) {
        if (title == null || title.isEmpty()) {
            return eventRepository.count();
        }
        return eventRepository.findByTitleContainingIgnoreCase(title).size();
    }

    @Override
    public long getFilteredCountByVenueId(int venueId) {
        return eventRepository.findByVenueId(venueId).size();
    }

    @Override
    public long getFilteredCountByGametypeId(int gametypeId) {
        return eventRepository.findByGametypeId(gametypeId).size();
    }

    @Override
    public Event save(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public void deleteEvent(int id) {
        eventRepository.deleteById(id);
    }
}