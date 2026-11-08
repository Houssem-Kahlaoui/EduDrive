package tn.enicarthage.eduDrive.dto;

import java.time.LocalDate;

public class CandidatResponse {
    public Long id;
    public String nom;
    public String prenom;
    public String email;
    public String telephone;
    public boolean actif;

    public Long forfaitId;
    public String forfaitNom;
    public LocalDate dateNaissance;
    public String statutFormation;
    public LocalDate dateValiditeCode;
    public int heuresCodeRestantes;
    public int heuresConduiteRestantes;
    public boolean dossierComplet;
}