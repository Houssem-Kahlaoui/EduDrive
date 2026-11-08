package tn.enicarthage.eduDrive.dao;

import tn.enicarthage.eduDrive.config.DatabaseConnection;
import tn.enicarthage.eduDrive.model.Candidat;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CandidatDAO {

    // 1. CREATE
    public void create(Candidat candidat) {
        String sqlUser = "INSERT INTO utilisateur(nom, prenom, email, mot_de_passe, telephone, role) VALUES (?, ?, ?, ?, ?, 'CANDIDAT')";
        String sqlCandidat = "INSERT INTO candidat(id, forfait_id, date_naissance, adresse, type_permis, date_inscription, statut_formation, heures_code_restantes, heures_conduite_restantes, dossier_complet) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement stmtUser = conn.prepareStatement(sqlUser, Statement.RETURN_GENERATED_KEYS)) {
                stmtUser.setString(1, candidat.getNom());
                stmtUser.setString(2, candidat.getPrenom());
                stmtUser.setString(3, candidat.getEmail());
                stmtUser.setString(4, candidat.getMotDePasse());
                stmtUser.setString(5, candidat.getTelephone());
                stmtUser.executeUpdate();

                try (ResultSet rs = stmtUser.getGeneratedKeys()) {
                    if (rs.next()) candidat.setId(rs.getLong(1));
                }
            }

            try (PreparedStatement stmtCandidat = conn.prepareStatement(sqlCandidat)) {
                stmtCandidat.setLong(1, candidat.getId());
                
                if (candidat.getForfaitId() != null) {
                    stmtCandidat.setLong(2, candidat.getForfaitId());
                } else {
                    stmtCandidat.setNull(2, Types.BIGINT);
                }

                // Gestion des dates
                stmtCandidat.setDate(3, candidat.getDateNaissance() != null ? Date.valueOf(candidat.getDateNaissance()) : null);
                stmtCandidat.setString(4, candidat.getAdresse());
                stmtCandidat.setString(5, candidat.getTypePermis());
                stmtCandidat.setDate(6, candidat.getDateInscription() != null ? Date.valueOf(candidat.getDateInscription()) : null);
                stmtCandidat.setString(7, candidat.getStatutFormation());
                stmtCandidat.setInt(8, candidat.getHeuresCodeRestantes());
                stmtCandidat.setInt(9, candidat.getHeuresConduiteRestantes());
                stmtCandidat.setBoolean(10, candidat.isDossierComplet());

                stmtCandidat.executeUpdate();
            }

            conn.commit();
            System.out.println("Candidat créé avec succès.");

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    System.err.println("Erreur, rollback effectué.");
                } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    // 2. READ (Tous)
    public List<Candidat> findAll() {
        List<Candidat> candidats = new ArrayList<>();
        String sql = "SELECT u.*, c.* FROM utilisateur u JOIN candidat c ON u.id = c.id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) candidats.add(mapResultSetToCandidat(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return candidats;
    }

    // 3. READ (Par ID)
    public Candidat findById(Long id) {
        String sql = "SELECT u.*, c.* FROM utilisateur u JOIN candidat c ON u.id = c.id WHERE u.id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapResultSetToCandidat(rs);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    // 4. UPDATE
    public void update(Candidat candidat) {
        String sqlUser = "UPDATE utilisateur SET nom=?, prenom=?, email=?, mot_de_passe=?, telephone=? WHERE id=?";
        String sqlCandidat = "UPDATE candidat SET forfait_id=?, date_naissance=?, adresse=?, type_permis=?, date_inscription=?, statut_formation=?, heures_code_restantes=?, heures_conduite_restantes=?, dossier_complet=? WHERE id=?";
        
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement stmtUser = conn.prepareStatement(sqlUser)) {
                stmtUser.setString(1, candidat.getNom());
                stmtUser.setString(2, candidat.getPrenom());
                stmtUser.setString(3, candidat.getEmail());
                stmtUser.setString(4, candidat.getMotDePasse());
                stmtUser.setString(5, candidat.getTelephone());
                stmtUser.setLong(6, candidat.getId());
                stmtUser.executeUpdate();
            }

            try (PreparedStatement stmtCandidat = conn.prepareStatement(sqlCandidat)) {
                // GESTION DU NULL POUR forfait_id
                if (candidat.getForfaitId() != null) {
                    stmtCandidat.setLong(1, candidat.getForfaitId());
                } else {
                    stmtCandidat.setNull(1, Types.BIGINT);
                }

                stmtCandidat.setDate(2, candidat.getDateNaissance() != null ? Date.valueOf(candidat.getDateNaissance()) : null);
                stmtCandidat.setString(3, candidat.getAdresse());
                stmtCandidat.setString(4, candidat.getTypePermis());
                stmtCandidat.setDate(5, candidat.getDateInscription() != null ? Date.valueOf(candidat.getDateInscription()) : null);
                stmtCandidat.setString(6, candidat.getStatutFormation());
                stmtCandidat.setInt(7, candidat.getHeuresCodeRestantes());
                stmtCandidat.setInt(8, candidat.getHeuresConduiteRestantes());
                stmtCandidat.setBoolean(9, candidat.isDossierComplet());
                stmtCandidat.setLong(10, candidat.getId());
                stmtCandidat.executeUpdate();
            }

            conn.commit();
            System.out.println("Candidat mis à jour.");

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    // 5. DELETE
    public void delete(Long id) {
        String sql = "DELETE FROM utilisateur WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
            System.out.println("Candidat supprimé : " + id);
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private Candidat mapResultSetToCandidat(ResultSet rs) throws SQLException {
        Candidat c = new Candidat();
        c.setId(rs.getLong("id"));
        c.setNom(rs.getString("nom"));
        c.setPrenom(rs.getString("prenom"));
        c.setEmail(rs.getString("email"));
        c.setMotDePasse(rs.getString("mot_de_passe"));
        c.setTelephone(rs.getString("telephone"));
        c.setActif(rs.getBoolean("actif"));
        c.setRole(rs.getString("role"));
        
        // GESTION DU NULL POUR forfait_id
        long forfaitId = rs.getLong("forfait_id");
        c.setForfaitId(rs.wasNull() ? null : forfaitId);

        c.setDateNaissance(rs.getDate("date_naissance") != null ? rs.getDate("date_naissance").toLocalDate() : null);
        c.setAdresse(rs.getString("adresse"));
        c.setTypePermis(rs.getString("type_permis"));
        c.setDateInscription(rs.getDate("date_inscription") != null ? rs.getDate("date_inscription").toLocalDate() : null);
        c.setStatutFormation(rs.getString("statut_formation"));
        c.setDateValiditeCode(rs.getDate("date_validite_code") != null ? rs.getDate("date_validite_code").toLocalDate() : null);
        c.setDateSouscription(rs.getDate("date_souscription") != null ? rs.getDate("date_souscription").toLocalDate() : null);
        c.setHeuresCodeRestantes(rs.getInt("heures_code_restantes"));
        c.setHeuresConduiteRestantes(rs.getInt("heures_conduite_restantes"));
        c.setDossierComplet(rs.getBoolean("dossier_complet"));
        
        return c;
    }
}