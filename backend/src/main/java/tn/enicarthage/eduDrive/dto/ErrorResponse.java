package tn.enicarthage.eduDrive.dto;

public class ErrorResponse {
    public int statut;
    public String erreur;
    public long timestamp;

    public ErrorResponse(int statut, String erreur) {
        this.statut = statut;
        this.erreur = erreur;
        this.timestamp = System.currentTimeMillis();
    }
}