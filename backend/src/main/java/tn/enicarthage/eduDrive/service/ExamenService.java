package tn.enicarthage.eduDrive.service;

import tn.enicarthage.eduDrive.dao.CandidatDAO;
import tn.enicarthage.eduDrive.dao.ExamenDAO;
import tn.enicarthage.eduDrive.exception.ResourceNotFoundException;
import tn.enicarthage.eduDrive.exception.ValidationException;
import tn.enicarthage.eduDrive.model.Candidat;
import tn.enicarthage.eduDrive.model.Examen;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class ExamenService {
    private static final Set<String> TYPES = Set.of("CODE", "CIRCULATION", "MANOEUVRES");
    private static final Set<String> RESULTATS = Set.of("REUSSI", "ECHOUE", "EN_ATTENTE");
    private static final int MAX_CIRCULATION = 6;
    private static final int MAX_MANOEUVRE = 2;

    private final ExamenDAO examenDAO;
    private final CandidatDAO candidatDAO;

    public ExamenService() {
        this(new ExamenDAO(), new CandidatDAO());
    }
    public ExamenService(ExamenDAO e, CandidatDAO c) {
        this.examenDAO = e;
        this.candidatDAO = c;
    }

    public List<Examen> getAll() { return examenDAO.findAll(); }

    public Examen getById(Long id) {
        Examen e = examenDAO.findById(id);
        if (e == null) throw new ResourceNotFoundException("Examen introuvable : " + id);
        return e;
    }

    public void create(Examen e) {
        if (e.getCandidatId() == null) throw new ValidationException("Le candidat est obligatoire.");
        if (!TYPES.contains(e.getType())) throw new ValidationException("Type d'examen invalide.");
        if (e.getDateExamen() == null) throw new ValidationException("La date d'examen est obligatoire.");

        if (candidatDAO.findById(e.getCandidatId()) == null)
            throw new ResourceNotFoundException("Candidat introuvable : " + e.getCandidatId());

        int tentatives = examenDAO.countByCandidatAndType(e.getCandidatId(), e.getType());

        if ("CIRCULATION".equals(e.getType())) {
            int echecs = examenDAO.countByCandidatAndTypeAndResultat(e.getCandidatId(), "CIRCULATION", "ECHOUE");
            if (echecs >= MAX_CIRCULATION)
                throw new ValidationException("Maximum " + MAX_CIRCULATION + " tentatives de circulation atteint. Le code doit être repassé.");
        }
        if ("MANOEUVRES".equals(e.getType())) {
            int echecs = examenDAO.countByCandidatAndTypeAndResultat(e.getCandidatId(), "MANOEUVRES", "ECHOUE");
            if (echecs >= MAX_MANOEUVRE)
                throw new ValidationException("Maximum " + MAX_MANOEUVRE + " tentatives de manœuvres atteint. La circulation doit être repassée.");
        }

        e.setNumeroTentative(tentatives + 1);
        e.setResultat("EN_ATTENTE");
        examenDAO.create(e);
    }

    public void update(Long id, Examen e) {
        Examen existant = getById(id);
        e.setCandidatId(existant.getCandidatId());
        e.setType(existant.getType());
        e.setNumeroTentative(existant.getNumeroTentative());

        if (e.getDateExamen() == null) throw new ValidationException("La date d'examen est obligatoire.");
        if (e.getResultat() == null || !RESULTATS.contains(e.getResultat())) throw new ValidationException("Résultat invalide.");

        boolean nouveauEchec = !"ECHOUE".equals(existant.getResultat()) && "ECHOUE".equals(e.getResultat());
        boolean nouveauReussi = !"REUSSI".equals(existant.getResultat()) && "REUSSI".equals(e.getResultat());

        if (nouveauEchec) appliquerRegleEchec(existant);
        if (nouveauReussi) appliquerReussite(existant);

        e.setId(id);
        examenDAO.update(e);
    }

    public void delete(Long id) {
        getById(id);
        examenDAO.delete(id);
    }

    private void appliquerRegleEchec(Examen e) {
        if ("CIRCULATION".equals(e.getType()) && e.getNumeroTentative() >= MAX_CIRCULATION) {
            Candidat c = candidatDAO.findById(e.getCandidatId());
            if (c != null) {
                c.setDateValiditeCode(null);
                c.setStatutFormation("EN_COURS");
                candidatDAO.update(c);
            }
            throw new ValidationException("Échec de la " + e.getNumeroTentative() + "ᵉ circulation : le candidat doit repasser le Code.");
        }
        if ("MANOEUVRES".equals(e.getType()) && e.getNumeroTentative() >= MAX_MANOEUVRE) {
            Candidat c = candidatDAO.findById(e.getCandidatId());
            if (c != null && "PRET_EXAMEN".equals(c.getStatutFormation())) {
                c.setStatutFormation("EN_COURS");
                candidatDAO.update(c);
            }
            throw new ValidationException("Échec de la " + e.getNumeroTentative() + "ᵉ manœuvre : le candidat doit repasser la Circulation.");
        }
    }

    private void appliquerReussite(Examen e) {
        if ("CODE".equals(e.getType())) {
            Candidat c = candidatDAO.findById(e.getCandidatId());
            if (c != null) {
                c.setDateValiditeCode(LocalDate.now().plusMonths(15));
                candidatDAO.update(c);
            }
        }
    }
}