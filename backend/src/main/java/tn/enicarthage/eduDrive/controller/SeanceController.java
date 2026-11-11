package tn.enicarthage.eduDrive.controller;

import tn.enicarthage.eduDrive.dto.SeanceRequest;
import tn.enicarthage.eduDrive.model.Seance;
import tn.enicarthage.eduDrive.service.SeanceService;
import java.util.List;

public class SeanceController {
    private final SeanceService service = new SeanceService();

    public List<Seance> getAll() { return service.getAll(); }
    public Seance getById(Long id) { return service.getById(id); }

    public Seance create(SeanceRequest req) {
        Seance s = new Seance();
        s.setCandidatId(req.candidatId);
        s.setMoniteurId(req.moniteurId);
        s.setVehiculeId(req.vehiculeId);
        s.setDateSeance(req.dateSeance);
        s.setHeureDebut(req.heureDebut);
        s.setDuree(req.duree);
        s.setTypeSeance(req.typeSeance);
        service.create(s);
        return s;
    }

    public Seance update(Long id, SeanceRequest req) {
        Seance s = service.getById(id);
        s.setCandidatId(req.candidatId);
        s.setMoniteurId(req.moniteurId);
        s.setVehiculeId(req.vehiculeId);
        s.setDateSeance(req.dateSeance);
        s.setHeureDebut(req.heureDebut);
        s.setDuree(req.duree);
        s.setTypeSeance(req.typeSeance);
        service.update(id, s);
        return s;
    }

    public void delete(Long id) { service.delete(id); }
}