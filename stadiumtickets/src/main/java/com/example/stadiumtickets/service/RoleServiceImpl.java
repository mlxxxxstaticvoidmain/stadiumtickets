package com.example.stadiumtickets.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.stadiumtickets.model.Account;
import com.example.stadiumtickets.model.Role;
import com.example.stadiumtickets.repository.AccountRepository;
import com.example.stadiumtickets.repository.RoleRepository;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public Page<Role> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return roleRepository.findAll(pageable);
    }

    @Override
    public Role findById(int id) {
        return roleRepository.findById(id).orElse(null);
    }

    @Override
    public List<Role> findByName(String name) {
        return roleRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public Page<Role> findByName(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        // ТЕПЕРЬ РАБОТАЕТ - метод с Pageable есть
        return roleRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    @Override
    public long getTotalCount() {
        return roleRepository.count();
    }

    @Override
    public long getFilteredCount(String name) {
        if (name == null || name.isEmpty()) {
            return roleRepository.count();
        }
        return roleRepository.findByNameContainingIgnoreCase(name).size();
    }

    @Override
    public Role save(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public void deleteRole(int id) {
        roleRepository.deleteById(id);
    }

    @Override
    public boolean canDeleteRole(int id) {
        return accountRepository.findByRoleId(id).isEmpty();
    }

    @Override
    public String getAccountsWithRole(int id) {
        List<Account> accounts = accountRepository.findByRoleId(id);
        if (accounts.isEmpty()) {
            return "";
        }
        return accounts.stream()
                .map(a -> a.getFirstName() + " " + a.getLastName())
                .reduce((a, b) -> a + ", " + b)
                .orElse("");
    }
}