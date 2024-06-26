package com.epsi.arosaj.persistence.model;

import jakarta.persistence.*;
import lombok.ToString;

@Entity
@ToString
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(nullable = false)
    private String code;
    @Column(nullable = false)
    private String role;

    public Role(String code, String role) {
        this.code = code;
        this.role = role;
    }

    public Role() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
