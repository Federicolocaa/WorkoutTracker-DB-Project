-- Creazione e selezione del database
DROP DATABASE IF EXISTS WorkoutDB;
CREATE DATABASE WorkoutDB;
USE WorkoutDB;

-- 1. UTENTI
CREATE TABLE UTENTI (
    email VARCHAR(100) PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    dataNascita DATE NOT NULL,
    nome VARCHAR(50) NOT NULL,
    cognome VARCHAR(50) NOT NULL,
    tipoUtente ENUM('Atleta', 'Coach') NOT NULL,
    altezza INT DEFAULT NULL,               -- in cm (solo per Atleti)
    pesoIniziale DECIMAL(5,2) DEFAULT NULL, -- in kg (solo per Atleti)
    obiettivo VARCHAR(255) DEFAULT NULL,    -- (solo per Atleti)
    certificazione VARCHAR(255) DEFAULT NULL,   -- (solo per Coach)
    specializzazione VARCHAR(100) DEFAULT NULL, -- (solo per Coach)
    supervisoreEmail VARCHAR(100) DEFAULT NULL, -- FK Ricorsiva
    FOREIGN KEY (supervisoreEmail) REFERENCES UTENTI(email) 
        ON DELETE SET NULL ON UPDATE CASCADE
);

-- 2. MISURAZIONI
CREATE TABLE MISURAZIONI (
    idMisura INT AUTO_INCREMENT PRIMARY KEY,
    data DATE NOT NULL,
    valorePeso DECIMAL(5,2) NOT NULL,
    percentualeGrasso DECIMAL(4,2) DEFAULT NULL,
    fotoPercorso VARCHAR(255) DEFAULT NULL,
    utenteEmail VARCHAR(100) NOT NULL,
    FOREIGN KEY (utenteEmail) REFERENCES UTENTI(email) 
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- 3. ATTREZZATURE
CREATE TABLE ATTREZZATURE (
    idAttrezzo INT AUTO_INCREMENT PRIMARY KEY,
    nomeAttrezzo VARCHAR(100) NOT NULL,
    marca VARCHAR(100) DEFAULT NULL
);

-- 4. GRUPPI_MUSCOLARI
CREATE TABLE GRUPPI_MUSCOLARI (
    nomeMuscolo VARCHAR(50) PRIMARY KEY
);

-- 5. ESERCIZI
CREATE TABLE ESERCIZI (
    nomeEsercizio VARCHAR(100) PRIMARY KEY,
    descrizione TEXT NOT NULL,
    tipoEsercizio ENUM('Libero', 'Guidato') NOT NULL,
    idAttrezzo INT DEFAULT NULL,
    FOREIGN KEY (idAttrezzo) REFERENCES ATTREZZATURE(idAttrezzo) 
        ON DELETE SET NULL ON UPDATE CASCADE
);

-- 6. VARIANTI (Associazione N:M ricorsiva)
CREATE TABLE VARIANTI (
    nomeEsercizioBase VARCHAR(100),
    nomeEsercizioSostituto VARCHAR(100),
    PRIMARY KEY (nomeEsercizioBase, nomeEsercizioSostituto),
    FOREIGN KEY (nomeEsercizioBase) REFERENCES ESERCIZI(nomeEsercizio) ON DELETE CASCADE,
    FOREIGN KEY (nomeEsercizioSostituto) REFERENCES ESERCIZI(nomeEsercizio) ON DELETE CASCADE
);

-- 7. COINVOLGIMENTI (Associazione N:M)
CREATE TABLE COINVOLGIMENTI (
    nomeEsercizio VARCHAR(100),
    nomeMuscolo VARCHAR(50),
    ruolo VARCHAR(50) NOT NULL, -- Es: 'Primario', 'Secondario'
    PRIMARY KEY (nomeEsercizio, nomeMuscolo),
    FOREIGN KEY (nomeEsercizio) REFERENCES ESERCIZI(nomeEsercizio) ON DELETE CASCADE,
    FOREIGN KEY (nomeMuscolo) REFERENCES GRUPPI_MUSCOLARI(nomeMuscolo) ON DELETE CASCADE
);

-- 8. SCHEDE
CREATE TABLE SCHEDE (
    idScheda INT AUTO_INCREMENT PRIMARY KEY,
    nomeScheda VARCHAR(100) NOT NULL UNIQUE,
    dataCreazione DATE NOT NULL,
    utenteEmail VARCHAR(100) NOT NULL,
    FOREIGN KEY (utenteEmail) REFERENCES UTENTI(email) ON DELETE CASCADE
);

-- 9. GIORNATE
CREATE TABLE GIORNATE (
    idGiornata INT AUTO_INCREMENT PRIMARY KEY,
    giorno ENUM('Lunedi','Martedi','Mercoledi','Giovedi',
                'Venerdi','Sabato','Domenica') NOT NULL,
    tipo ENUM('Upper','Lower','Push','Pull',
              'Leg','Full Body','Torso','Limbs') NOT NULL,
    idScheda INT NOT NULL,
    UNIQUE(idScheda, giorno, tipo),
    FOREIGN KEY (idScheda) REFERENCES SCHEDE(idScheda) ON DELETE CASCADE
);

-- 10. PIANIFICAZIONI (Associazione N:M con attributi)
CREATE TABLE PIANIFICAZIONI (
    idGiornata INT,
    nomeEsercizio VARCHAR(100),
    ordine INT NOT NULL,
    serieTarget INT NOT NULL,
    repsTarget INT NOT NULL,
    PRIMARY KEY (idGiornata, nomeEsercizio),
    FOREIGN KEY (idGiornata) REFERENCES GIORNATE(idGiornata) ON DELETE CASCADE,
    FOREIGN KEY (nomeEsercizio) REFERENCES ESERCIZI(nomeEsercizio) ON DELETE CASCADE
);

-- 11. SESSIONI
CREATE TABLE SESSIONI (
    idSessione INT AUTO_INCREMENT PRIMARY KEY,
    data DATE NOT NULL,
    durata INT DEFAULT NULL, -- in minuti
    note TEXT DEFAULT NULL,
    volumeTotale DECIMAL(8,2) DEFAULT 0, -- Ridondanza calcolata
    idGiornata INT NOT NULL,
    utenteEmail VARCHAR(100) NOT NULL,
    FOREIGN KEY (idGiornata) REFERENCES GIORNATE(idGiornata) ON DELETE CASCADE,
    FOREIGN KEY (utenteEmail) REFERENCES UTENTI(email) ON DELETE CASCADE
);

-- 12. SERIE
CREATE TABLE SERIE (
    idSerie INT AUTO_INCREMENT PRIMARY KEY,
    ordineEsecuzione INT NOT NULL,
    caricoKG DECIMAL(5,2) NOT NULL,
    reps INT NOT NULL,
    isWarmUp BOOLEAN NOT NULL,
    zavorra DECIMAL(5,2) DEFAULT NULL,
    isFailure BOOLEAN DEFAULT NULL,
    noteWarmUp TEXT DEFAULT NULL,
    idSessione INT NOT NULL,
    nomeEsercizio VARCHAR(100) NOT NULL,
    FOREIGN KEY (idSessione) REFERENCES SESSIONI(idSessione) ON DELETE CASCADE,
    FOREIGN KEY (nomeEsercizio) REFERENCES ESERCIZI(nomeEsercizio) ON DELETE CASCADE
);

-- 13. MESSAGGI
CREATE TABLE MESSAGGI (
    idMessaggio INT AUTO_INCREMENT PRIMARY KEY,
    testo TEXT NOT NULL,
    timeStamp DATETIME NOT NULL,
    autoreEmail VARCHAR(100) NOT NULL,
    idSessione INT NOT NULL,
    FOREIGN KEY (autoreEmail) REFERENCES UTENTI(email) ON DELETE CASCADE,
    FOREIGN KEY (idSessione) REFERENCES SESSIONI(idSessione) ON DELETE CASCADE
);
