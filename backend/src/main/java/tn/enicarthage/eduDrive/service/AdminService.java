package tn.enicarthage.eduDrive.service;

import tn.enicarthage.eduDrive.dao.AdminDAO;
import tn.enicarthage.eduDrive.dao.UtilisateurDAO;
import tn.enicarthage.eduDrive.exception.ConflictException;
import tn.enicarthage.eduDrive.exception.ResourceNotFoundException;
import tn.enicarthage.eduDrive.exception.ValidationException;
import tn.enicarthage.eduDrive.model.Admin;
import tn.enicarthage.eduDrive.util.PasswordUtil;

import java.util.List;

public class AdminService {
    private final AdminDAO dao = new AdminDAO();
    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    public List<Admin> getAll() { return dao.findAll(); }

    public Admin getById(Long id) {
        Admin a = dao.findById(id);
        if (a == null) throw new ResourceNotFoundException("Admin introuvable : " + id);
        return a;
    }

    public void create(Admin a) {
        if (a.getEmail() == null || a.getEmail().trim().isEmpty()) {
            throw new ValidationException("L'email est obligatoire.");
        }
        if (utilisateurDAO.findByEmail(a.getEmail()) != null) {
            throw new ConflictException("Un utilisateur avec cet email existe déjà.");
        }
        if (a.getMotDePasse() != null && !a.getMotDePasse().isEmpty()) {
            a.setMotDePasse(PasswordUtil.hash(a.getMotDePasse()));
        }
        dao.create(a);
    }

    public void update(Long id, Admin a) {
        Admin existant = getById(id);

        if (!existant.getEmail().equals(a.getEmail())) {
            if (utilisateurDAO.findByEmail(a.getEmail()) != null) {
                throw new ConflictException("Un autre utilisateur avec cet email existe déjà.");
            }
        }

        if (a.getMotDePasse() != null
                && !a.getMotDePasse().isEmpty()
                && !a.getMotDePasse().equals(existant.getMotDePasse())) {
            a.setMotDePasse(PasswordUtil.hash(a.getMotDePasse()));
        }

        a.setId(id);
        dao.update(a);
    }

    public void delete(Long id) {
        getById(id);
        dao.delete(id);
    }
}