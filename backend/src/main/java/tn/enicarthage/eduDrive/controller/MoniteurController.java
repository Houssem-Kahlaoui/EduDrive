package tn.enicarthage.eduDrive.controller;

import tn.enicarthage.eduDrive.dto.MoniteurRequest;
import tn.enicarthage.eduDrive.model.Moniteur;
import tn.enicarthage.eduDrive.service.MoniteurService;
import java.util.List;

public class MoniteurController {
    private final MoniteurService service = new MoniteurService();

    public List<Moniteur> getAll() { return service.getAll(); }
    public Moniteur getById(Long id) { return service.getById(id); }

    public Moniteur create(MoniteurRequest req) {
        Moniteur m = new Moniteur();
        m.setNom(req.nom);
        m.setPrenom(req.prenom);
        m.setEmail(req.email);
        m.setMotDePasse(req.password);
        m.setTelephone(req.telephone);
        m.setSpecialite(req.specialite);
        m.setActif(true);
        service.create(m);
        return m;
    }

    public Moniteur update(Long id, MoniteurRequest req) {
        Moniteur m = service.getById(id);
        m.setNom(req.nom);
        m.setPrenom(req.prenom);
        m.setEmail(req.email);
        m.setTelephone(req.telephone);
        m.setSpecialite(req.specialite);
        service.update(id, m);
        return m;
    }

    public void delete(Long id) { service.delete(id); }
}