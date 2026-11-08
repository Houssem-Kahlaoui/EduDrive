package tn.enicarthage.eduDrive.service;

import tn.enicarthage.eduDrive.dao.ForfaitDAO;
import tn.enicarthage.eduDrive.exception.ResourceNotFoundException;
import tn.enicarthage.eduDrive.exception.ValidationException;
import tn.enicarthage.eduDrive.model.Forfait;

import java.math.BigDecimal;
import java.util.List;

public class ForfaitService {

    private final ForfaitDAO forfaitDAO;

    public ForfaitService() {
        this.forfaitDAO = new ForfaitDAO();
    }

    public List<Forfait> getAllForfaits() {
        return forfaitDAO.findAll();
    }

    public Forfait getForfaitById(Long id) {
        Forfait f = forfaitDAO.findById(id);
        if (f == null) {
            throw new ResourceNotFoundException("Forfait introuvable avec l'id : " + id);
        }
        return f;
    }

    public void createForfait(Forfait f) {
        // Prix doit etre >= 0 || nom forfait not null
        if (f.getPrix() == null || f.getPrix().compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("Le prix du forfait doit être positif.");
        }
        if (f.getNom() == null || f.getNom().trim().isEmpty()) {
            throw new ValidationException("Le nom du forfait est obligatoire.");
        }
        forfaitDAO.create(f);
    }

    public void updateForfait(Long id, Forfait f) {
        getForfaitById(id); // Forfait Exist

        if (f.getPrix() == null || f.getPrix().compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("Le prix du forfait doit être positif.");
        }
        f.setId(id);
        forfaitDAO.update(f);
    }

    public void deleteForfait(Long id) {
        getForfaitById(id);
        forfaitDAO.delete(id);
    }
}