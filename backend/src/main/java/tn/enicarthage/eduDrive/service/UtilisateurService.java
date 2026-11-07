package tn.enicarthage.eduDrive.service;

import tn.enicarthage.eduDrive.dao.UtilisateurDAO;
import tn.enicarthage.eduDrive.exception.AuthenticationException;
import tn.enicarthage.eduDrive.model.Utilisateur;

public class UtilisateurService {

    private final UtilisateurDAO utilisateurDAO;

    public UtilisateurService() {
        this.utilisateurDAO = new UtilisateurDAO();
    }

    public Utilisateur login(String email, String password) {
        if (email == null || password == null) {
            throw new AuthenticationException("Email et mot de passe obligatoires.");
        }

        Utilisateur user = utilisateurDAO.findByEmailAndPassword(email, password);

        if (user == null) {
            throw new AuthenticationException("Email ou mot de passe incorrect.");
        }

        if (!user.isActif()) {
            throw new AuthenticationException("Ce compte est désactivé.");
        }

        return user;
    }
}