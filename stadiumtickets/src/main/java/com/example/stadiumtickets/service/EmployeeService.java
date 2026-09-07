package com.example.stadiumtickets.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.example.stadiumtickets.model.Employee;

public interface EmployeeService {
    List<Employee> findAll();
    Page<Employee> findAll(int page, int size);
    Employee findById(int id);
    List<Employee> findByBankId(int bankId);
    Page<Employee> findByBankId(int bankId, int page, int size);
    Optional<Employee> findByAccountId(int accountId);  
    Page<Employee> findByAccountId(int accountId, int page, int size);
    long getTotalCount();
    long getFilteredCountByBankId(int bankId);
    long getFilteredCountByAccountId(int accountId);
    Employee save(Employee employee);
    void deleteEmployee(int id);
}