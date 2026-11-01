package com.edudrive.router;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.edudrive.dao.ForfaitDAO;
import com.edudrive.dao.UtilisateurDAO;
import com.edudrive.dto.LoginRequest;
import com.edudrive.exception.ResourceNotFoundException;
import com.edudrive.model.Forfait;
import com.edudrive.model.Utilisateur;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Router {
    private final Gson gson = new Gson();
    private final ForfaitDAO forfaitDAO = new ForfaitDAO();
    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    private record Reponse(int statut, String corps) {}

    public void dispatcher(HttpExchange exchange) throws IOException {
        String methode = exchange.getRequestMethod();
        String chemin = exchange.getRequestURI().getPath();
        String[] segments = chemin.split("/");

        try {
            Reponse reponse = router(methode, segments, exchange);
            envoyer(exchange, reponse.statut(), reponse.corps());
        } catch (ResourceNotFoundException e) {
            envoyer(exchange, 404, messageErreur(e));
        } catch (IllegalArgumentException e) {
            envoyer(exchange, 400, messageErreur(e));
        } catch (Exception e) {
            envoyer(exchange, 500, messageErreur(e));
        }
    }

    private Reponse router(String methode, String[] segments, HttpExchange exchange) throws IOException {
        if (segments.length < 2 || segments[1].isBlank()) {
            throw new ResourceNotFoundException("Route vide ou inconnue.");
        }

        switch (segments[1]) {
            case "login":
                return routeLogin(methode, exchange);
            case "forfaits":
                return routesForfait(methode, segments, exchange);
            default:
                throw new ResourceNotFoundException("Route inconnue : /" + segments[1]);
        }
    }

    //Login Route (Post/login)
    private Reponse routeLogin(String methode, HttpExchange exchange) throws IOException {
        if (!methode.equals("POST")) throw new IllegalArgumentException("Methode non supportee : " + methode);

        LoginRequest req = lireCorps(exchange, LoginRequest.class);
        Utilisateur user = utilisateurDAO.findByEmailAndPassword(req.email, req.password);

        if (user == null) {
            return new Reponse(401, "{\"erreur\":\"Email ou mot de passe incorrect\"}");
        }
        return new Reponse(200, gson.toJson(user));
    }

    // Forfait Routes
    private Reponse routesForfait(String methode, String[] segments, HttpExchange exchange) throws IOException {
        if (segments.length == 2) {
            switch (methode) {
                case "GET":
                    List<Forfait> forfaits = forfaitDAO.findAll();
                    return new Reponse(200, gson.toJson(forfaits));
                case "POST":
                    Forfait nouveau = lireCorps(exchange, Forfait.class);
                    forfaitDAO.create(nouveau);
                    return new Reponse(201, gson.toJson(nouveau));
                default:
                    throw new IllegalArgumentException("Methode non supportee : " + methode);
            }
        }

        if (segments.length == 3) {
            Long id = Long.parseLong(segments[2]);
            switch (methode) {
                case "GET":
                    Forfait f = forfaitDAO.findById(id);
                    if (f == null) throw new ResourceNotFoundException("Forfait introuvable : " + id);
                    return new Reponse(200, gson.toJson(f));
                case "PUT":
                    Forfait fModif = lireCorps(exchange, Forfait.class);
                    fModif.setId(id);
                    forfaitDAO.update(fModif);
                    return new Reponse(200, gson.toJson(fModif));
                case "DELETE":
                    forfaitDAO.delete(id);
                    return new Reponse(204, "");
                default:
                    throw new IllegalArgumentException("Methode non supportee : " + methode);
            }
        }
        throw new ResourceNotFoundException("Route inconnue");
    }

    //
    private <T> T lireCorps(HttpExchange exchange, Class<T> classe) throws IOException {
        try (InputStream is = exchange.getRequestBody()) {
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            if (body.isEmpty()) throw new IllegalArgumentException("Corps de requete vide");
            return gson.fromJson(body, classe);
        }
    }

    private void envoyer(HttpExchange exchange, int statut, String corps) throws IOException {
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

    private String messageErreur(Exception e) {
        return "{\"erreur\":\"" + e.getMessage() + "\"}";
    }
}