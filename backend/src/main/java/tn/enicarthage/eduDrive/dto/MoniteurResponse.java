package tn.enicarthage.eduDrive.dto;

import tn.enicarthage.eduDrive.model.Moniteur;

public class MoniteurResponse {
    public Long id;
    public String nom;
    public String prenom;
    public String email;
    public String telephone;
    public boolean actif;
    public String role;
    public String specialite;

    public static MoniteurResponse from(Moniteur m) {
        MoniteurResponse r = new MoniteurResponse();
        r.id = m.getId();
        r.nom = m.getNom();
        r.prenom = m.getPrenom();
        r.email = m.getEmail();
        r.telephone = m.getTelephone();
        r.actif = m.isActif();
        r.role = m.getRole();
        r.specialite = m.getSpecialite();
        return r;
    }
}