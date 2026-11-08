
package tn.enicarthage.eduDrive.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class SeanceRequest {
    public Long candidatId;
    public Long moniteurId;
    public Long vehiculeId;
    public LocalDate dateSeance;
    public LocalTime heureDebut;
    public int duree;
    public String typeSeance; // "CODE" ou "CONDUITE"
}
Houssem
Crée dto/SeanceResponse.java :

        package tn.enicarthage.eduDrive.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class SeanceResponse {
    public Long id;
    public Long candidatId;
    public String candidatNom; // Enrichi
    public Long moniteurId;
    public String moniteurNom; // Enrichi
    public Long vehiculeId;
    public String immatriculation; // Enrichi
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