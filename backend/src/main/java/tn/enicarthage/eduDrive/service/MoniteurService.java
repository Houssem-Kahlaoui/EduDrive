package tn.enicarthage.eduDrive.service;

import tn.enicarthage.eduDrive.dao.MoniteurDAO;
import tn.enicarthage.eduDrive.exception.ResourceNotFoundException;
import tn.enicarthage.eduDrive.exception.ValidationException;
import tn.enicarthage.eduDrive.model.Moniteur;
import java.util.List;

public class MoniteurService {
    private final MoniteurDAO dao = new MoniteurDAO();

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
        dao.create(m);
    }

    public void update(Long id, Moniteur m) {
        getById(id);
        m.setId(id);
        dao.update(m);
    }

    public void delete(Long id) {
        getById(id);
        dao.delete(id);
    }
}