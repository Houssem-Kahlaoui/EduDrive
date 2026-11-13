package tn.enicarthage.eduDrive.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public final class DatabaseConnection {

    private static String dbUrl;
    private static String dbUser;
    private static String dbPassword;

    private static final String DATABASE = "edudrive_db";

    static {
        try {
            InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream("config.properties");
            Properties prop = new Properties();

            if (input == null) {
                System.err.println("ERREUR CRITIQUE : Impossible de trouver le fichier config.properties");
            } else {
                prop.load(input);
                dbUrl = prop.getProperty("db.url");
                dbUser = prop.getProperty("db.user");
                dbPassword = prop.getProperty("db.password");
            }
        } catch (Exception ex) {
            System.err.println("ERREUR lors du chargement de config.properties : " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private DatabaseConnection() {}

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    public static void main(String[] args) {
        Path databaseScript = Paths.get("src", "main", "resources", "db", "database.sql");

        try {
            String sql = Files.readString(databaseScript, StandardCharsets.UTF_8);
            initializeDatabase(sql);
            System.out.println("DB + tables creeres avec succes.");
        } catch (IOException | SQLException e) {
            System.err.println("Erreur d'initialisation de la base : " + e.getMessage());
            System.exit(1);
        }
    }

    private static void initializeDatabase(String databaseSql) throws SQLException {
        if (dbUrl == null || dbUser == null) {
            throw new SQLException("Configuration de la base de données introuvable. Vérifiez config.properties.");
        }


        String serverUrl = dbUrl.replaceAll("/" + DATABASE + ".*$", "/") 
                + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&allowMultiQueries=true";

        try (Connection connection = DriverManager.getConnection(serverUrl, dbUser, dbPassword);
             Statement statement = connection.createStatement()) {

            String database = "`" + DATABASE + "`";

            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS " + database
                    + " CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");

            statement.execute("USE " + database);

            statement.execute(databaseSql);
        }
    }
}