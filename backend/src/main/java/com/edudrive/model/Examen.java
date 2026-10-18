package com.edudrive.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Examen {
    private Long id;
    private Long candidatId;
    private String type;
    private LocalDate dateExamen;
    private LocalTime heure;
    private String resultat;
    private int numeroTentative;

    public Examen() {
    }

    public Examen(Long id, Long candidatId, String type, LocalDate dateExamen, LocalTime heure, String resultat, int numeroTentative) {
        this.id = id;
        this.candidatId = candidatId;
        this.type = type;
        this.dateExamen = dateExamen;
        this.heure = heure;
        this.resultat = resultat;
        this.numeroTentative = numeroTentative;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCandidatId() { return candidatId; }
    public void setCandidatId(Long candidatId) { this.candidatId = candidatId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public LocalDate getDateExamen() { return dateExamen; }
    public void setDateExamen(LocalDate dateExamen) { this.dateExamen = dateExamen; }

    public LocalTime getHeure() { return heure; }
    public void setHeure(LocalTime heure) { this.heure = heure; }

    public String getResultat() { return resultat; }
    public void setResultat(String resultat) { this.resultat = resultat; }

    public int getNumeroTentative() { return numeroTentative; }
    public void setNumeroTentative(int numeroTentative) { this.numeroTentative = numeroTentative; }
}