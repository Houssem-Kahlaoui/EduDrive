package tn.enicarthage.eduDrive.controller;

import tn.enicarthage.eduDrive.dto.AdminResponse;
import tn.enicarthage.eduDrive.dto.LoginRequest;
import tn.enicarthage.eduDrive.model.Admin;
import tn.enicarthage.eduDrive.service.AdminService;

import java.util.List;
import java.util.stream.Collectors;

public class AdminController {
    private final AdminService service = new AdminService();

    public List<AdminResponse> getAll() {
        return service.getAll().stream()
                .map(AdminResponse::from)
                .collect(Collectors.toList());
    }

    public AdminResponse getById(Long id) {
        return AdminResponse.from(service.getById(id));
    }

    public AdminResponse create(LoginRequest req) {
        Admin a = new Admin();
        a.setNom(req.email.split("@")[0]);
        a.setPrenom("Admin");
        a.setEmail(req.email);
        a.setMotDePasse(req.password);
        a.setActif(true);
        service.create(a);
        return AdminResponse.from(a);
    }

    public AdminResponse update(Long id, LoginRequest req) {
        Admin a = service.getById(id);
        a.setEmail(req.email);
        a.setMotDePasse(req.password);
        service.update(id, a);
        return AdminResponse.from(a);
    }

    public void delete(Long id) { service.delete(id); }
}