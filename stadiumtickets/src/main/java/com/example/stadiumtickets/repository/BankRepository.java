package com.example.stadiumtickets.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.stadiumtickets.model.Bank;

@Repository
public interface BankRepository extends JpaRepository<Bank, Integer> {
    
    Optional<Bank> findByBankName(String bankName);
    
    List<Bank> findByBankNameContainingIgnoreCase(String bankName);
    
    Page<Bank> findByBankNameContainingIgnoreCase(String bankName, Pageable pageable);
}