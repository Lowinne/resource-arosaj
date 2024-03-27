package com.epsi.arosaj.persistence.dto;

public class ConseilDto {
    private String nom;
    private String conseil;

    public ConseilDto(String nom, String conseil) {
        this.nom = nom;
        this.conseil = conseil;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getConseil() {
        return conseil;
    }

    public void setConseil(String conseil) {
        this.conseil = conseil;
    }
}
