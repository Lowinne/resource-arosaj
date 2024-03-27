package com.epsi.arosaj.persistence.model;

import jakarta.persistence.*;

@Entity
public class BotanistePlante {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "plante_id", referencedColumnName = "id")
    private Plante plante;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bota_id", referencedColumnName = "id")
    private Utilisateur botaniste;

    private String conseil;

    public String getConseil() {
        return conseil;
    }

    public void setConseil(String conseil) {
        this.conseil = conseil;
    }

    public BotanistePlante() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Plante getPlante() {
        return plante;
    }

    public void setPlante(Plante plante) {
        this.plante = plante;
    }

    public Utilisateur getBotaniste() {
        return botaniste;
    }

    public void setBotaniste(Utilisateur botaniste) {
        this.botaniste = botaniste;
    }
}