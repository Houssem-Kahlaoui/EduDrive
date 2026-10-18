package com.edudrive.model;

public class Vehicule {
    private Long id;
    private String immatriculation;
    private String marque;
    private String modele;
    private String etat;

    public Vehicule() {
    }

    public Vehicule(Long id, String immatriculation, String marque, String modele, String etat) {
        this.id = id;
        this.immatriculation = immatriculation;
        this.marque = marque;
        this.modele = modele;
        this.etat = etat;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getImmatriculation() { return immatriculation; }
    public void setImmatriculation(String immatriculation) { this.immatriculation = immatriculation; }

    public String getMarque() { return marque; }
    public void setMarque(String marque) { this.marque = marque; }

    public String getModele() { return modele; }
    public void setModele(String modele) { this.modele = modele; }

    public String getEtat() { return etat; }
    public void setEtat(String etat) { this.etat = etat; }
}