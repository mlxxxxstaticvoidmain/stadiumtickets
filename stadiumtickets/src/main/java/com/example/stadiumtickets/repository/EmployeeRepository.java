package com.example.stadiumtickets.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.stadiumtickets.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    List<Employee> findByBankId(int bankId);
    Optional<Employee> findByAccountId(int accountId);
    Page<Employee> findByBankId(int bankId, Pageable pageable);
    Page<Employee> findByAccountId(int accountId, Pageable pageable);
    Page<Employee> findByAccount_Id(int accountId, Pageable pageable);
    Page<Employee> findByBank_Id(int bankId, Pageable pageable);
    
}