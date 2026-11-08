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
        // 1. Vérifier que la date existe
        if (s.getDateSeance() == null) {
            throw new ValidationException("La date de la séance est obligatoire.");
        }
        if (s.getHeureDebut() == null) {
            throw new ValidationException("L'heure de la séance est obligatoire.");
        }

        // 2. Vérifier que la date n'est pas dans le passé
        if (s.getDateSeance().isBefore(LocalDate.now())) {
            throw new ValidationException("Impossible de planifier une séance dans le passé.");
        }

        // 3. Vérifier les conflits d'horaires (via la méthode SQL du DAO)
        if (dao.hasConflitHoraire(s.getCandidatId(), s.getMoniteurId(), s.getDateSeance(), s.getHeureDebut())) {
            throw new ValidationException("Ce candidat ou ce moniteur a déjà une séance à cette heure.");
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