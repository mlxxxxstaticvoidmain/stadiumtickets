package com.example.stadiumtickets.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.stadiumtickets.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    
    Optional<Role> findByName(String name);
    
    List<Role> findByNameContainingIgnoreCase(String name);
    
    Page<Role> findByNameContainingIgnoreCase(String name, Pageable pageable);
}