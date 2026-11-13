package tn.enicarthage.eduDrive.dto;

public class LoginResponse {
    public String token; 
    public Long id;
    public String nom;
    public String prenom;
    public String email;
    public String telephone;
    public boolean actif;
    public String role;

    public LoginResponse(String token, Long id, String nom, String prenom, String email, String telephone, boolean actif, String role) {
        this.token = token; 
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.actif = actif;
        this.role = role;
    }
}