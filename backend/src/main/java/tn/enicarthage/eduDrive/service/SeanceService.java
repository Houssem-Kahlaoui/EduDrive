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
        if (s.getDateSeance() == null) throw new ValidationException("La date de la séance est obligatoire.");
        if (s.getHeureDebut() == null) throw new ValidationException("L'heure de la séance est obligatoire.");
        if (s.getDateSeance().isBefore(LocalDate.now())) {
            throw new ValidationException("Impossible de planifier une séance dans le passé.");
        }
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

    // --- NOUVELLES MÉTHODES DE CONFIRMATION ---
    public Seance confirmerParCandidat(Long id) {
        Seance s = getById(id);
        s.setConfirmeParCandidat(true);
        // Si le moniteur a déjà confirmé, on valide la séance
        if (s.isConfirmeParMoniteur()) {
            s.setStatut("CONFIRMEE");
        }
        dao.update(s);
        return s;
    }

    public Seance confirmerParMoniteur(Long id) {
        Seance s = getById(id);
        s.setConfirmeParMoniteur(true);
        // Si le candidat a déjà confirmé, on valide la séance
        if (s.isConfirmeParCandidat()) {
            s.setStatut("CONFIRMEE");
        }
        dao.update(s);
        return s;
    }

    public Seance refuser(Long id, String motif) {
        Seance s = getById(id);
        s.setStatut("REFUSEE");
        s.setMotifRefus(motif);
        dao.update(s);
        return s;
    }
}