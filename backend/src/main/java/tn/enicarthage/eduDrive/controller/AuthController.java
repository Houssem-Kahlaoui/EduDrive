package tn.enicarthage.eduDrive.controller;

import tn.enicarthage.eduDrive.dto.LoginRequest;
import tn.enicarthage.eduDrive.dto.LoginResponse;
import tn.enicarthage.eduDrive.model.Utilisateur;
import tn.enicarthage.eduDrive.service.UtilisateurService;
import tn.enicarthage.eduDrive.util.JwtUtil; 

public class AuthController {
    private final UtilisateurService service = new UtilisateurService();

    public LoginResponse login(LoginRequest req) {
        Utilisateur user = service.login(req.email, req.password);
        
        String token = JwtUtil.generateToken(user);

        return new LoginResponse(
            token,
            user.getId(), user.getNom(), user.getPrenom(),
            user.getEmail(), user.getTelephone(), user.isActif(), user.getRole()
        );
    }
}