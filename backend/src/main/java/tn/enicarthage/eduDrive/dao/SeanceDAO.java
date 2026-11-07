package tn.enicarthage.eduDrive.dao;

import tn.enicarthage.eduDrive.config.DatabaseConnection;
import tn.enicarthage.eduDrive.model.Seance;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SeanceDAO {

    public void create(Seance seance) {
        String sql = "INSERT INTO seance(candidat_id, moniteur_id, vehicule_id, date_seance, heure_debut, duree, type_seance, statut, confirme_par_candidat, confirme_par_moniteur, motif_refus, note, commentaire) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1, seance.getCandidatId());
            stmt.setLong(2, seance.getMoniteurId());
            stmt.setLong(3, seance.getVehiculeId());
            stmt.setDate(4, Date.valueOf(seance.getDateSeance()));
            stmt.setTime(5, Time.valueOf(seance.getHeureDebut()));
            stmt.setInt(6, seance.getDuree());
            stmt.setString(7, seance.getTypeSeance());
            stmt.setString(8, seance.getStatut());
            stmt.setBoolean(9, seance.isConfirmeParCandidat());
            stmt.setBoolean(10, seance.isConfirmeParMoniteur());
            stmt.setString(11, seance.getMotifRefus());
            if (seance.getNote() != null) stmt.setInt(12, seance.getNote()); else stmt.setNull(12, Types.INTEGER);
            stmt.setString(13, seance.getCommentaire());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) seance.setId(rs.getLong(1));
            }
            System.out.println("Séance créée : " + seance.getId());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Seance> findAll() {
        List<Seance> seances = new ArrayList<>();
        String sql = "SELECT * FROM seance";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) seances.add(mapResultSetToSeance(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return seances;
    }

    public Seance findById(Long id) {
        String sql = "SELECT * FROM seance WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapResultSetToSeance(rs);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public void update(Seance seance) {
        String sql = "UPDATE seance SET candidat_id=?, moniteur_id=?, vehicule_id=?, date_seance=?, heure_debut=?, duree=?, type_seance=?, statut=?, confirme_par_candidat=?, confirme_par_moniteur=?, motif_refus=?, note=?, commentaire=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, seance.getCandidatId());
            stmt.setLong(2, seance.getMoniteurId());
            stmt.setLong(3, seance.getVehiculeId());
            stmt.setDate(4, Date.valueOf(seance.getDateSeance()));
            stmt.setTime(5, Time.valueOf(seance.getHeureDebut()));
            stmt.setInt(6, seance.getDuree());
            stmt.setString(7, seance.getTypeSeance());
            stmt.setString(8, seance.getStatut());
            stmt.setBoolean(9, seance.isConfirmeParCandidat());
            stmt.setBoolean(10, seance.isConfirmeParMoniteur());
            stmt.setString(11, seance.getMotifRefus());
            if (seance.getNote() != null) stmt.setInt(12, seance.getNote()); else stmt.setNull(12, Types.INTEGER);
            stmt.setString(13, seance.getCommentaire());
            stmt.setLong(14, seance.getId());

            stmt.executeUpdate();
            System.out.println("Séance mise à jour : " + seance.getId());
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void delete(Long id) {
        String sql = "DELETE FROM seance WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
            System.out.println("Séance supprimée : " + id);
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private Seance mapResultSetToSeance(ResultSet rs) throws SQLException {
        Seance s = new Seance();
        s.setId(rs.getLong("id"));
        s.setCandidatId(rs.getLong("candidat_id"));
        s.setMoniteurId(rs.getLong("moniteur_id"));
        s.setVehiculeId(rs.getLong("vehicule_id"));
        s.setDateSeance(rs.getDate("date_seance").toLocalDate());
        s.setHeureDebut(rs.getTime("heure_debut").toLocalTime());
        s.setDuree(rs.getInt("duree"));
        s.setTypeSeance(rs.getString("type_seance"));
        s.setStatut(rs.getString("statut"));
        s.setConfirmeParCandidat(rs.getBoolean("confirme_par_candidat"));
        s.setConfirmeParMoniteur(rs.getBoolean("confirme_par_moniteur"));
        s.setMotifRefus(rs.getString("motif_refus"));
        int note = rs.getInt("note"); s.setNote(rs.wasNull() ? null : note);
        s.setCommentaire(rs.getString("commentaire"));
        return s;
    }
}