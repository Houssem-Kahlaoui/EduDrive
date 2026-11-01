package com.edudrive.dao;

import com.edudrive.config.DatabaseConnection;
import com.edudrive.model.Admin;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {

    public void create(Admin admin) {
        String sqlUser = "INSERT INTO utilisateur(nom, prenom, email, mot_de_passe, telephone, role) VALUES (?, ?, ?, ?, ?, 'ADMIN')";
        String sqlAdmin = "INSERT INTO admin(id) VALUES (?)";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement stmtUser = conn.prepareStatement(sqlUser, Statement.RETURN_GENERATED_KEYS)) {
                stmtUser.setString(1, admin.getNom());
                stmtUser.setString(2, admin.getPrenom());
                stmtUser.setString(3, admin.getEmail());
                stmtUser.setString(4, admin.getMotDePasse());
                stmtUser.setString(5, admin.getTelephone());
                stmtUser.executeUpdate();

                try (ResultSet rs = stmtUser.getGeneratedKeys()) {
                    if (rs.next()) admin.setId(rs.getLong(1));
                }
            }

            try (PreparedStatement stmtAdmin = conn.prepareStatement(sqlAdmin)) {
                stmtAdmin.setLong(1, admin.getId());
                stmtAdmin.executeUpdate();
            }

            conn.commit();
            System.out.println("Admin créé avec succès.");
        } catch (SQLException e) {
            if (conn != null) { try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); } }
            e.printStackTrace();
        } finally {
            if (conn != null) { try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); } }
        }
    }

    public List<Admin> findAll() {
        List<Admin> admins = new ArrayList<>();
        String sql = "SELECT u.* FROM utilisateur u JOIN admin a ON u.id = a.id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) admins.add(mapResultSetToAdmin(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return admins;
    }

    public Admin findById(Long id) {
        String sql = "SELECT u.* FROM utilisateur u JOIN admin a ON u.id = a.id WHERE u.id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapResultSetToAdmin(rs);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public void update(Admin admin) {
        String sql = "UPDATE utilisateur SET nom=?, prenom=?, email=?, mot_de_passe=?, telephone=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, admin.getNom());
            stmt.setString(2, admin.getPrenom());
            stmt.setString(3, admin.getEmail());
            stmt.setString(4, admin.getMotDePasse());
            stmt.setString(5, admin.getTelephone());
            stmt.setLong(6, admin.getId());
            stmt.executeUpdate();
            System.out.println("Admin mis à jour.");
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void delete(Long id) {
        String sql = "DELETE FROM utilisateur WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
            System.out.println("Admin supprimé : " + id);
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private Admin mapResultSetToAdmin(ResultSet rs) throws SQLException {
        Admin a = new Admin();
        a.setId(rs.getLong("id"));
        a.setNom(rs.getString("nom"));
        a.setPrenom(rs.getString("prenom"));
        a.setEmail(rs.getString("email"));
        a.setMotDePasse(rs.getString("mot_de_passe"));
        a.setTelephone(rs.getString("telephone"));
        a.setActif(rs.getBoolean("actif"));
        a.setRole(rs.getString("role"));
        return a;
    }
}