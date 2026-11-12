package tn.enicarthage.eduDrive.service;

import tn.enicarthage.eduDrive.dao.CandidatDAO;
import tn.enicarthage.eduDrive.dao.MoniteurDAO;
import tn.enicarthage.eduDrive.dao.SeanceDAO;
import tn.enicarthage.eduDrive.exception.ResourceNotFoundException;
import tn.enicarthage.eduDrive.exception.ValidationException;
import tn.enicarthage.eduDrive.model.Candidat;
import tn.enicarthage.eduDrive.model.Seance;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class SeanceService {
    private static final Set<String> STATUTS_FINAUX = Set.of("TERMINEE", "ANNULEE", "REFUSEE");

    private final SeanceDAO dao = new SeanceDAO();
    private final CandidatDAO candidatDAO = new CandidatDAO();
    private final MoniteurDAO moniteurDAO = new MoniteurDAO();

    public List<Seance> getAll() { return dao.findAll(); }

    public Seance getById(Long id) {
        Seance s = dao.findById(id);
        if (s == null) throw new ResourceNotFoundException("Seance introuvable : " + id);
        return s;
    }

    public void create(Seance s) {
        if (s.getDateSeance() == null) throw new ValidationException("La date de la séance est obligatoire.");
        if (s.getHeureDebut() == null) throw new ValidationException("L'heure de la séance est obligatoire.");
        if (s.getDateSeance().isBefore(LocalDate.now())) throw new ValidationException("Impossible de planifier une séance dans le passé.");
        if (s.getTypeSeance() == null || (!s.getTypeSeance().equals("CODE") && !s.getTypeSeance().equals("CONDUITE")))
            throw new ValidationException("Le type de séance doit être CODE ou CONDUITE.");

        if (candidatDAO.findById(s.getCandidatId()) == null)
            throw new ResourceNotFoundException("Candidat introuvable : " + s.getCandidatId());
        if (moniteurDAO.findById(s.getMoniteurId()) == null)
            throw new ResourceNotFoundException("Moniteur introuvable : " + s.getMoniteurId());

        if ("CODE".equals(s.getTypeSeance())) {
            s.setVehiculeId(null); // ✅ Une séance de CODE n'a pas de véhicule
        } else if (s.getVehiculeId() == null) {
            throw new ValidationException("Un véhicule est obligatoire pour une séance de conduite.");
        }

        if (dao.hasConflitHoraire(s.getCandidatId(), s.getMoniteurId(), s.getDateSeance(), s.getHeureDebut()))
            throw new ValidationException("Ce candidat ou ce moniteur a déjà une séance à cette heure.");

        s.setStatut("PLANIFIEE");
        s.setConfirmeParCandidat(false);
        s.setConfirmeParMoniteur(false);
        dao.create(s);
    }

    public void update(Long id, Seance s) {
        Seance existante = getById(id);
        
        // 1. Vérification statut
        if (STATUTS_FINAUX.contains(existante.getStatut()) && !existante.getStatut().equals(s.getStatut()))
            throw new ValidationException("Une séance " + existante.getStatut() + " ne peut plus être modifiée.");

        if (s.getDateSeance() == null || s.getHeureDebut() == null)
            throw new ValidationException("Date et heure obligatoires.");

        // 2. Vérification conflit (seulement si date/heure change)
        boolean planningModifie = !s.getDateSeance().equals(existante.getDateSeance()) || !s.getHeureDebut().equals(existante.getHeureDebut());
        if (planningModifie && dao.hasConflitHoraire(s.getCandidatId(), s.getMoniteurId(), s.getDateSeance(), s.getHeureDebut()))
            throw new ValidationException("Conflit d'horaire détecté.");

        // 3. Décrémentation des heures
        if ("TERMINEE".equals(s.getStatut()) && !"TERMINEE".equals(existante.getStatut()))
            appliquerDecrementation(existante);

        s.setId(id);
        dao.update(s);
    }

    public void delete(Long id) {
        getById(id);
        dao.delete(id);
    }

    public Seance confirmerParCandidat(Long id) {
        Seance s = getById(id);
        if (!"PLANIFIEE".equals(s.getStatut())) throw new ValidationException("Seule une séance PLANIFIEE peut être confirmée.");
        s.setConfirmeParCandidat(true);
        if (s.isConfirmeParMoniteur()) s.setStatut("CONFIRMEE");
        dao.update(s);
        return s;
    }

    public Seance confirmerParMoniteur(Long id) {
        Seance s = getById(id);
        if (!"PLANIFIEE".equals(s.getStatut())) throw new ValidationException("Seule une séance PLANIFIEE peut être confirmée.");
        s.setConfirmeParMoniteur(true);
        if (s.isConfirmeParCandidat()) s.setStatut("CONFIRMEE");
        dao.update(s);
        return s;
    }

    public Seance refuser(Long id, String motif) {
        Seance s = getById(id);
        if (!"PLANIFIEE".equals(s.getStatut())) throw new ValidationException("Seule une séance PLANIFIEE peut être refusée.");
        if (motif == null || motif.trim().isEmpty()) throw new ValidationException("Le motif de refus est obligatoire.");
        s.setStatut("REFUSEE");
        s.setMotifRefus(motif);
        dao.update(s);
        return s;
    }

    public Seance terminer(Long id) {
        Seance s = getById(id);
        if (!"CONFIRMEE".equals(s.getStatut())) throw new ValidationException("Seule une séance CONFIRMEE peut être terminée.");
        appliquerDecrementation(s);
        s.setStatut("TERMINEE");
        dao.update(s);
        return s;
    }

    private void appliquerDecrementation(Seance s) {
        Candidat c = candidatDAO.findById(s.getCandidatId());
        if (c == null) return;
        
        if ("CODE".equals(s.getTypeSeance())) {
            if (c.getHeuresCodeRestantes() <= 0) throw new ValidationException("Aucune heure de code restante.");
            c.setHeuresCodeRestantes(c.getHeuresCodeRestantes() - 1);
        } else {
            if (c.getHeuresConduiteRestantes() <= 0) throw new ValidationException("Aucune heure de conduite restante.");
            c.setHeuresConduiteRestantes(c.getHeuresConduiteRestantes() - 1);
        }
        candidatDAO.update(c);
    }
}