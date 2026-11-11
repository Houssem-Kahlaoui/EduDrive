package tn.enicarthage.eduDrive.controller;

import tn.enicarthage.eduDrive.dto.ExamenRequest;
import tn.enicarthage.eduDrive.model.Examen;
import tn.enicarthage.eduDrive.service.ExamenService;
import java.util.List;

public class ExamenController {
    private final ExamenService service = new ExamenService();

    public List<Examen> getAll() { return service.getAll(); }
    public Examen getById(Long id) { return service.getById(id); }

    public Examen create(ExamenRequest req) {
        Examen e = new Examen();
        e.setCandidatId(req.candidatId);
        e.setType(req.type);
        e.setDateExamen(req.dateExamen);
        e.setHeure(req.heure);
        service.create(e);
        return e;
    }

    public Examen update(Long id, ExamenRequest req) {
        Examen e = service.getById(id);
        e.setDateExamen(req.dateExamen);
        e.setHeure(req.heure);
        service.update(id, e);
        return service.getById(id);
    }

    public void delete(Long id) { service.delete(id); }
}
