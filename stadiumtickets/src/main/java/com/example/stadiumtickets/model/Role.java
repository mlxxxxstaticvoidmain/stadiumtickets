package com.example.stadiumtickets.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Role")
public class Role {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @NotBlank(message = "Название роли обязательно")
    @Size(min = 3, max = 50, message = "Название роли должно быть от 3 до 50 символов")
    @Column(unique = true, nullable = false)
    private String name;
    
    private String permissions;
    
    public Role() {}
    
    public Role(int id, String name, String permissions) {
        this.id = id;
        this.name = name;
        this.permissions = permissions;
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getPermissions() { return permissions; }
    public void setPermissions(String permissions) { this.permissions = permissions; }
    
    public boolean hasPermission(String permission) {
        if (permissions == null || permissions.isEmpty()) return false;
        for (String p : permissions.split(",")) {
            if (p.trim().equals(permission)) return true;
        }
        return false;
    }
}