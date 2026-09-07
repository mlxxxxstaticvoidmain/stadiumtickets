package com.example.stadiumtickets.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "account")
public class Account {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @NotBlank(message = "Логин обязателен")
    @Size(min = 3, max = 50, message = "Логин должен быть от 3 до 50 символов")
    @Column(unique = true, nullable = false)
    private String username;
    
    @NotBlank(message = "Пароль обязателен")
    @Size(min = 6, message = "Пароль должен быть не менее 6 символов")
    @Column(nullable = false)
    private String password;
    
    @NotBlank(message = "Email обязателен")
    @Email(message = "Введите корректный email")
    @Column(unique = true, nullable = false)
    private String email;
    
    @NotBlank(message = "Имя обязательно")
    @Size(min = 2, message = "Имя должно быть не менее 2 символов")
    private String firstName;
    
    @NotBlank(message = "Фамилия обязательна")
    @Size(min = 2, message = "Фамилия должна быть не менее 2 символов")
    private String lastName;
    
    private String middleName;
    
    @NotBlank(message = "Номер телефона обязателен")
    @Pattern(regexp = "^[0-9]{10,15}$", message = "Номер телефона должен содержать только цифры (10-15)")
    @Column(unique = true, nullable = false)
    private String phoneNumber;
    
    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
    
    @PastOrPresent(message = "Дата создания не может быть в будущем")
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    private String bankName;

    private boolean active;

    @Transient
    private String roleName;

    @Transient
    private String accountName;

    @JsonIgnore
    @OneToMany(mappedBy = "account")
    private List<Cart> carts;

    @JsonIgnore
    @OneToMany(mappedBy = "account")
    private List<Order> orders;

    @JsonIgnore
    @OneToOne(mappedBy = "account")
    private Employee employee;
    
    public Account() {
        this.createdAt = LocalDateTime.now();
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    
    public String getRoleName() { 
        return role != null ? role.getName() : roleName; 
    }
    public void setRoleName(String roleName) { this.roleName = roleName; }

    public String getAccountName() {
        if (accountName != null && !accountName.isEmpty()) {
            return accountName;
        }
        String fullName = lastName + " " + firstName;
        if (middleName != null && !middleName.isEmpty()) {
            fullName += " " + middleName;
        }
        return fullName;
    }
    public void setAccountName(String accountName) { this.accountName = accountName; }
}