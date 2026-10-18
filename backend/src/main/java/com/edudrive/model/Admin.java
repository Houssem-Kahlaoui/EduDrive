package com.edudrive.model;

public class Admin extends Utilisateur {

    public Admin() {
        super();
    }

    public Admin(Long id, String nom, String prenom, String email, String motDePasse, String telephone, boolean actif) {
        super(id, nom, prenom, email, motDePasse, telephone, actif, "ADMIN");
    }

}