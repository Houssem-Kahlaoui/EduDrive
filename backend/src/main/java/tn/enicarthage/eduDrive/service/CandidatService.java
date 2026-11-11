package tn.enicarthage.eduDrive.service;

import tn.enicarthage.eduDrive.dao.CandidatDAO;
import tn.enicarthage.eduDrive.dao.UtilisateurDAO;
import tn.enicarthage.eduDrive.exception.ConflictException;
import tn.enicarthage.eduDrive.exception.ResourceNotFoundException;
import tn.enicarthage.eduDrive.exception.ValidationException;
import tn.enicarthage.eduDrive.model.Candidat;
import tn.enicarthage.eduDrive.util.PasswordUtil;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public class CandidatService {

    private final CandidatDAO candidatDAO;
    private final UtilisateurDAO utilisateurDAO;

    public CandidatService() {
        this.candidatDAO = new CandidatDAO();
        this.utilisateurDAO = new UtilisateurDAO();
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
        if (utilisateurDAO.findByEmail(c.getEmail()) != null) {
            throw new ConflictException("Un utilisateur avec cet email existe déjà.");
        }
        // Hash du mot de passe
        if (c.getMotDePasse() != null && !c.getMotDePasse().isEmpty()) {
            c.setMotDePasse(PasswordUtil.hash(c.getMotDePasse()));
        }
        candidatDAO.create(c);
    }

    public void updateCandidat(Long id, Candidat c) {
        Candidat existant = getCandidatById(id);
        validerCandidat(c);

        if (!existant.getEmail().equals(c.getEmail())) {
            if (utilisateurDAO.findByEmail(c.getEmail()) != null) {
                throw new ConflictException("Un autre utilisateur avec cet email existe déjà.");
            }
        }

        // Si le mot de passe a changé, on re-hash
        if (c.getMotDePasse() != null
                && !c.getMotDePasse().isEmpty()
                && !c.getMotDePasse().equals(existant.getMotDePasse())) {
            c.setMotDePasse(PasswordUtil.hash(c.getMotDePasse()));
        }

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