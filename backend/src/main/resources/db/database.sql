DROP DATABASE IF EXISTS edudrive_db;
CREATE DATABASE edudrive_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
USE edudrive_db;


CREATE TABLE forfait (
                         id              BIGINT AUTO_INCREMENT PRIMARY KEY,
                         nom             VARCHAR(100) NOT NULL,
                         type_forfait    ENUM('CODE_SEUL','CODE_CONDUITE','A_L_HEURE') NOT NULL,
                         heures_code     INT NOT NULL DEFAULT 0,
                         heures_conduite INT NOT NULL DEFAULT 0,
                         prix            DECIMAL(10,2) NOT NULL,
                         actif           BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE vehicule (
                          id              BIGINT AUTO_INCREMENT PRIMARY KEY,
                          immatriculation VARCHAR(30) NOT NULL UNIQUE,
                          marque          VARCHAR(50),
                          modele          VARCHAR(50),
                          etat            ENUM('DISPONIBLE','ENTRETIEN','PANNE') NOT NULL DEFAULT 'DISPONIBLE'
);

CREATE TABLE utilisateur (
                             id              BIGINT AUTO_INCREMENT PRIMARY KEY,
                             nom             VARCHAR(100) NOT NULL,
                             prenom          VARCHAR(100) NOT NULL,
                             email           VARCHAR(150) NOT NULL UNIQUE,
                             mot_de_passe    VARCHAR(255) NOT NULL,
                             telephone       VARCHAR(20),
                             actif           BOOLEAN NOT NULL DEFAULT TRUE,
                             role            ENUM('CANDIDAT','MONITEUR','ADMIN') NOT NULL
);

CREATE TABLE candidat (
                          id                      BIGINT PRIMARY KEY,
                          forfait_id              BIGINT,
                          date_naissance          DATE,
                          adresse                 VARCHAR(255),
                          type_permis             VARCHAR(10),
                          date_inscription        DATE,
                          statut_formation        ENUM('EN_COURS','PRET_EXAMEN','TERMINEE') NOT NULL DEFAULT 'EN_COURS',
                          date_validite_code      DATE,
                          date_souscription       DATE,
                          heures_code_restantes   INT NOT NULL DEFAULT 0,
                          heures_conduite_restantes INT NOT NULL DEFAULT 0,
                          dossier_complet         BOOLEAN NOT NULL DEFAULT FALSE,

                          CONSTRAINT fk_candidat_utilisateur FOREIGN KEY (id) REFERENCES utilisateur(id) ON DELETE CASCADE,
                          CONSTRAINT fk_candidat_forfait FOREIGN KEY (forfait_id) REFERENCES forfait(id) ON DELETE SET NULL
);

CREATE TABLE moniteur (
                          id              BIGINT PRIMARY KEY,
                          specialite      ENUM('CODE','CONDUITE','CODE_CONDUITE') NOT NULL,
                          CONSTRAINT fk_moniteur_utilisateur FOREIGN KEY (id) REFERENCES utilisateur(id) ON DELETE CASCADE
);

CREATE TABLE admin (
                       id              BIGINT PRIMARY KEY,
                       CONSTRAINT fk_admin_utilisateur FOREIGN KEY (id) REFERENCES utilisateur(id) ON DELETE CASCADE
);

CREATE TABLE seance (
                        id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
                        candidat_id             BIGINT NOT NULL,
                        moniteur_id             BIGINT NOT NULL,
                        vehicule_id             BIGINT NOT NULL,
                        date_seance             DATE NOT NULL,
                        heure_debut             TIME NOT NULL,
                        duree                   INT NOT NULL DEFAULT 60,
                        type_seance             ENUM('CODE','CONDUITE') NOT NULL,
                        statut                  ENUM('PLANIFIEE','CONFIRMEE','REFUSEE','TERMINEE','ANNULEE') NOT NULL DEFAULT 'PLANIFIEE',
                        confirme_par_candidat   BOOLEAN NOT NULL DEFAULT FALSE,
                        confirme_par_moniteur   BOOLEAN NOT NULL DEFAULT FALSE,
                        motif_refus             VARCHAR(255),
                        note                    INT,
                        commentaire             TEXT,

                        CONSTRAINT fk_seance_candidat FOREIGN KEY (candidat_id) REFERENCES candidat(id) ON DELETE CASCADE,
                        CONSTRAINT fk_seance_moniteur FOREIGN KEY (moniteur_id) REFERENCES moniteur(id) ON DELETE CASCADE,
                        CONSTRAINT fk_seance_vehicule FOREIGN KEY (vehicule_id) REFERENCES vehicule(id) ON DELETE CASCADE
);

CREATE TABLE examen (
                        id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
                        candidat_id         BIGINT NOT NULL,
                        type                ENUM('CODE','CIRCULATION','MANOEUVRES') NOT NULL,
                        date_examen         DATE NOT NULL,
                        heure               TIME,
                        resultat            ENUM('REUSSI','ECHOUE','EN_ATTENTE') NOT NULL DEFAULT 'EN_ATTENTE',
                        numero_tentative    INT NOT NULL DEFAULT 1,

                        CONSTRAINT fk_examen_candidat FOREIGN KEY (candidat_id) REFERENCES candidat(id) ON DELETE CASCADE
);

-- Index pour optimiser les recherches :
CREATE INDEX idx_candidat_statut ON candidat(statut_formation);
CREATE INDEX idx_seance_date ON seance(date_seance);

-- DONNÉES DE TEST :

INSERT INTO forfait (nom, type_forfait, heures_code, heures_conduite, prix, actif)
VALUES ('Permis B - Complet', 'CODE_CONDUITE', 12, 32, 1500.00, TRUE);

INSERT INTO vehicule (immatriculation, marque, modele, etat)
VALUES ('263 TU 4567', 'Peugeot', '208', 'DISPONIBLE');

INSERT INTO utilisateur (nom, prenom, email, mot_de_passe, telephone, role) VALUES
                                                                                ('Admin',    'Système',  'admin@edudrive.tn',     'admin123',    '71000000', 'ADMIN'),
                                                                                ('Bouzaine',  'Bechir',  'bechirbouzaine@edudrive.tn',   'mon123',      '20111222', 'MONITEUR'),
                                                                                ('Gharbi',   'Amine',    'amine@gmail.com',       'cand123',     '55666777', 'CANDIDAT');

INSERT INTO admin (id) VALUES (1);
INSERT INTO moniteur (id, specialite) VALUES (2, 'CONDUITE');
INSERT INTO candidat (id, forfait_id, date_naissance, statut_formation, heures_code_restantes, heures_conduite_restantes)
VALUES (3, 1, '2000-05-15', 'EN_COURS', 10, 10);

INSERT INTO seance (candidat_id, moniteur_id, vehicule_id, date_seance, heure_debut, type_seance, statut)
VALUES (3, 2, 1, '2026-10-15', '09:00:00', 'CONDUITE', 'PLANIFIEE');

INSERT INTO examen (candidat_id, type, date_examen, heure, resultat)
VALUES (3, 'CODE', '2026-10-30', '10:00:00', 'EN_ATTENTE');