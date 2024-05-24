package com.epsi.arosaj.persistence.model;

import lombok.Getter;

@Getter
public class AuthResponse {

    private final String jwt;

    private final Utilisateur utilisateur;

    public AuthResponse(String jwt, Utilisateur utilisateur) {
        this.jwt = jwt;
        this.utilisateur = utilisateur;
    }

}
