package com.epsi.arosaj.persistence.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class PhotoPlante {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "plante_id", referencedColumnName = "id")
    private Plante plante;

    @OneToMany(mappedBy="photoPlante", cascade = CascadeType.ALL)
    private List<Photo> photoList;

    public PhotoPlante() {
        photoList = new ArrayList<Photo>();
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

    public List<Photo> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<Photo> photoList) {
        this.photoList = photoList;
    }
}
