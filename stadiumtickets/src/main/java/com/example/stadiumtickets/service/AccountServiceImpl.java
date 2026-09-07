package com.example.stadiumtickets.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.stadiumtickets.model.Account;
import com.example.stadiumtickets.repository.AccountRepository;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public Page<Account> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return accountRepository.findAll(pageable);
    }

    @Override
    public Account findById(int id) {
        return accountRepository.findById(id).orElse(null);
    }

    @Override
    public List<Account> findByFirstName(String firstName) {
        return accountRepository.findByFirstNameContainingIgnoreCase(firstName);
    }

    @Override
    public Page<Account> findByFirstName(String firstName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return accountRepository.findByFirstNameContainingIgnoreCase(firstName, pageable);
    }

    @Override
    public List<Account> findByRoleId(int roleId) {
        return accountRepository.findByRoleId(roleId);
    }

    @Override
    public Page<Account> findByRoleId(int roleId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return accountRepository.findByRoleId(roleId, pageable);
    }

    @Override
    public long getTotalCount() {
        return accountRepository.count();
    }

    @Override
    public long getFilteredCount(String firstName) {
        if (firstName == null || firstName.isEmpty()) {
            return accountRepository.count();
        }
        return accountRepository.findByFirstNameContainingIgnoreCase(firstName).size();
    }

    @Override
    public long getFilteredCountByRole(int roleId) {
        return accountRepository.findByRoleId(roleId).size();
    }

    @Override
    public Account save(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public void deleteAccount(int id) {
        accountRepository.deleteById(id);
    }
    
    @Override
    public List<Account> findByBankName(String bankName) {
        return accountRepository.findAll().stream()
                .filter(account -> bankName.equals(account.getBankName()))
                .collect(Collectors.toList());
    }
    
    @Override
    public Page<Account> findByBankName(String bankName, int page, int size) {
        List<Account> filtered = findByBankName(bankName);
        int start = (int) PageRequest.of(page, size).getOffset();
        int end = Math.min((start + size), filtered.size());
        List<Account> pageContent = filtered.subList(start, end);
        return new PageImpl<>(pageContent, PageRequest.of(page, size), filtered.size());
    }

    @Override
    public Account findByUsername(String username) {
        return accountRepository.findByUsername(username).orElse(null);
    }
    
    @Override
    public Account findByEmail(String email) {
        return accountRepository.findByEmail(email).orElse(null);
    }
    
    @Override
    public Account findByPhoneNumber(String phoneNumber) {
        return accountRepository.findByPhoneNumber(phoneNumber).orElse(null);
    }
    
    @Override
    public Page<Account> findByUsername(String username, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return accountRepository.findByUsernameContainingIgnoreCase(username, pageable);
    }
    
    @Override
    public Page<Account> findByEmail(String email, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return accountRepository.findByEmailContainingIgnoreCase(email, pageable);
    }
}