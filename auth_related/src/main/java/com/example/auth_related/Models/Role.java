package com.example.auth_related.Models;

import jakarta.persistence.Entity;

@Entity
public class Role extends BaseModel{
    private String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
