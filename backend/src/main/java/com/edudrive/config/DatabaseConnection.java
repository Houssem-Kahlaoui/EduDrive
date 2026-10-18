package com.edudrive.config;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    private static String url;
    private static String user;
    private static String password;

    static {
        try {
            InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream("db.properties");
            Properties prop = new Properties();

            if (input == null) {
                System.err.println("ERREUR CRITIQUE : Impossible de trouver le fichier db.properties");
            } else {
                // On charge les propriétés seulement si le fichier existe
                prop.load(input);
                url = prop.getProperty("db.url");
                user = prop.getProperty("db.user");
                password = prop.getProperty("db.password");
            }

        } catch (Exception ex) {
            System.err.println("ERREUR lors du chargement de db.properties : " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}