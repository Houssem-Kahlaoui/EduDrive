package tn.enicarthage.eduDrive.dao;

import tn.enicarthage.eduDrive.config.DatabaseConnection;
import tn.enicarthage.eduDrive.model.Seance;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SeanceDAO {

    public void create(Seance s) {
        String sql = "INSERT INTO seance(candidat_id, moniteur_id, vehicule_id, date_seance, heure_debut, duree, type_seance, statut, confirme_par_candidat, confirme_par_moniteur, motif_refus, note, commentaire) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, s.getCandidatId());
            stmt.setLong(2, s.getMoniteurId());

            // CORRECTION : Gérer le null pour le véhicule (séance de CODE)
            if (s.getVehiculeId() != null) stmt.setLong(3, s.getVehiculeId());
            else stmt.setNull(3, Types.BIGINT);

            stmt.setDate(4, Date.valueOf(s.getDateSeance()));
            stmt.setTime(5, Time.valueOf(s.getHeureDebut()));
            stmt.setInt(6, s.getDuree());
            stmt.setString(7, s.getTypeSeance());
            stmt.setString(8, s.getStatut());
            stmt.setBoolean(9, s.isConfirmeParCandidat());
            stmt.setBoolean(10, s.isConfirmeParMoniteur());
            stmt.setString(11, s.getMotifRefus());
            if (s.getNote() != null) stmt.setInt(12, s.getNote()); else stmt.setNull(12, Types.INTEGER);
            stmt.setString(13, s.getCommentaire());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) s.setId(rs.getLong(1));
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    public List<Seance> findAll() {
        List<Seance> list = new ArrayList<>();
        String sql = "SELECT * FROM seance";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException ex) { ex.printStackTrace(); }
        return list;
    }

    public Seance findById(Long id) {
        String sql = "SELECT * FROM seance WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return null;
    }

    public void update(Seance s) {
        String sql = "UPDATE seance SET candidat_id=?, moniteur_id=?, vehicule_id=?, date_seance=?, heure_debut=?, duree=?, type_seance=?, statut=?, confirme_par_candidat=?, confirme_par_moniteur=?, motif_refus=?, note=?, commentaire=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, s.getCandidatId());
            stmt.setLong(2, s.getMoniteurId());

            // CORRECTION : Gérer le null pour le véhicule (séance de CODE)
            if (s.getVehiculeId() != null) stmt.setLong(3, s.getVehiculeId());
            else stmt.setNull(3, Types.BIGINT);

            stmt.setDate(4, Date.valueOf(s.getDateSeance()));
            stmt.setTime(5, Time.valueOf(s.getHeureDebut()));
            stmt.setInt(6, s.getDuree());
            stmt.setString(7, s.getTypeSeance());
            stmt.setString(8, s.getStatut());
            stmt.setBoolean(9, s.isConfirmeParCandidat());
            stmt.setBoolean(10, s.isConfirmeParMoniteur());
            stmt.setString(11, s.getMotifRefus());
            if (s.getNote() != null) stmt.setInt(12, s.getNote()); else stmt.setNull(12, Types.INTEGER);
            stmt.setString(13, s.getCommentaire());
            stmt.setLong(14, s.getId());
            stmt.executeUpdate();
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    public void delete(Long id) {
        String sql = "DELETE FROM seance WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    // NOUVEAU : Vérification des conflits d'agenda (appelée par le Service)
    public boolean hasConflitHoraire(Long candidatId, Long moniteurId, java.time.LocalDate date, java.time.LocalTime heure) {
        String sql = "SELECT 1 FROM seance WHERE date_seance = ? AND heure_debut = ? " +
                "AND statut NOT IN ('ANNULEE', 'REFUSEE') " +
                "AND (candidat_id = ? OR moniteur_id = ?) LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(date));
            stmt.setTime(2, Time.valueOf(heure));
            stmt.setLong(3, candidatId);
            stmt.setLong(4, moniteurId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Renvoie true si un conflit est trouvé
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return false;
    }

    // NOUVEAU : Séances d'un candidat (triées de la plus récente à la plus ancienne)
    public List<Seance> findByCandidatId(Long candidatId) {
        return findBy("SELECT * FROM seance WHERE candidat_id = ? ORDER BY date_seance DESC, heure_debut DESC", candidatId);
    }

    // NOUVEAU : Séances d'un moniteur (triées de la plus récente à la plus ancienne)
    public List<Seance> findByMoniteurId(Long moniteurId) {
        return findBy("SELECT * FROM seance WHERE moniteur_id = ? ORDER BY date_seance DESC, heure_debut DESC", moniteurId);
    }

    private List<Seance> findBy(String sql, Long id) {
        List<Seance> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return list;
    }

    private Seance mapRow(ResultSet rs) throws SQLException {
        Seance s = new Seance();
        s.setId(rs.getLong("id"));
        s.setCandidatId(rs.getLong("candidat_id"));
        s.setMoniteurId(rs.getLong("moniteur_id"));

        long vId = rs.getLong("vehicule_id");
        s.setVehiculeId(rs.wasNull() ? null : vId); // ✅ Gestion du NULL

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