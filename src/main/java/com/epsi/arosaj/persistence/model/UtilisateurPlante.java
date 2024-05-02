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

    @ManyToOne
    @JoinColumn(name = "prop_id", referencedColumnName = "id")
    private utilisateur proprietaire;
    @OneToMany(mappedBy="utilisateurPlante", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Plante> planteList;

    public UtilisateurPlante() {
        planteList = new ArrayList<Plante>();
    }

    public utilisateur getProprietaire() {
        return proprietaire;
    }

    public void setProprietaire(utilisateur proprietaire) {
        this.proprietaire = proprietaire;
    }

    public List<Plante> getPlanteList() {
        return planteList;
    }

    public void setPlanteList(List<Plante> planteList) {
        this.planteList = planteList;
    }
}
