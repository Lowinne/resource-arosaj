package com.epsi.arosaj.persistence.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Time;
import java.util.Date;
@ToString
@Getter
@Setter
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne
    @JoinColumn(name = "expe_id", referencedColumnName = "id")
    private Utilisateur expediteur;

    @ManyToOne
    @JoinColumn(name = "dest_id", referencedColumnName = "id")
    private Utilisateur destinataire;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private Date date;

    public Message() {
        this.date = new Date();
    }

}

