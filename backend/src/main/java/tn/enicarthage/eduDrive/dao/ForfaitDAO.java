package tn.enicarthage.eduDrive.dao;

import tn.enicarthage.eduDrive.config.DatabaseConnection;
import tn.enicarthage.eduDrive.model.Forfait;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ForfaitDAO {

    //create forfait
    public void create(Forfait forfait) {
        String sql = "INSERT INTO forfait(nom, type_forfait, heures_code, heures_conduite, prix, actif) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, forfait.getNom());
            stmt.setString(2, forfait.getTypeForfait());
            stmt.setInt(3, forfait.getHeuresCode());
            stmt.setInt(4, forfait.getHeuresConduite());
            stmt.setBigDecimal(5, forfait.getPrix());
            stmt.setBoolean(6, forfait.isActif());

            stmt.executeUpdate();
            System.out.println("Forfait créé : " + forfait.getNom());

        } catch (SQLException e) {
            System.err.println("Erreur lors de la création du forfait");
            e.printStackTrace();
        }
    }

    // read all forfait
    public List<Forfait> findAll() {
        List<Forfait> forfaits = new ArrayList<>();
        String sql = "SELECT * FROM forfait";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                forfaits.add(mapResultSetToForfait(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return forfaits;
    }

    // read forfait with id
    public Forfait findById(Long id) {
        String sql = "SELECT * FROM forfait WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToForfait(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Pas trouvé
    }

    // update forfait
    public void update(Forfait forfait) {
        String sql = "UPDATE forfait SET nom = ?, type_forfait = ?, heures_code = ?, heures_conduite = ?, prix = ?, actif = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, forfait.getNom());
            stmt.setString(2, forfait.getTypeForfait());
            stmt.setInt(3, forfait.getHeuresCode());
            stmt.setInt(4, forfait.getHeuresConduite());
            stmt.setBigDecimal(5, forfait.getPrix());
            stmt.setBoolean(6, forfait.isActif());
            stmt.setLong(7, forfait.getId());

            stmt.executeUpdate();
            System.out.println("Forfait mis à jour : " + forfait.getId());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // delete forfait
    public void delete(Long id) {
        String sql = "DELETE FROM forfait WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
            System.out.println("Forfait supprimé : " + id);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Forfait mapResultSetToForfait(ResultSet rs) throws SQLException {
        Forfait f = new Forfait();
        f.setId(rs.getLong("id"));
        f.setNom(rs.getString("nom"));
        f.setTypeForfait(rs.getString("type_forfait"));
        f.setHeuresCode(rs.getInt("heures_code"));
        f.setHeuresConduite(rs.getInt("heures_conduite"));
        f.setPrix(rs.getBigDecimal("prix"));
        f.setActif(rs.getBoolean("actif"));
        return f;
    }
}