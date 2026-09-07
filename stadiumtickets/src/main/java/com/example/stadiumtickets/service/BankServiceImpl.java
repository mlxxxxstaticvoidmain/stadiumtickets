package com.example.stadiumtickets.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.stadiumtickets.model.Bank;
import com.example.stadiumtickets.repository.BankRepository;

@Service
public class BankServiceImpl implements BankService {

    @Autowired
    private BankRepository bankRepository;

    @Override
    public List<Bank> findAll() {
        return bankRepository.findAll();
    }

    @Override
    public Page<Bank> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return bankRepository.findAll(pageable);
    }

    @Override
    public Bank findById(int id) {
        return bankRepository.findById(id).orElse(null);
    }

    @Override
    public List<Bank> findByBankName(String bankName) {
        return bankRepository.findByBankNameContainingIgnoreCase(bankName);
    }

    @Override
    public Page<Bank> findByBankName(String bankName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        // ТЕПЕРЬ РАБОТАЕТ - метод с Pageable есть
        return bankRepository.findByBankNameContainingIgnoreCase(bankName, pageable);
    }

    @Override
    public long getTotalCount() {
        return bankRepository.count();
    }

    @Override
    public long getFilteredCount(String bankName) {
        if (bankName == null || bankName.isEmpty()) {
            return bankRepository.count();
        }
        return bankRepository.findByBankNameContainingIgnoreCase(bankName).size();
    }

    @Override
    public Bank save(Bank bank) {
        return bankRepository.save(bank);
    }

    @Override
    public void deleteBank(int id) {
        bankRepository.deleteById(id);
    }
}