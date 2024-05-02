package com.epsi.arosaj.persistence.model;

import jakarta.persistence.*;

import java.sql.Time;
import java.util.Date;

@Entity
public class BotanistePlante {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "plante_id", referencedColumnName = "id")
    private Plante plante;
    @ManyToOne
    @JoinColumn(name = "bota_id", referencedColumnName = "id")
    private utilisateur botaniste;

    private String conseil;

    private Date date;
    private Time time;

    public String getConseil() {
        return conseil;
    }

    public void setConseil(String conseil) {
        this.conseil = conseil;
    }

    public BotanistePlante() {
        this.date = new Date();
        this.time =new Time(date.getTime());
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

    public utilisateur getBotaniste() {
        return botaniste;
    }

    public void setBotaniste(utilisateur botaniste) {
        this.botaniste = botaniste;
    }
}