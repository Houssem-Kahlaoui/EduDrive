package com.edudrive.dao;

import com.edudrive.config.DatabaseConnection;
import com.edudrive.model.Candidat;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CandidatDAO {

    //create Candidat
    public void create(Candidat candidat) {
        String sqlUser = "INSERT INTO utilisateur(nom, prenom, email, mot_de_passe, telephone, role) VALUES (?, ?, ?, ?, ?, 'CANDIDAT')";
        String sqlCandidat = "INSERT INTO candidat(id, forfait_id, date_naissance, adresse, type_permis, date_inscription, statut_formation, heures_code_restantes, heures_conduite_restantes, dossier_complet) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            //Insertion dans utilisateur
            try (PreparedStatement stmtUser = conn.prepareStatement(sqlUser, Statement.RETURN_GENERATED_KEYS)) {
                stmtUser.setString(1, candidat.getNom());
                stmtUser.setString(2, candidat.getPrenom());
                stmtUser.setString(3, candidat.getEmail());
                stmtUser.setString(4, candidat.getMotDePasse());
                stmtUser.setString(5, candidat.getTelephone());
                stmtUser.executeUpdate();

                //get utilisateur id
                try (ResultSet rs = stmtUser.getGeneratedKeys()) {
                    if (rs.next()) {
                        candidat.setId(rs.getLong(1));
                    }
                }
            }

            //Insertion dans candidat avec l'id recc
            try (PreparedStatement stmtCandidat = conn.prepareStatement(sqlCandidat)) {
                stmtCandidat.setLong(1, candidat.getId());
                stmtCandidat.setLong(2, candidat.getForfaitId());

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
            System.out.println("Candidat créé avec succès .");

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    System.err.println("!Erreur");
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    //read all candidats
    public List<Candidat> findAll() {
        List<Candidat> candidats = new ArrayList<>();
        // Requête avec JOIN pour récupérer les infos de 'utilisateur' et 'candidat'
        String sql = "SELECT u.*, c.* FROM utilisateur u JOIN candidat c ON u.id = c.id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                candidats.add(mapResultSetToCandidat(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return candidats;
    }

    //read Candidat with id
    public Candidat findById(Long id) {
        String sql = "SELECT u.*, c.* FROM utilisateur u JOIN candidat c ON u.id = c.id WHERE u.id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCandidat(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //update utilisateur + candidat
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
                stmtCandidat.setLong(1, candidat.getForfaitId());
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
            System.out.println("Candidat mis à jour avec succès.");

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

    //delete candidat with id ( utilisateur avec delete CASCADE )
    public void delete(Long id) {
        String sql = "DELETE FROM utilisateur WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
            System.out.println("Candidat supprimé : " + id);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //ORM
    private Candidat mapResultSetToCandidat(ResultSet rs) throws SQLException {
        Candidat c = new Candidat();
        //champs de utilisateur
        c.setId(rs.getLong("id"));
        c.setNom(rs.getString("nom"));
        c.setPrenom(rs.getString("prenom"));
        c.setEmail(rs.getString("email"));
        c.setMotDePasse(rs.getString("mot_de_passe"));
        c.setTelephone(rs.getString("telephone"));
        c.setActif(rs.getBoolean("actif"));
        c.setRole(rs.getString("role"));

        //champs de candidat
        c.setForfaitId(rs.getLong("forfait_id"));
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