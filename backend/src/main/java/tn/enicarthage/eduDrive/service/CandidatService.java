package tn.enicarthage.eduDrive.service;

import tn.enicarthage.eduDrive.dao.CandidatDAO;
import tn.enicarthage.eduDrive.exception.ResourceNotFoundException;
import tn.enicarthage.eduDrive.exception.ValidationException;
import tn.enicarthage.eduDrive.model.Candidat;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public class CandidatService {

    private final CandidatDAO candidatDAO;

    public CandidatService() {
        this.candidatDAO = new CandidatDAO();
    }

    public List<Candidat> getAllCandidats() {
        return candidatDAO.findAll();
    }

    public Candidat getCandidatById(Long id) {
        Candidat c = candidatDAO.findById(id);
        if (c == null) throw new ResourceNotFoundException("Candidat introuvable : " + id);
        return c;
    }

    public void createCandidat(Candidat c) {
        validerCandidat(c);
        candidatDAO.create(c);
    }

    public void updateCandidat(Long id, Candidat c) {
        getCandidatById(id);
        validerCandidat(c);
        c.setId(id);
        candidatDAO.update(c);
    }

    public void deleteCandidat(Long id) {
        getCandidatById(id);
        candidatDAO.delete(id);
    }


    private void validerCandidat(Candidat c) {
        if (c.getDateNaissance() == null) {
            throw new ValidationException("La date de naissance est obligatoire.");
        }

        int age = Period.between(c.getDateNaissance(), LocalDate.now()).getYears();

        String typePermis = (c.getTypePermis() == null) ? "B" : c.getTypePermis();
        int ageMinimum = "A1".equals(typePermis) ? 16 : 18;

        if (age < ageMinimum) {
            throw new ValidationException("Le candidat doit avoir au moins " + ageMinimum + " ans pour le permis " + typePermis + ".");
        }
    }
}