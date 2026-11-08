package tn.enicarthage.eduDrive.controller;

import tn.enicarthage.eduDrive.dao.ForfaitDAO;
import tn.enicarthage.eduDrive.dto.CandidatRequest;
import tn.enicarthage.eduDrive.dto.CandidatResponse;
import tn.enicarthage.eduDrive.dto.ChoixForfaitRequest;
import tn.enicarthage.eduDrive.model.Candidat;
import tn.enicarthage.eduDrive.model.Forfait;
import tn.enicarthage.eduDrive.service.CandidatService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CandidatController {
    private final CandidatService service = new CandidatService();
    private final ForfaitDAO forfaitDAO = new ForfaitDAO();

    public List<CandidatResponse> getAll() {
        List<CandidatResponse> responses = new ArrayList<>();
        for (Candidat c : service.getAllCandidats()) {
            responses.add(toResponse(c));
        }
        return responses;
    }

    public CandidatResponse getById(Long id) {
        return toResponse(service.getCandidatById(id));
    }

    public CandidatResponse create(CandidatRequest req) {
        Candidat c = new Candidat();
        c.setNom(req.nom);
        c.setPrenom(req.prenom);
        c.setEmail(req.email);
        c.setMotDePasse(req.password);
        c.setTelephone(req.telephone);
        c.setForfaitId(null); 
        c.setDateNaissance(req.dateNaissance);
        c.setAdresse(req.adresse);
        c.setTypePermis(req.typePermis);
        c.setDateInscription(LocalDate.now());
        c.setStatutFormation("EN_COURS");
        c.setActif(true);
        service.createCandidat(c);
        return toResponse(c);
    }

    public CandidatResponse update(Long id, CandidatRequest req) {
        Candidat c = service.getCandidatById(id);
        c.setNom(req.nom);
        c.setPrenom(req.prenom);
        c.setEmail(req.email);
        c.setTelephone(req.telephone);
        c.setDateNaissance(req.dateNaissance);
        c.setAdresse(req.adresse);
        c.setTypePermis(req.typePermis);
        service.updateCandidat(id, c);
        return toResponse(c);
    }

    public CandidatResponse choisirForfait(Long id, ChoixForfaitRequest req) {
        Candidat c = service.getCandidatById(id);
        c.setForfaitId(req.forfaitId);
        service.updateCandidat(id, c);
        return toResponse(c);
    }

    public void delete(Long id) { service.deleteCandidat(id); }

    private CandidatResponse toResponse(Candidat c) {
        CandidatResponse r = new CandidatResponse();
        r.id = c.getId();
        r.nom = c.getNom();
        r.prenom = c.getPrenom();
        r.email = c.getEmail();
        r.telephone = c.getTelephone();
        r.actif = c.isActif();
        r.forfaitId = c.getForfaitId();
        r.dateNaissance = c.getDateNaissance();
        r.statutFormation = c.getStatutFormation();
        r.dateValiditeCode = c.getDateValiditeCode();
        r.heuresCodeRestantes = c.getHeuresCodeRestantes();
        r.heuresConduiteRestantes = c.getHeuresConduiteRestantes();
        r.dossierComplet = c.isDossierComplet();
        if (c.getForfaitId() != null) {
            Forfait f = forfaitDAO.findById(c.getForfaitId());
            if (f != null) r.forfaitNom = f.getNom();
        }
        return r;
    }
}