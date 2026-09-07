package com.example.stadiumtickets.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "Employee")
public class Employee {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @ManyToOne
    @JoinColumn(name = "bank_id", nullable = false)
    private Bank bank;
    
    @OneToOne
    @JoinColumn(name = "account_id", nullable = false, unique = true)
    private Account account;
    
    @PastOrPresent(message = "Дата найма не может быть в будущем")
    private LocalDate hireDate;
    
    @PositiveOrZero(message = "Зарплата не может быть отрицательной")
    private double salary;
    
    @Transient
    private String bankName;
    
    @Transient
    private String employeeName;

    @JsonIgnore
    @OneToMany(mappedBy = "employee")
    private List<Order> orders;
    
    public Employee() {
        this.hireDate = LocalDate.now();
    }
    
    public Employee(int id, Bank bank, Account account, LocalDate hireDate, double salary) {
        this.id = id;
        this.bank = bank;
        this.account = account;
        this.hireDate = hireDate;
        this.salary = salary;
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public Bank getBank() { return bank; }
    public void setBank(Bank bank) { this.bank = bank; }
    
    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }
    
    public LocalDate getHireDate() { return hireDate; }
    public void setHireDate(LocalDate hireDate) { this.hireDate = hireDate; }
    
    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }
    
    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }
    
    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
}