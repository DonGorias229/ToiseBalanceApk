package com.example.judeapp.models;

public class Utilisateur {
    public String nom, prenom, numero, password, id;

    public Utilisateur() {

    }

    public Utilisateur(String nom, String prenom, String numero, String password, String id) {
        this.nom = nom;
        this.prenom = prenom;
        this.numero = numero;
        this.password = password;
        this.id = id;
    }

    public Utilisateur(String nom, String prenom, String numero, String password) {
        this.nom = nom;
        this.prenom = prenom;
        this.numero = numero;
        this.password = password;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
