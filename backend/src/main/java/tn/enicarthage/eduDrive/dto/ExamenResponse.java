
package tn.enicarthage.eduDrive.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class ExamenResponse {
    public Long id;
    public Long candidatId;
    public String candidatNom; // Enrichi
    public String type;
    public LocalDate dateExamen;
    public LocalTime heure;
    public String resultat;
    public int numeroTentative;
}