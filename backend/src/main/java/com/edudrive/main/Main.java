package com.edudrive.main;

import com.edudrive.ApiServer;
import com.edudrive.config.DatabaseConnection;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        try (Connection c = DatabaseConnection.getConnection()) {
            ApiServer serveur = new ApiServer(8080);
            serveur.demarrer();
            System.out.println("Serveur HTTP demarre sur http://localhost:8080");
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }
}