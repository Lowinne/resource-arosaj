package com.epsi.arosaj.persistence.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PlanteDto {
    private Long id;
    private String nom;
    private String description;
    private String prenomProprio;
    private String pseudoProprio;

    public PlanteDto(Long id, String nom, String description, String prenomProprio, String pseudoProprio) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.prenomProprio = prenomProprio;
        this.pseudoProprio = pseudoProprio;
    }
}
