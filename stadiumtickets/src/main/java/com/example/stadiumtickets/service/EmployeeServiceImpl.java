package com.example.stadiumtickets.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.stadiumtickets.model.Employee;
import com.example.stadiumtickets.repository.EmployeeRepository;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Override
    public Page<Employee> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return employeeRepository.findAll(pageable);
    }

    @Override
    public Employee findById(int id) {
        return employeeRepository.findById(id).orElse(null);
    }

    @Override
    public List<Employee> findByBankId(int bankId) {
        return employeeRepository.findByBankId(bankId);
    }

    @Override
    public Page<Employee> findByBankId(int bankId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return employeeRepository.findByBankId(bankId, pageable);
    }

    @Override
    public Optional<Employee> findByAccountId(int accountId) {  
        return employeeRepository.findByAccountId(accountId);
    }

    @Override
    public Page<Employee> findByAccountId(int accountId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return employeeRepository.findByAccountId(accountId, pageable);
    }

    @Override
    public long getTotalCount() {
        return employeeRepository.count();
    }

    @Override
    public long getFilteredCountByBankId(int bankId) {
        return employeeRepository.findByBankId(bankId).size();
    }

    @Override
    public long getFilteredCountByAccountId(int accountId) {
        return employeeRepository.findByAccountId(accountId).isPresent() ? 1 : 0;
    }

    @Override
    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public void deleteEmployee(int id) {
        employeeRepository.deleteById(id);
    }
}