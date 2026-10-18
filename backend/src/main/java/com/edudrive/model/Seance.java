package com.edudrive.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Seance {
    private Long id;
    private Long candidatId;
    private Long moniteurId;
    private Long vehiculeId;
    private LocalDate dateSeance;
    private LocalTime heureDebut;
    private int duree;
    private String typeSeance;
    private String statut;
    private boolean confirmeParCandidat;
    private boolean confirmeParMoniteur;
    private String motifRefus;
    private Integer note;
    private String commentaire;

    public Seance() {
    }

    public Seance(Long id, Long candidatId, Long moniteurId, Long vehiculeId, LocalDate dateSeance, LocalTime heureDebut, int duree, String typeSeance, String statut, boolean confirmeParCandidat, boolean confirmeParMoniteur, String motifRefus, Integer note, String commentaire) {
        this.id = id;
        this.candidatId = candidatId;
        this.moniteurId = moniteurId;
        this.vehiculeId = vehiculeId;
        this.dateSeance = dateSeance;
        this.heureDebut = heureDebut;
        this.duree = duree;
        this.typeSeance = typeSeance;
        this.statut = statut;
        this.confirmeParCandidat = confirmeParCandidat;
        this.confirmeParMoniteur = confirmeParMoniteur;
        this.motifRefus = motifRefus;
        this.note = note;
        this.commentaire = commentaire;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCandidatId() { return candidatId; }
    public void setCandidatId(Long candidatId) { this.candidatId = candidatId; }

    public Long getMoniteurId() { return moniteurId; }
    public void setMoniteurId(Long moniteurId) { this.moniteurId = moniteurId; }

    public Long getVehiculeId() { return vehiculeId; }
    public void setVehiculeId(Long vehiculeId) { this.vehiculeId = vehiculeId; }

    public LocalDate getDateSeance() { return dateSeance; }
    public void setDateSeance(LocalDate dateSeance) { this.dateSeance = dateSeance; }

    public LocalTime getHeureDebut() { return heureDebut; }
    public void setHeureDebut(LocalTime heureDebut) { this.heureDebut = heureDebut; }

    public int getDuree() { return duree; }
    public void setDuree(int duree) { this.duree = duree; }

    public String getTypeSeance() { return typeSeance; }
    public void setTypeSeance(String typeSeance) { this.typeSeance = typeSeance; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public boolean isConfirmeParCandidat() { return confirmeParCandidat; }
    public void setConfirmeParCandidat(boolean confirmeParCandidat) { this.confirmeParCandidat = confirmeParCandidat; }

    public boolean isConfirmeParMoniteur() { return confirmeParMoniteur; }
    public void setConfirmeParMoniteur(boolean confirmeParMoniteur) { this.confirmeParMoniteur = confirmeParMoniteur; }

    public String getMotifRefus() { return motifRefus; }
    public void setMotifRefus(String motifRefus) { this.motifRefus = motifRefus; }

    public Integer getNote() { return note; }
    public void setNote(Integer note) { this.note = note; }

    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }
}