package tn.enicarthage.eduDrive.dao;

import tn.enicarthage.eduDrive.config.DatabaseConnection;
import tn.enicarthage.eduDrive.model.Vehicule;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VehiculeDAO {

    //create Vehicule
    public void create(Vehicule vehicule) {
        String sql = "INSERT INTO vehicule(immatriculation, marque, modele, etat) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, vehicule.getImmatriculation());
            stmt.setString(2, vehicule.getMarque());
            stmt.setString(3, vehicule.getModele());
            stmt.setString(4, vehicule.getEtat());

            stmt.executeUpdate();
            System.out.println("Véhicule créé : " + vehicule.getImmatriculation());

        } catch (SQLException e) {
            System.err.println("Erreur lors de la création du véhicule");
            e.printStackTrace();
        }
    }

    //read All vehicules
    public List<Vehicule> findAll() {
        List<Vehicule> vehicules = new ArrayList<>();
        String sql = "SELECT * FROM vehicule";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                vehicules.add(mapResultSetToVehicule(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicules;
    }

    // read vehicule with id
    public Vehicule findById(Long id) {
        String sql = "SELECT * FROM vehicule WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToVehicule(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // update vehicule
    public void update(Vehicule vehicule) {
        String sql = "UPDATE vehicule SET immatriculation = ?, marque = ?, modele = ?, etat = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, vehicule.getImmatriculation());
            stmt.setString(2, vehicule.getMarque());
            stmt.setString(3, vehicule.getModele());
            stmt.setString(4, vehicule.getEtat());
            stmt.setLong(5, vehicule.getId());

            stmt.executeUpdate();
            System.out.println("Véhicule mis à jour : " + vehicule.getId());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // delete Vehicule with id
    public void delete(Long id) {
        String sql = "DELETE FROM vehicule WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
            System.out.println("Véhicule supprimé : " + id);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ORM manuelle ( objet Java <--> table SQL )
    private Vehicule mapResultSetToVehicule(ResultSet rs) throws SQLException {
        Vehicule v = new Vehicule();
        v.setId(rs.getLong("id"));
        v.setImmatriculation(rs.getString("immatriculation"));
        v.setMarque(rs.getString("marque"));
        v.setModele(rs.getString("modele"));
        v.setEtat(rs.getString("etat"));
        return v;
    }
}