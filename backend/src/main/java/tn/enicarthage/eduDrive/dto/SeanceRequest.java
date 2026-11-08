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
