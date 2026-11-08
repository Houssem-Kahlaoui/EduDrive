package tn.enicarthage.eduDrive.controller;

import tn.enicarthage.eduDrive.dto.LoginRequest;
import tn.enicarthage.eduDrive.model.Admin;
import tn.enicarthage.eduDrive.service.AdminService;
import java.util.List;

public class AdminController {
    private final AdminService service = new AdminService();

    public List<Admin> getAll() { return service.getAll(); }
    
    public Admin getById(Long id) { return service.getById(id); }

    public Admin create(LoginRequest req) {
        Admin a = new Admin();
        a.setNom(req.email.split("@")[0]); 
        a.setPrenom("Admin");
        a.setEmail(req.email);
        a.setMotDePasse(req.password);
        a.setActif(true);
        service.create(a);
        return a;
    }

    public Admin update(Long id, LoginRequest req) {
        Admin a = service.getById(id);
        a.setEmail(req.email);
        a.setMotDePasse(req.password);
        service.update(id, a);
        return a;
    }

    public void delete(Long id) { service.delete(id); }
}