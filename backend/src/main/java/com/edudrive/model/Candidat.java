package com.edudrive.model;

import java.time.LocalDate;

public class Candidat extends Utilisateur {

    private Long forfaitId;
    private LocalDate dateNaissance;
    private String adresse;
    private String typePermis;
    private LocalDate dateInscription;
    private String statutFormation;
    private LocalDate dateValiditeCode;
    private LocalDate dateSouscription;
    private int heuresCodeRestantes;
    private int heuresConduiteRestantes;
    private boolean dossierComplet;

    public Candidat() {
        super();
    }

    public Candidat(Long id, String nom, String prenom, String email, String motDePasse, String telephone, boolean actif,
                    Long forfaitId, LocalDate dateNaissance, String adresse, String typePermis,
                    LocalDate dateInscription, String statutFormation, LocalDate dateValiditeCode,
                    LocalDate dateSouscription, int heuresCodeRestantes, int heuresConduiteRestantes, boolean dossierComplet) {

        super(id, nom, prenom, email, motDePasse, telephone, actif, "CANDIDAT");

        this.forfaitId = forfaitId;
        this.dateNaissance = dateNaissance;
        this.adresse = adresse;
        this.typePermis = typePermis;
        this.dateInscription = dateInscription;
        this.statutFormation = statutFormation;
        this.dateValiditeCode = dateValiditeCode;
        this.dateSouscription = dateSouscription;
        this.heuresCodeRestantes = heuresCodeRestantes;
        this.heuresConduiteRestantes = heuresConduiteRestantes;
        this.dossierComplet = dossierComplet;
    }


    public Long getForfaitId() { return forfaitId; }
    public void setForfaitId(Long forfaitId) { this.forfaitId = forfaitId; }

    public LocalDate getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(LocalDate dateNaissance) { this.dateNaissance = dateNaissance; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public String getTypePermis() { return typePermis; }
    public void setTypePermis(String typePermis) { this.typePermis = typePermis; }

    public LocalDate getDateInscription() { return dateInscription; }
    public void setDateInscription(LocalDate dateInscription) { this.dateInscription = dateInscription; }

    public String getStatutFormation() { return statutFormation; }
    public void setStatutFormation(String statutFormation) { this.statutFormation = statutFormation; }

    public LocalDate getDateValiditeCode() { return dateValiditeCode; }
    public void setDateValiditeCode(LocalDate dateValiditeCode) { this.dateValiditeCode = dateValiditeCode; }

    public LocalDate getDateSouscription() { return dateSouscription; }
    public void setDateSouscription(LocalDate dateSouscription) { this.dateSouscription = dateSouscription; }

    public int getHeuresCodeRestantes() { return heuresCodeRestantes; }
    public void setHeuresCodeRestantes(int heuresCodeRestantes) { this.heuresCodeRestantes = heuresCodeRestantes; }

    public int getHeuresConduiteRestantes() { return heuresConduiteRestantes; }
    public void setHeuresConduiteRestantes(int heuresConduiteRestantes) { this.heuresConduiteRestantes = heuresConduiteRestantes; }

    public boolean isDossierComplet() { return dossierComplet; }
    public void setDossierComplet(boolean dossierComplet) { this.dossierComplet = dossierComplet; }
}