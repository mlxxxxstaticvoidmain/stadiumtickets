package com.example.stadiumtickets.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.stadiumtickets.model.Bank;

public interface BankService {
    List<Bank> findAll();
    Page<Bank> findAll(int page, int size);
    Bank findById(int id);
    List<Bank> findByBankName(String bankName);
    Page<Bank> findByBankName(String bankName, int page, int size);
    long getTotalCount();
    long getFilteredCount(String bankName);
    Bank save(Bank bank);
    void deleteBank(int id);
}