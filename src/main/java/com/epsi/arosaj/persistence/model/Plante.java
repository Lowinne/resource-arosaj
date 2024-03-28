package com.epsi.arosaj.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class Plante {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String nom;
    private String description;

    @ManyToOne
    @JoinColumn(name="utilisateurPlante_id", nullable=false)
    @JsonIgnore
    private UtilisateurPlante utilisateurPlante;

    public Plante() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UtilisateurPlante getUtilisateurPlante() {
        return utilisateurPlante;
    }

    public void setUtilisateurPlante(UtilisateurPlante utilisateurPlante) {
        this.utilisateurPlante = utilisateurPlante;
    }
}

