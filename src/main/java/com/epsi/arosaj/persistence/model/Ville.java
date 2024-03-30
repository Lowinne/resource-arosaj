package com.epsi.arosaj.persistence.model;

import jakarta.persistence.*;
import lombok.ToString;

@Entity
@ToString
public class Ville {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(nullable = false)
    private String nom;
    @Column(nullable = false)
    private String codePostale;

    public Ville() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCodePostale() {
        return codePostale;
    }

    public void setCodePostale(String codePostale) {
        this.codePostale = codePostale;
    }
}
