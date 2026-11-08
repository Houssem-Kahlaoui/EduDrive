package tn.enicarthage.eduDrive.router;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import tn.enicarthage.eduDrive.controller.*;
import tn.enicarthage.eduDrive.dto.*;
import tn.enicarthage.eduDrive.exception.GlobalExceptionHandler;
import tn.enicarthage.eduDrive.exception.ResourceNotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class Router {
    private final Gson gson = new Gson();

    // Instanciate all controllers 
    private final AuthController authController = new AuthController();
    private final ForfaitController forfaitController = new ForfaitController();
    private final CandidatController candidatController = new CandidatController();
    private final MoniteurController moniteurController = new MoniteurController();
    private final AdminController adminController = new AdminController();

    private record Reponse(int statut, String corps) {}

    public void dispatcher(HttpExchange exchange) throws IOException {
        // Gestion CORS
        if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            ajouterEntetesCors(exchange);
            exchange.sendResponseHeaders(204, -1);
            exchange.close();
            return;
        }

        String methode = exchange.getRequestMethod();
        String chemin = exchange.getRequestURI().getPath();
        String[] segments = chemin.split("/");

        try {
            Reponse reponse = router(methode, segments, exchange);
            envoyer(exchange, reponse.statut(), reponse.corps());
        } catch (Exception e) {
            ErrorResponse error = GlobalExceptionHandler.handle(e);
            envoyer(exchange, error.statut, gson.toJson(error));
        }
    }

    private Reponse router(String methode, String[] segments, HttpExchange exchange) throws IOException {
        if (segments.length < 2 || segments[1].isBlank()) {
            throw new ResourceNotFoundException("Route vide ou inconnue.");
        }

        switch (segments[1]) {
            case "login": 
                return routeLogin(methode, exchange);
            case "admin": 
                return routeAdmin(methode, segments, exchange);
            default: 
                throw new ResourceNotFoundException("Route inconnue : /" + segments[1]);
        }
    }

    // Auth
    private Reponse routeLogin(String methode, HttpExchange exchange) throws IOException {
        if (!methode.equals("POST")) throw new IllegalArgumentException("Methode non supportee : " + methode);
        LoginRequest req = lireCorps(exchange, LoginRequest.class);
        LoginResponse res = authController.login(req);
        return new Reponse(200, gson.toJson(res));
    }

    // Admin
    private Reponse routeAdmin(String methode, String[] segments, HttpExchange exchange) throws IOException {
        if (segments.length < 3) throw new ResourceNotFoundException("Ressource admin manquante.");
        String ressource = segments[2];

        switch (ressource) {
            case "forfaits":   return routesForfait(methode, segments, exchange);
            case "candidats":  return routesCandidat(methode, segments, exchange);
            case "moniteurs":  return routesMoniteur(methode, segments, exchange);
            case "admins":     return routesAdminUser(methode, segments, exchange);
            default: throw new ResourceNotFoundException("Ressource admin inconnue : " + ressource);
        }
    }

    // Forfait CRUD
    private Reponse routesForfait(String methode, String[] segments, HttpExchange exchange) throws IOException {
        if (segments.length == 3) {
            if (methode.equals("GET")) return new Reponse(200, gson.toJson(forfaitController.getAll()));
            if (methode.equals("POST")) {
                ForfaitRequest req = lireCorps(exchange, ForfaitRequest.class);
                return new Reponse(201, gson.toJson(forfaitController.create(req)));
            }
        }
        if (segments.length == 4) {
            Long id = Long.parseLong(segments[3]);
            if (methode.equals("GET")) return new Reponse(200, gson.toJson(forfaitController.getById(id)));
            if (methode.equals("PUT")) {
                ForfaitRequest req = lireCorps(exchange, ForfaitRequest.class);
                return new Reponse(200, gson.toJson(forfaitController.update(id, req)));
            }
            if (methode.equals("DELETE")) {
                forfaitController.delete(id);
                return new Reponse(204, "");
            }
        }
        throw new IllegalArgumentException("Methode non supportee : " + methode);
    }

    // Condidat CRUD
    private Reponse routesCandidat(String methode, String[] segments, HttpExchange exchange) throws IOException {
        if (segments.length == 3) {
            if (methode.equals("GET")) return new Reponse(200, gson.toJson(candidatController.getAll()));
            if (methode.equals("POST")) {
                CandidatRequest req = lireCorps(exchange, CandidatRequest.class);
                return new Reponse(201, gson.toJson(candidatController.create(req)));
            }
        }
        if (segments.length == 4) {
            Long id = Long.parseLong(segments[3]);
            if (methode.equals("GET")) return new Reponse(200, gson.toJson(candidatController.getById(id)));
            if (methode.equals("PUT")) {
                CandidatRequest req = lireCorps(exchange, CandidatRequest.class);
                return new Reponse(200, gson.toJson(candidatController.update(id, req)));
            }
            if (methode.equals("DELETE")) {
                candidatController.delete(id);
                return new Reponse(204, "");
            }
        }
        throw new IllegalArgumentException("Methode non supportee : " + methode);
    }

    // MONITEURS CRUD
    private Reponse routesMoniteur(String methode, String[] segments, HttpExchange exchange) throws IOException {
        if (segments.length == 3) {
            if (methode.equals("GET")) return new Reponse(200, gson.toJson(moniteurController.getAll()));
            if (methode.equals("POST")) {
                MoniteurRequest req = lireCorps(exchange, MoniteurRequest.class);
                return new Reponse(201, gson.toJson(moniteurController.create(req)));
            }
        }
        if (segments.length == 4) {
            Long id = Long.parseLong(segments[3]);
            if (methode.equals("GET")) return new Reponse(200, gson.toJson(moniteurController.getById(id)));
            if (methode.equals("PUT")) {
                MoniteurRequest req = lireCorps(exchange, MoniteurRequest.class);
                return new Reponse(200, gson.toJson(moniteurController.update(id, req)));
            }
            if (methode.equals("DELETE")) {
                moniteurController.delete(id);
                return new Reponse(204, "");
            }
        }
        throw new IllegalArgumentException("Methode non supportee : " + methode);
    }

    // ADMINS CRUD
    private Reponse routesAdminUser(String methode, String[] segments, HttpExchange exchange) throws IOException {
        if (segments.length == 3) {
            if (methode.equals("GET")) return new Reponse(200, gson.toJson(adminController.getAll()));
            if (methode.equals("POST")) {
                LoginRequest req = lireCorps(exchange, LoginRequest.class);
                return new Reponse(201, gson.toJson(adminController.create(req)));
            }
        }
        if (segments.length == 4) {
            Long id = Long.parseLong(segments[3]);
            if (methode.equals("GET")) return new Reponse(200, gson.toJson(adminController.getById(id)));
            if (methode.equals("PUT")) {
                LoginRequest req = lireCorps(exchange, LoginRequest.class);
                return new Reponse(200, gson.toJson(adminController.update(id, req)));
            }
            if (methode.equals("DELETE")) {
                adminController.delete(id);
                return new Reponse(204, "");
            }
        }
        throw new IllegalArgumentException("Methode non supportee : " + methode);
    }

    //
    private <T> T lireCorps(HttpExchange exchange, Class<T> classe) throws IOException {
        try (InputStream is = exchange.getRequestBody()) {
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            if (body.isEmpty()) throw new IllegalArgumentException("Corps de requete vide");
            return gson.fromJson(body, classe);
        }
    }

    private void ajouterEntetesCors(HttpExchange exchange) {
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type, Authorization");
    }

    private void envoyer(HttpExchange exchange, int statut, String corps) throws IOException {
        ajouterEntetesCors(exchange);
        byte[] bytes = corps == null ? new byte[0] : corps.getBytes(StandardCharsets.UTF_8);
        if (bytes.length > 0) {
            exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
            exchange.sendResponseHeaders(statut, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        } else {
            exchange.sendResponseHeaders(statut, -1);
            exchange.close();
        }
    }
}