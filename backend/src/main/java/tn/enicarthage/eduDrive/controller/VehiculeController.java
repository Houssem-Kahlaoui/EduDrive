package tn.enicarthage.eduDrive.controller;

import tn.enicarthage.eduDrive.dto.VehiculeRequest;
import tn.enicarthage.eduDrive.model.Vehicule;
import tn.enicarthage.eduDrive.service.VehiculeService;
import java.util.List;

public class VehiculeController {
    private final VehiculeService service = new VehiculeService();

    public List<Vehicule> getAll() { return service.getAll(); }
    
    public Vehicule getById(Long id) { return service.getById(id); }

    public Vehicule create(VehiculeRequest req) {
        Vehicule v = new Vehicule();
        v.setImmatriculation(req.immatriculation);
        v.setMarque(req.marque);
        v.setModele(req.modele);
        v.setEtat(req.etat != null ? req.etat : "DISPONIBLE");
        service.create(v);
        return v;
    }

    public Vehicule update(Long id, VehiculeRequest req) {
        Vehicule v = service.getById(id);
        v.setImmatriculation(req.immatriculation);
        v.setMarque(req.marque);
        v.setModele(req.modele);
        v.setEtat(req.etat);
        service.update(id, v);
        return v;
    }

    public void delete(Long id) { service.delete(id); }
}