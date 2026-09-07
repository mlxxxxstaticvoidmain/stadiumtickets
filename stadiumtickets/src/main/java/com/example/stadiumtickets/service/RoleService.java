package com.example.stadiumtickets.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.stadiumtickets.model.Role;

public interface RoleService {
    List<Role> findAll();
    Page<Role> findAll(int page, int size);
    Role findById(int id);
    List<Role> findByName(String name);
    Page<Role> findByName(String name, int page, int size);
    long getTotalCount();
    long getFilteredCount(String name);
    Role save(Role role);
    void deleteRole(int id);
    boolean canDeleteRole(int id);
    String getAccountsWithRole(int id);
}