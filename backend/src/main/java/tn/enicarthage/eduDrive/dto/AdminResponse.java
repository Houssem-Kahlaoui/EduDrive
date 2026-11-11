package tn.enicarthage.eduDrive.dto;

import tn.enicarthage.eduDrive.model.Admin;

public class AdminResponse {
    public Long id;
    public String nom;
    public String prenom;
    public String email;
    public String telephone;
    public boolean actif;
    public String role;

    public static AdminResponse from(Admin a) {
        AdminResponse r = new AdminResponse();
        r.id = a.getId();
        r.nom = a.getNom();
        r.prenom = a.getPrenom();
        r.email = a.getEmail();
        r.telephone = a.getTelephone();
        r.actif = a.isActif();
        r.role = a.getRole();
        return r;
    }
}