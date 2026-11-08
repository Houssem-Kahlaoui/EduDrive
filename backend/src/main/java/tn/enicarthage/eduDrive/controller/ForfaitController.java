package tn.enicarthage.eduDrive.controller;

import tn.enicarthage.eduDrive.dto.ForfaitRequest;
import tn.enicarthage.eduDrive.model.Forfait;
import tn.enicarthage.eduDrive.service.ForfaitService;
import java.util.List;

public class ForfaitController {
    private final ForfaitService service = new ForfaitService();

    public List<Forfait> getAll() { return service.getAllForfaits(); }
    public Forfait getById(Long id) { return service.getForfaitById(id); }

    public Forfait create(ForfaitRequest req) {
        Forfait f = new Forfait();
        f.setNom(req.nom);
        f.setTypeForfait(req.typeForfait);
        f.setHeuresCode(req.heuresCode);
        f.setHeuresConduite(req.heuresConduite);
        f.setPrix(req.prix);
        f.setActif(req.actif);
        service.createForfait(f);
        return f;
    }

    public Forfait update(Long id, ForfaitRequest req) {
        Forfait f = new Forfait();
        f.setNom(req.nom);
        f.setTypeForfait(req.typeForfait);
        f.setHeuresCode(req.heuresCode);
        f.setHeuresConduite(req.heuresConduite);
        f.setPrix(req.prix);
        f.setActif(req.actif);
        service.updateForfait(id, f);
        return service.getForfaitById(id);
    }

    public void delete(Long id) { service.deleteForfait(id); }
}