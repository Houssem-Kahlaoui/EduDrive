package com.edudrive.model;

public class Forfait {
    private Long id;
    private String nom;
    private String typeForfait;
    private int heuresCode;
    private int heuresConduite;
    private double prix;
    private boolean actif;

    public Forfait() {
    }

    public Forfait(Long id, String nom, String typeForfait, int heuresCode, int heuresConduite, double prix, boolean actif) {
        this.id = id;
        this.nom = nom;
        this.typeForfait = typeForfait;
        this.heuresCode = heuresCode;
        this.heuresConduite = heuresConduite;
        this.prix = prix;
        this.actif = actif;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getTypeForfait() { return typeForfait; }
    public void setTypeForfait(String typeForfait) { this.typeForfait = typeForfait; }

    public int getHeuresCode() { return heuresCode; }
    public void setHeuresCode(int heuresCode) { this.heuresCode = heuresCode; }

    public int getHeuresConduite() { return heuresConduite; }
    public void setHeuresConduite(int heuresConduite) { this.heuresConduite = heuresConduite; }

    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }

    public boolean isActif() { return actif; }
    public void setActif(boolean actif) { this.actif = actif; }
}