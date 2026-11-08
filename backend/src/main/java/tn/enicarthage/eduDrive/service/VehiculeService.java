package tn.enicarthage.eduDrive.service;

import tn.enicarthage.eduDrive.dao.SeanceDAO;
import tn.enicarthage.eduDrive.dao.VehiculeDAO;
import tn.enicarthage.eduDrive.exception.ResourceNotFoundException;
import tn.enicarthage.eduDrive.exception.ValidationException;
import tn.enicarthage.eduDrive.model.Seance;
import tn.enicarthage.eduDrive.model.Vehicule;
import java.util.List;

public class VehiculeService {
    private final VehiculeDAO vehiculeDAO = new VehiculeDAO();
    private final SeanceDAO seanceDAO = new SeanceDAO(); // Ajout

    public List<Vehicule> getAll() { return vehiculeDAO.findAll(); }

    public Vehicule getById(Long id) {
        Vehicule v = vehiculeDAO.findById(id);
        if (v == null) throw new ResourceNotFoundException("Vehicule introuvable : " + id);
        return v;
    }

    public void create(Vehicule v) {
        if (v.getImmatriculation() == null || v.getImmatriculation().trim().isEmpty()) {
            throw new ValidationException("L'immatriculation est obligatoire.");
        }
        for (Vehicule existant : vehiculeDAO.findAll()) {
            if (existant.getImmatriculation().equalsIgnoreCase(v.getImmatriculation())) {
                throw new ValidationException("Un véhicule avec cette immatriculation existe déjà.");
            }
        }
        vehiculeDAO.create(v);
    }

    public void update(Long id, Vehicule v) {
        getById(id);
        v.setId(id);
        vehiculeDAO.update(v);
    }

    public void delete(Long id) {
        getById(id);

        for (Seance s : seanceDAO.findAll()) {
            if (s.getVehiculeId() != null && s.getVehiculeId().equals(id)) {
                throw new ValidationException("Impossible de supprimer ce véhicule : il est utilisé dans une ou plusieurs séances.");
            }
        }

        vehiculeDAO.delete(id);
    }
}