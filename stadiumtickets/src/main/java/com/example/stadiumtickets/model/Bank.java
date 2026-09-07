package com.example.stadiumtickets.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Bank")
public class Bank {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @NotBlank(message = "Название банка обязательно")
    @Size(min = 3, message = "Название банка должно быть не менее 3 символов")
    @Column(unique = true, nullable = false)
    private String bankName;

    @JsonIgnore
    @OneToMany(mappedBy = "bank")
    private List<Employee> employees;
    
    public Bank() {}
    
    public Bank(int id, String bankName) {
        this.id = id;
        this.bankName = bankName;
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }
}