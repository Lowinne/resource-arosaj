package com.epsi.arosaj.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] data;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore // User already known
    private Utilisateur utilisateur;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "photoPlante_id", nullable = false)
    @JsonIgnore // Prevent infinite recursion
    private PhotoPlante photoPlante;

    public Photo() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public PhotoPlante getPhotoPlante() {
        return photoPlante;
    }

    public void setPhotoPlante(PhotoPlante photoPlante) {
        this.photoPlante = photoPlante;
    }
}
