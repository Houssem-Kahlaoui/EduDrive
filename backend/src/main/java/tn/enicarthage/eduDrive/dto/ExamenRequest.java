package tn.enicarthage.eduDrive.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class ExamenRequest {
    public Long candidatId;
    public String type; // "CODE", "CIRCULATION", "MANOEUVRES"
    public LocalDate dateExamen;
    public LocalTime heure;
}