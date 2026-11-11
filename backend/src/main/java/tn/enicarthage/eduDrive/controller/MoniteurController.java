package tn.enicarthage.eduDrive.controller;

import tn.enicarthage.eduDrive.dto.MoniteurRequest;
import tn.enicarthage.eduDrive.dto.MoniteurResponse;
import tn.enicarthage.eduDrive.model.Moniteur;
import tn.enicarthage.eduDrive.service.MoniteurService;

import java.util.List;
import java.util.stream.Collectors;

public class MoniteurController {
    private final MoniteurService service = new MoniteurService();

    public List<MoniteurResponse> getAll() {
        return service.getAll().stream()
                .map(MoniteurResponse::from)
                .collect(Collectors.toList());
    }

    public MoniteurResponse getById(Long id) {
        return MoniteurResponse.from(service.getById(id));
    }

    public MoniteurResponse create(MoniteurRequest req) {
        Moniteur m = new Moniteur();
        m.setNom(req.nom);
        m.setPrenom(req.prenom);
        m.setEmail(req.email);
        m.setMotDePasse(req.password);
        m.setTelephone(req.telephone);
        m.setSpecialite(req.specialite);
        m.setActif(true);
        service.create(m);
        return MoniteurResponse.from(m);
    }

    public MoniteurResponse update(Long id, MoniteurRequest req) {
        Moniteur m = service.getById(id);
        m.setNom(req.nom);
        m.setPrenom(req.prenom);
        m.setEmail(req.email);
        m.setTelephone(req.telephone);
        m.setSpecialite(req.specialite);
        service.update(id, m);
        return MoniteurResponse.from(m);
    }

    public void delete(Long id) { service.delete(id); }
}