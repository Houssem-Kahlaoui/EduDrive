package tn.enicarthage.eduDrive.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class SeanceResponse {
    public Long id;
    public Long candidatId;
    public String candidatNom;
    public Long moniteurId;
    public String moniteurNom;
    public Long vehiculeId;
    public String immatriculation;
    public LocalDate dateSeance;
    public LocalTime heureDebut;
    public int duree;
    public String typeSeance;
    public String statut;
    public boolean confirmeParCandidat;
    public boolean confirmeParMoniteur;
    public Integer note;
    public String commentaire;
}