package com.edudrive.dao;

import com.edudrive.config.DatabaseConnection;
import com.edudrive.model.Examen;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExamenDAO {

    public void create(Examen examen) {
        String sql = "INSERT INTO examen(candidat_id, type, date_examen, heure, resultat, numero_tentative) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1, examen.getCandidatId());
            stmt.setString(2, examen.getType());
            stmt.setDate(3, Date.valueOf(examen.getDateExamen()));
            stmt.setTime(4, Time.valueOf(examen.getHeure()));
            stmt.setString(5, examen.getResultat());
            stmt.setInt(6, examen.getNumeroTentative());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) examen.setId(rs.getLong(1));
            }
            System.out.println("Examen créé : " + examen.getId());

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public List<Examen> findAll() {
        List<Examen> examens = new ArrayList<>();
        String sql = "SELECT * FROM examen";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) examens.add(mapResultSetToExamen(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return examens;
    }

    public Examen findById(Long id) {
        String sql = "SELECT * FROM examen WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapResultSetToExamen(rs);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public void update(Examen examen) {
        String sql = "UPDATE examen SET candidat_id=?, type=?, date_examen=?, heure=?, resultat=?, numero_tentative=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, examen.getCandidatId());
            stmt.setString(2, examen.getType());
            stmt.setDate(3, Date.valueOf(examen.getDateExamen()));
            stmt.setTime(4, Time.valueOf(examen.getHeure()));
            stmt.setString(5, examen.getResultat());
            stmt.setInt(6, examen.getNumeroTentative());
            stmt.setLong(7, examen.getId());

            stmt.executeUpdate();
            System.out.println("Examen mis à jour : " + examen.getId());
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void delete(Long id) {
        String sql = "DELETE FROM examen WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
            System.out.println("Examen supprimé : " + id);
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private Examen mapResultSetToExamen(ResultSet rs) throws SQLException {
        Examen e = new Examen();
        e.setId(rs.getLong("id"));
        e.setCandidatId(rs.getLong("candidat_id"));
        e.setType(rs.getString("type"));
        e.setDateExamen(rs.getDate("date_examen").toLocalDate());
        e.setHeure(rs.getTime("heure").toLocalTime());
        e.setResultat(rs.getString("resultat"));
        e.setNumeroTentative(rs.getInt("numero_tentative"));
        return e;
    }
}