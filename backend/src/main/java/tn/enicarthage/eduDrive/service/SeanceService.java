package tn.enicarthage.eduDrive.service;

import tn.enicarthage.eduDrive.dao.SeanceDAO;
import tn.enicarthage.eduDrive.exception.ResourceNotFoundException;
import tn.enicarthage.eduDrive.exception.ValidationException;
import tn.enicarthage.eduDrive.model.Seance;
import java.time.LocalDate;
import java.util.List;

public class SeanceService {
    private final SeanceDAO dao = new SeanceDAO();

    public List<Seance> getAll() { return dao.findAll(); }

    public Seance getById(Long id) {
        Seance s = dao.findById(id);
        if (s == null) throw new ResourceNotFoundException("Seance introuvable : " + id);
        return s;
    }

    public void create(Seance s) {

        if (s.getDateSeance().isBefore(LocalDate.now())) {
            throw new ValidationException("Impossible de planifier une séance dans le passé.");
        }

        //Vérifier les conflits d'horaires
        for (Seance existante : dao.findAll()) {
            if (existante.getDateSeance().equals(s.getDateSeance())
                    && existante.getHeureDebut().equals(s.getHeureDebut())) {

                if (existante.getCandidatId().equals(s.getCandidatId())) {
                    throw new ValidationException("Ce candidat a déjà une séance à cette heure.");
                }
                if (existante.getMoniteurId().equals(s.getMoniteurId())) {
                    throw new ValidationException("Ce moniteur est déjà occupé à cette heure.");
                }
            }
        }

        s.setStatut("PLANIFIEE");
        s.setConfirmeParCandidat(false);
        s.setConfirmeParMoniteur(false);
        dao.create(s);
    }

    public void update(Long id, Seance s) {
        getById(id);
        s.setId(id);
        dao.update(s);
    }

    public void delete(Long id) {
        getById(id);
        dao.delete(id);
    }
}