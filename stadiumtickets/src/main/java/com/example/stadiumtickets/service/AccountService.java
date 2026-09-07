package com.example.stadiumtickets.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.stadiumtickets.model.Account;

public interface AccountService {
    List<Account> findAll();
    Page<Account> findAll(int page, int size); 
    Account findById(int id);
    List<Account> findByFirstName(String firstName);
    Page<Account> findByFirstName(String firstName, int page, int size);
    List<Account> findByRoleId(int roleId);
    Page<Account> findByRoleId(int roleId, int page, int size);
    long getTotalCount(); 
    long getFilteredCount(String firstName);
    long getFilteredCountByRole(int roleId);
    Account save(Account account); 
    void deleteAccount(int id);
    List<Account> findByBankName(String bankName);
    Page<Account> findByBankName(String bankName, int page, int size);
    Account findByUsername(String username);
    Account findByEmail(String email);
    Account findByPhoneNumber(String phoneNumber);
    Page<Account> findByUsername(String username, int page, int size);
    Page<Account> findByEmail(String email, int page, int size);
}