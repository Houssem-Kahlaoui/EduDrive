package tn.enicarthage.eduDrive.service;

import tn.enicarthage.eduDrive.dao.AdminDAO;
import tn.enicarthage.eduDrive.exception.ResourceNotFoundException;
import tn.enicarthage.eduDrive.model.Admin;
import java.util.List;

public class AdminService {
    private final AdminDAO dao = new AdminDAO();

    public List<Admin> getAll() {
        return dao.findAll();
    }

    public Admin getById(Long id) {
        Admin a = dao.findById(id);
        if (a == null) throw new ResourceNotFoundException("Admin introuvable : " + id);
        return a;
    }

    public void create(Admin a) {
        if (a.getEmail() == null || a.getEmail().trim().isEmpty()) {
            throw new tn.enicarthage.eduDrive.exception.ValidationException("L'email est obligatoire.");
        }
        dao.create(a);
    }

    public void update(Long id, Admin a) {
        getById(id);
        a.setId(id);
        dao.update(a);
    }

    public void delete(Long id) {
        getById(id);
        dao.delete(id);
    }
}