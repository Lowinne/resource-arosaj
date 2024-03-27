package com.epsi.arosaj.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class UtilisateurPlante {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "prop_id", referencedColumnName = "id")
    private Utilisateur proprietaire;
    @OneToMany(mappedBy="utilisateurPlante")
    @JsonIgnore
    private List<Plante> planteList;

    public UtilisateurPlante() {
        planteList = new ArrayList<Plante>();
    }

    public Utilisateur getProprietaire() {
        return proprietaire;
    }

    public void setProprietaire(Utilisateur proprietaire) {
        this.proprietaire = proprietaire;
    }

    public List<Plante> getPlanteList() {
        return planteList;
    }

    public void setPlanteList(List<Plante> planteList) {
        this.planteList = planteList;
    }
}
