package com.example.stadiumtickets.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.stadiumtickets.model.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    
    Optional<Account> findByUsername(String username);
    Optional<Account> findByEmail(String email);
    Optional<Account> findByPhoneNumber(String phoneNumber);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    
    List<Account> findByFirstNameContainingIgnoreCase(String firstName);
    List<Account> findByRoleId(int roleId);
    
    Page<Account> findByFirstNameContainingIgnoreCase(String firstName, Pageable pageable);
    Page<Account> findByRoleId(int roleId, Pageable pageable);
    Page<Account> findByRole_Id(int roleId, Pageable pageable);
    Page<Account> findByUsernameContainingIgnoreCase(String username, Pageable pageable);
    Page<Account> findByEmailContainingIgnoreCase(String email, Pageable pageable);
}