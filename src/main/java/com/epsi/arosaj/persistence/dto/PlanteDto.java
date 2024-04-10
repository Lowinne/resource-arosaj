package com.epsi.arosaj.persistence.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PlanteDto {
    private String nom;
    private String description;
    private String prenomProprio;

    public PlanteDto(String nom, String description, String prenomProprio) {
        this.nom = nom;
        this.description = description;
        this.prenomProprio = prenomProprio;
    }
}
