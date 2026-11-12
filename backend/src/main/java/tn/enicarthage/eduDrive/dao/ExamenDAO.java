package tn.enicarthage.eduDrive.dao;

import tn.enicarthage.eduDrive.config.DatabaseConnection;
import tn.enicarthage.eduDrive.model.Examen;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExamenDAO {

    public void create(Examen e) {
        String sql = "INSERT INTO examen(candidat_id, type, date_examen, heure, resultat, numero_tentative) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, e.getCandidatId());
            stmt.setString(2, e.getType());
            stmt.setDate(3, Date.valueOf(e.getDateExamen()));
            stmt.setTime(4, Time.valueOf(e.getHeure()));
            stmt.setString(5, e.getResultat());
            stmt.setInt(6, e.getNumeroTentative());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) e.setId(rs.getLong(1));
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    public List<Examen> findAll() {
        List<Examen> list = new ArrayList<>();
        String sql = "SELECT * FROM examen";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException ex) { ex.printStackTrace(); }
        return list;
    }

    public Examen findById(Long id) {
        String sql = "SELECT * FROM examen WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return null;
    }

    public void update(Examen e) {
        String sql = "UPDATE examen SET candidat_id=?, type=?, date_examen=?, heure=?, resultat=?, numero_tentative=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, e.getCandidatId());
            stmt.setString(2, e.getType());
            stmt.setDate(3, Date.valueOf(e.getDateExamen()));
            stmt.setTime(4, Time.valueOf(e.getHeure()));
            stmt.setString(5, e.getResultat());
            stmt.setInt(6, e.getNumeroTentative());
            stmt.setLong(7, e.getId());
            stmt.executeUpdate();
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    public void delete(Long id) {
        String sql = "DELETE FROM examen WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    // --- NOUVELLES MÉTHODES POUR LE SERVICE ---
    public int countByCandidatAndType(Long candidatId, String type) {
        String sql = "SELECT COUNT(*) FROM examen WHERE candidat_id = ? AND type = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, candidatId);
            stmt.setString(2, type);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return 0;
    }

    public int countByCandidatAndTypeAndResultat(Long candidatId, String type, String resultat) {
        String sql = "SELECT COUNT(*) FROM examen WHERE candidat_id = ? AND type = ? AND resultat = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, candidatId);
            stmt.setString(2, type);
            stmt.setString(3, resultat);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return 0;
    }

    // NOUVEAU : Historique des examens d'un candidat (du plus récent au plus ancien)
    public List<Examen> findByCandidatId(Long candidatId) {
        List<Examen> list = new ArrayList<>();
        String sql = "SELECT * FROM examen WHERE candidat_id = ? ORDER BY date_examen DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, candidatId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return list;
    }

    private Examen mapRow(ResultSet rs) throws SQLException {
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