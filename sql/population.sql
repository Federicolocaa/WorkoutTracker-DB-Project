USE WorkoutDB;

-- 1. UTENTI (Inseriamo un Coach e un Atleta supervisionato)
INSERT INTO UTENTI (email, password, dataNascita, nome, cognome, tipoUtente, certificazione, specializzazione)
VALUES ('coach@unibo.it', 'pass123', '1985-06-15', 'Mario', 'Rossi', 'Coach', 'Project Invictus', 'Powerlifting');

INSERT INTO UTENTI (email, password, dataNascita, nome, cognome, tipoUtente, altezza, pesoIniziale, obiettivo, supervisoreEmail)
VALUES ('federico@unibo.it', 'fedepass', '2001-09-20', 'Federico', 'Locatelli', 'Atleta', 180, 75.5, 'Ipertrofia', 'coach@unibo.it');

-- 2. MISURAZIONI (Una misurazione per Federico)
INSERT INTO MISURAZIONI (data, valorePeso, percentualeGrasso, utenteEmail)
VALUES ('2023-10-01', 75.5, 14.0, 'federico@unibo.it'),
       ('2023-11-01', 76.8, 13.5, 'federico@unibo.it');

-- 3. ATTREZZATURE
INSERT INTO ATTREZZATURE (nomeAttrezzo, marca)
VALUES ('Bilanciere Olimpionico', 'Eleiko'),
       ('Lat Machine', 'Technogym'),
       ('Manubri Componibili', 'Bowflex');

-- 4. GRUPPI MUSCOLARI
INSERT INTO GRUPPI_MUSCOLARI (nomeMuscolo)
VALUES ('Petto'), ('Dorso'), ('Gambe'), ('Spalle'), ('Tricipiti'), ('Bicipiti');

-- 5. ESERCIZI (Rispettando il vincolo: Guidato ha l'attrezzo, Libero no)
INSERT INTO ESERCIZI (nomeEsercizio, descrizione, tipoEsercizio, idAttrezzo)
VALUES 
    ('Panca Piana', 'Distensioni con bilanciere su panca piana', 'Libero', NULL),
    ('Lat Pulldown', 'Trazioni alla lat machine presa larga', 'Guidato', 2),
    ('Squat', 'Accosciata profonda con bilanciere', 'Libero', NULL),
    ('Spinte Manubri', 'Spinte su panca inclinata con manubri', 'Libero', NULL);

-- 6. COINVOLGIMENTI
INSERT INTO COINVOLGIMENTI (nomeEsercizio, nomeMuscolo, ruolo)
VALUES 
    ('Panca Piana', 'Petto', 'Primario'),
    ('Panca Piana', 'Tricipiti', 'Secondario'),
    ('Lat Pulldown', 'Dorso', 'Primario'),
    ('Squat', 'Gambe', 'Primario');

-- 7. VARIANTI
INSERT INTO VARIANTI (nomeEsercizioBase, nomeEsercizioSostituto)
VALUES ('Panca Piana', 'Spinte Manubri');

-- 8. SCHEDE E GIORNATE
INSERT INTO SCHEDE (nomeScheda, dataCreazione, utenteEmail)
VALUES ('Massa Invernale', '2023-10-01', 'federico@unibo.it');

-- (Assumiamo che la scheda inserita abbia ID 1)
INSERT INTO GIORNATE (nomeGiornata, idScheda)
VALUES ('Push Day', 1), ('Pull Day', 1), ('Leg Day', 1);

-- 9. PIANIFICAZIONI
-- (Assumiamo Push Day = ID 1, Pull Day = ID 2, Leg Day = ID 3)
INSERT INTO PIANIFICAZIONI (idGiornata, nomeEsercizio, ordine, serieTarget, repsTarget)
VALUES 
    (1, 'Panca Piana', 1, 4, 8),
    (2, 'Lat Pulldown', 1, 4, 10),
    (3, 'Squat', 1, 4, 6);

-- 10. SESSIONI (Una sessione completata)
INSERT INTO SESSIONI (data, durata, note, volumeTotale, idGiornata, utenteEmail)
VALUES ('2023-10-03', 65, 'Buone sensazioni sulla panca', 1920.00, 1, 'federico@unibo.it');

-- 11. SERIE (Per la Panca Piana nella sessione 1)
-- 1 Serie di riscaldamento (isWarmUp = TRUE, niente zavorra/failure)
INSERT INTO SERIE (ordineEsecuzione, caricoKG, reps, isWarmUp, noteWarmUp, idSessione, nomeEsercizio)
VALUES (1, 40.0, 15, TRUE, 'Spalla sinistra un po rigida', 1, 'Panca Piana');

-- 3 Serie allenanti (isWarmUp = FALSE)
INSERT INTO SERIE (ordineEsecuzione, caricoKG, reps, isWarmUp, isFailure, idSessione, nomeEsercizio)
VALUES 
    (2, 80.0, 8, FALSE, FALSE, 1, 'Panca Piana'),
    (3, 80.0, 8, FALSE, FALSE, 1, 'Panca Piana'),
    (4, 80.0, 8, FALSE, TRUE, 1, 'Panca Piana'); -- Cedimento all'ultima serie!

-- 12. MESSAGGI
INSERT INTO MESSAGGI (testo, timeStamp, autoreEmail, idSessione)
VALUES ('Ottimo lavoro sul cedimento, mantieni il carico.', CURRENT_TIMESTAMP, 'coach@unibo.it', 1);
