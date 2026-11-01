package com.edudrive.dao;

import com.edudrive.config.DatabaseConnection;
import com.edudrive.model.Moniteur;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MoniteurDAO {

    //create Moniteur
    public void create(Moniteur moniteur) {
        String sqlUser = "INSERT INTO utilisateur(nom, prenom, email, mot_de_passe, telephone, role) VALUES (?, ?, ?, ?, ?, 'MONITEUR')";
        String sqlMoniteur = "INSERT INTO moniteur(id, specialite) VALUES (?, ?)";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement stmtUser = conn.prepareStatement(sqlUser, Statement.RETURN_GENERATED_KEYS)) {
                stmtUser.setString(1, moniteur.getNom());
                stmtUser.setString(2, moniteur.getPrenom());
                stmtUser.setString(3, moniteur.getEmail());
                stmtUser.setString(4, moniteur.getMotDePasse());
                stmtUser.setString(5, moniteur.getTelephone());
                stmtUser.executeUpdate();

                try (ResultSet rs = stmtUser.getGeneratedKeys()) {
                    if (rs.next()) moniteur.setId(rs.getLong(1));
                }
            }

            try (PreparedStatement stmtMoniteur = conn.prepareStatement(sqlMoniteur)) {
                stmtMoniteur.setLong(1, moniteur.getId());
                stmtMoniteur.setString(2, moniteur.getSpecialite());
                stmtMoniteur.executeUpdate();
            }

            conn.commit();
            System.out.println("Moniteur créé avec succès.");
        } catch (SQLException e) {
            if (conn != null) { try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); } }
            e.printStackTrace();
        } finally {
            if (conn != null) { try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); } }
        }
    }

    //read All Moniteurs
    public List<Moniteur> findAll() {
        List<Moniteur> moniteurs = new ArrayList<>();
        String sql = "SELECT u.*, m.* FROM utilisateur u JOIN moniteur m ON u.id = m.id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) moniteurs.add(mapResultSetToMoniteur(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return moniteurs;
    }

    //read by id
    public Moniteur findById(Long id) {
        String sql = "SELECT u.*, m.* FROM utilisateur u JOIN moniteur m ON u.id = m.id WHERE u.id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapResultSetToMoniteur(rs);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    //update Moniteur
    public void update(Moniteur moniteur) {
        String sqlUser = "UPDATE utilisateur SET nom=?, prenom=?, email=?, mot_de_passe=?, telephone=? WHERE id=?";
        String sqlMoniteur = "UPDATE moniteur SET specialite=? WHERE id=?";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement stmtUser = conn.prepareStatement(sqlUser)) {
                stmtUser.setString(1, moniteur.getNom());
                stmtUser.setString(2, moniteur.getPrenom());
                stmtUser.setString(3, moniteur.getEmail());
                stmtUser.setString(4, moniteur.getMotDePasse());
                stmtUser.setString(5, moniteur.getTelephone());
                stmtUser.setLong(6, moniteur.getId());
                stmtUser.executeUpdate();
            }

            try (PreparedStatement stmtMoniteur = conn.prepareStatement(sqlMoniteur)) {
                stmtMoniteur.setString(1, moniteur.getSpecialite());
                stmtMoniteur.setLong(2, moniteur.getId());
                stmtMoniteur.executeUpdate();
            }

            conn.commit();
            System.out.println("Moniteur mis à jour.");
        } catch (SQLException e) {
            if (conn != null) { try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); } }
            e.printStackTrace();
        } finally {
            if (conn != null) { try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); } }
        }
    }

    //delete ( CASCADE )
    public void delete(Long id) {
        String sql = "DELETE FROM utilisateur WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
            System.out.println("Moniteur supprimé : " + id);
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private Moniteur mapResultSetToMoniteur(ResultSet rs) throws SQLException {
        Moniteur m = new Moniteur();
        m.setId(rs.getLong("id"));
        m.setNom(rs.getString("nom"));
        m.setPrenom(rs.getString("prenom"));
        m.setEmail(rs.getString("email"));
        m.setMotDePasse(rs.getString("mot_de_passe"));
        m.setTelephone(rs.getString("telephone"));
        m.setActif(rs.getBoolean("actif"));
        m.setRole(rs.getString("role"));
        m.setSpecialite(rs.getString("specialite"));
        return m;
    }
}