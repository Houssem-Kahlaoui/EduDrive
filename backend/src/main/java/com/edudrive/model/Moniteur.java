package com.edudrive.model;

public class Moniteur extends Utilisateur {
    private String specialite;

    public Moniteur() {
        super();
    }

    public Moniteur(Long id, String nom, String prenom, String email, String motDePasse, String telephone, boolean actif, String specialite) {
        super(id, nom, prenom, email, motDePasse, telephone, actif, "MONITEUR");

        this.specialite = specialite;
    }

    public String getSpecialite() { return specialite; }
    public void setSpecialite(String specialite) { this.specialite = specialite; }
}