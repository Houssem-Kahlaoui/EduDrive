package tn.enicarthage.eduDrive.service;

import tn.enicarthage.eduDrive.dao.MoniteurDAO;
import tn.enicarthage.eduDrive.dao.UtilisateurDAO;
import tn.enicarthage.eduDrive.exception.ConflictException;
import tn.enicarthage.eduDrive.exception.ResourceNotFoundException;
import tn.enicarthage.eduDrive.exception.ValidationException;
import tn.enicarthage.eduDrive.model.Moniteur;
import tn.enicarthage.eduDrive.util.PasswordUtil;

import java.util.List;

public class MoniteurService {
    private final MoniteurDAO dao = new MoniteurDAO();
    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    public List<Moniteur> getAll() { return dao.findAll(); }

    public Moniteur getById(Long id) {
        Moniteur m = dao.findById(id);
        if (m == null) throw new ResourceNotFoundException("Moniteur introuvable : " + id);
        return m;
    }

    public void create(Moniteur m) {
        if (m.getSpecialite() == null ||
                (!m.getSpecialite().equals("CODE") && !m.getSpecialite().equals("CONDUITE") && !m.getSpecialite().equals("CODE_CONDUITE"))) {
            throw new ValidationException("La spécialité doit être CODE, CONDUITE ou CODE_CONDUITE.");
        }
        if (utilisateurDAO.findByEmail(m.getEmail()) != null) {
            throw new ConflictException("Un utilisateur avec cet email existe déjà.");
        }
        if (m.getMotDePasse() != null && !m.getMotDePasse().isEmpty()) {
            m.setMotDePasse(PasswordUtil.hash(m.getMotDePasse()));
        }
        dao.create(m);
    }

    public void update(Long id, Moniteur m) {
        Moniteur existant = getById(id);

        if (m.getSpecialite() == null ||
                (!m.getSpecialite().equals("CODE") && !m.getSpecialite().equals("CONDUITE") && !m.getSpecialite().equals("CODE_CONDUITE"))) {
            throw new ValidationException("La spécialité doit être CODE, CONDUITE ou CODE_CONDUITE.");
        }

        if (!existant.getEmail().equals(m.getEmail())) {
            if (utilisateurDAO.findByEmail(m.getEmail()) != null) {
                throw new ConflictException("Un autre utilisateur avec cet email existe déjà.");
            }
        }

        if (m.getMotDePasse() != null
                && !m.getMotDePasse().isEmpty()
                && !m.getMotDePasse().equals(existant.getMotDePasse())) {
            m.setMotDePasse(PasswordUtil.hash(m.getMotDePasse()));
        }

        m.setId(id);
        dao.update(m);
    }

    public void delete(Long id) {
        getById(id);
        dao.delete(id);
    }
}