USE WorkoutDB;

-- ==============================================================================
-- UTENTI: 2 Coach + 5 Atleti
-- ==============================================================================

INSERT INTO UTENTI (email, password, dataNascita, nome, cognome,
                    tipoUtente, certificazione, specializzazione)
VALUES
    ('mario.rossi@unibo.it',    'coach123', '1980-03-15', 'Mario',  'Rossi',
     'Coach', 'ISSA Certified', 'Powerlifting'),
    ('giulia.ferrari@unibo.it', 'coach456', '1985-07-22', 'Giulia', 'Ferrari',
     'Coach', 'NASM Certified', 'Bodybuilding');

INSERT INTO UTENTI (email, password, dataNascita, nome, cognome,
                    tipoUtente, altezza, pesoIniziale, obiettivo, supervisoreEmail)
VALUES
    ('federico.locatelli@unibo.it', 'fede123', '2001-09-20',
     'Federico', 'Locatelli', 'Atleta', 181, 80.0,  'Forza massimale',
     'mario.rossi@unibo.it'),

    ('luca.bianchi@unibo.it',       'luca456', '1999-04-10',
     'Luca',     'Bianchi',   'Atleta', 178, 75.5,  'Ipertrofia',
     'mario.rossi@unibo.it'),

    ('sara.verde@unibo.it',         'sara789', '2000-11-05',
     'Sara',     'Verde',     'Atleta', 165, 58.0,  'Dimagrimento',
     'giulia.ferrari@unibo.it'),

    ('marco.neri@unibo.it',         'marco321', '1998-06-18',
     'Marco',    'Neri',      'Atleta', 175, 82.0,  'Atletismo generale',
     'giulia.ferrari@unibo.it'),

    ('anna.blu@unibo.it',           'anna654', '2002-02-14',
     'Anna',     'Blu',       'Atleta', 162, 55.0,  'Tonificazione',
     'mario.rossi@unibo.it');

-- ==============================================================================
-- ATTREZZATURE
-- ==============================================================================

INSERT INTO ATTREZZATURE (nomeAttrezzo, marca) VALUES
    ('Bilanciere Olimpionico', 'Eleiko'),      -- id 1
    ('Lat Machine',            'Technogym'),   -- id 2
    ('Manubri Componibili',    'Bowflex'),     -- id 3
    ('Cavo Basso / Alto',      'Technogym'),   -- id 4
    ('Leg Press',              'Panatta'),     -- id 5
    ('Chest Press Guidata',    'Panatta');     -- id 6
INSERT INTO ATTREZZATURE (nomeAttrezzo, marca) VALUES
    ('Macchina Leg Curl/Ext', 'Panatta');      -- id 7

-- ==============================================================================
-- GRUPPI MUSCOLARI
-- ==============================================================================

INSERT INTO GRUPPI_MUSCOLARI (nomeMuscolo) VALUES
    ('Petto'), ('Dorso'), ('Gambe'), ('Spalle'),
    ('Tricipiti'), ('Bicipiti'), ('Glutei'), ('Core'), ('Trapezio');

-- ==============================================================================
-- ESERCIZI
-- ==============================================================================

INSERT INTO ESERCIZI (nomeEsercizio, descrizione, tipoEsercizio, idAttrezzo) VALUES
    ('Panca Piana',
     'Distensioni su panca piana con bilanciere olimpionico.',
     'Libero', NULL),

    ('Squat',
     'Accosciata profonda con bilanciere in posizione alta.',
     'Libero', NULL),

    ('Stacco da Terra',
     'Sollevamento da terra con bilanciere, schiena neutra.',
     'Libero', NULL),

    ('Military Press',
     'Spinta verticale sopra la testa con bilanciere.',
     'Libero', NULL),

    ('Rematore con Bilanciere',
     'Rowing bent-over con bilanciere, busto inclinato.',
     'Libero', NULL),

    ('Curl con Bilanciere',
     'Flessione del bicipite con bilanciere dritto.',
     'Libero', NULL),

    ('Romanian Deadlift',
     'Stacco rumeno per l\'enfasi su glutei e femorali.',
     'Libero', NULL),

    ('Spinte con Manubri',
     'Distensioni su panca inclinata con manubri componibili.',
     'Libero', NULL),

    ('Croci con Manubri',
     'Aperture orizzontali per isolamento del petto.',
     'Libero', NULL),

    ('Lat Pulldown',
     'Trazioni alla lat machine con presa larga.',
     'Guidato', 2),

    ('Cable Crossover',
     'Croci ai cavi per isolamento del petto.',
     'Guidato', 4),

    ('Tricep Pushdown',
     'Estensioni tricipite al cavo alto con corda.',
     'Guidato', 4),

    ('Leg Press',
     'Pressa orizzontale per quadricipiti e glutei.',
     'Guidato', 5),

    ('Chest Press Guidata',
     'Distensioni guidate su macchina Panatta.',
     'Guidato', 6);

INSERT INTO ESERCIZI (nomeEsercizio, descrizione, tipoEsercizio, idAttrezzo) VALUES
     ('Dips',
     'Piegamenti alle parallele per petto basso e tricipiti.',
     'Libero', NULL),

    ('Alzate Laterali',
     'Alzate laterali con manubri per i deltoidi.',
     'Libero', 3),

    ('Trazioni',
     'Trazioni alla sbarra a corpo libero, presa prona.',
     'Libero', NULL),

    ('Pulley Basso',
     'Rematore al pulley basso con cavo.',
     'Guidato', 4),

    ('Face Pull',
     'Tirata al volto al cavo alto per deltoidi posteriori.',
     'Guidato', 4),

    ('Curl con Manubri',
     'Curl alternato con manubri per i bicipiti.',
     'Libero', 3),

    ('Affondi',
     'Affondi in avanti con manubri o bilanciere.',
     'Libero', NULL),

    ('Leg Curl',
     'Flessione delle gambe alla macchina, femorali.',
     'Guidato', 7),

    ('Leg Extension',
     'Estensione delle gambe alla macchina, quadricipiti.',
     'Guidato', 7),

    ('Calf Raise',
     'Sollevamento sui polpacci in piedi.',
     'Libero', NULL),

    ('Hip Thrust',
     'Spinta dell\'anca con bilanciere per i glutei.',
     'Libero', 1);

-- ==============================================================================
-- COINVOLGIMENTI
-- ==============================================================================

INSERT INTO COINVOLGIMENTI (nomeEsercizio, nomeMuscolo, ruolo) VALUES
    ('Panca Piana',            'Petto',      'Primario'),
    ('Panca Piana',            'Tricipiti',  'Secondario'),
    ('Panca Piana',            'Spalle',     'Secondario'),

    ('Squat',                  'Gambe',      'Primario'),
    ('Squat',                  'Glutei',     'Secondario'),
    ('Squat',                  'Core',       'Secondario'),

    ('Stacco da Terra',        'Dorso',      'Primario'),
    ('Stacco da Terra',        'Glutei',     'Primario'),
    ('Stacco da Terra',        'Gambe',      'Secondario'),
    ('Stacco da Terra',        'Trapezio',   'Secondario'),

    ('Military Press',         'Spalle',     'Primario'),
    ('Military Press',         'Tricipiti',  'Secondario'),

    ('Rematore con Bilanciere','Dorso',      'Primario'),
    ('Rematore con Bilanciere','Bicipiti',   'Secondario'),
    ('Rematore con Bilanciere','Trapezio',   'Secondario'),

    ('Curl con Bilanciere',    'Bicipiti',   'Primario'),

    ('Romanian Deadlift',      'Gambe',      'Primario'),
    ('Romanian Deadlift',      'Glutei',     'Primario'),

    ('Spinte con Manubri',     'Petto',      'Primario'),
    ('Spinte con Manubri',     'Spalle',     'Secondario'),

    ('Croci con Manubri',      'Petto',      'Primario'),

    ('Lat Pulldown',           'Dorso',      'Primario'),
    ('Lat Pulldown',           'Bicipiti',   'Secondario'),

    ('Cable Crossover',        'Petto',      'Primario'),

    ('Tricep Pushdown',        'Tricipiti',  'Primario'),

    ('Leg Press',              'Gambe',      'Primario'),
    ('Leg Press',              'Glutei',     'Secondario'),

    ('Chest Press Guidata',    'Petto',      'Primario'),
    ('Chest Press Guidata',    'Tricipiti',  'Secondario');

INSERT INTO COINVOLGIMENTI (nomeEsercizio, nomeMuscolo, ruolo) VALUES

    ('Dips',             'Petto',      'Primario'),
    ('Dips',             'Tricipiti',  'Primario'),

    ('Alzate Laterali',  'Spalle',     'Primario'),

    ('Trazioni',         'Dorso',      'Primario'),
    ('Trazioni',         'Bicipiti',   'Secondario'),

    ('Pulley Basso',     'Dorso',      'Primario'),
    ('Pulley Basso',     'Bicipiti',   'Secondario'),

    ('Face Pull',        'Spalle',     'Primario'),
    ('Face Pull',        'Trapezio',   'Secondario'),

    ('Curl con Manubri', 'Bicipiti',   'Primario'),

    ('Affondi',          'Gambe',      'Primario'),
    ('Affondi',          'Glutei',     'Secondario'),

    ('Leg Curl',         'Gambe',      'Primario'),

    ('Leg Extension',    'Gambe',      'Primario'),

    ('Calf Raise',       'Gambe',      'Primario'),

    ('Hip Thrust',       'Glutei',     'Primario'),
    ('Hip Thrust',       'Gambe',      'Secondario');

-- ==============================================================================
-- VARIANTI
-- ==============================================================================

INSERT INTO VARIANTI (nomeEsercizioBase, nomeEsercizioSostituto) VALUES
    ('Panca Piana',     'Spinte con Manubri'),
    ('Panca Piana',     'Chest Press Guidata'),
    ('Panca Piana',     'Cable Crossover'),
    ('Squat',           'Leg Press'),
    ('Lat Pulldown',    'Rematore con Bilanciere'),
    ('Cable Crossover', 'Croci con Manubri'),
    ('Stacco da Terra', 'Romanian Deadlift');

-- ==============================================================================
-- SCHEDE (1 per atleta)
-- ==============================================================================

INSERT INTO SCHEDE (nomeScheda, dataCreazione, utenteEmail) VALUES
    ('Powerlifting Base',   DATE_SUB(CURRENT_DATE, INTERVAL 60 DAY),
     'federico.locatelli@unibo.it'),   -- id 1

    ('Massa Invernale',     DATE_SUB(CURRENT_DATE, INTERVAL 45 DAY),
     'luca.bianchi@unibo.it'),          -- id 2

    ('Fitness Completo',    DATE_SUB(CURRENT_DATE, INTERVAL 30 DAY),
     'sara.verde@unibo.it'),            -- id 3

    ('Forza Funzionale',    DATE_SUB(CURRENT_DATE, INTERVAL 50 DAY),
     'marco.neri@unibo.it'),            -- id 4

    ('Ipertrofia',          DATE_SUB(CURRENT_DATE, INTERVAL 40 DAY),
     'anna.blu@unibo.it');              -- id 5

-- ==============================================================================
-- GIORNATE (giorno + tipo al posto di nomeGiornata)
-- ==============================================================================

-- Federico (scheda 1 — Powerlifting Base)
INSERT INTO GIORNATE (giorno, tipo, idScheda) VALUES
    ('Lunedi',    'Lower',      1),  -- id 1 (Squat)
    ('Mercoledi', 'Push',       1),  -- id 2 (Panca)
    ('Venerdi',   'Full Body',  1);  -- id 3 (Stacco)

-- Luca (scheda 2 — Massa Invernale)
INSERT INTO GIORNATE (giorno, tipo, idScheda) VALUES
    ('Lunedi',    'Push',  2),  -- id 4
    ('Mercoledi', 'Pull',  2),  -- id 5
    ('Sabato',    'Leg',   2);  -- id 6

-- Sara (scheda 3 — Fitness Completo)
INSERT INTO GIORNATE (giorno, tipo, idScheda) VALUES
    ('Martedi',  'Upper',  3),  -- id 7
    ('Giovedi',  'Upper',  3),  -- id 8
    ('Sabato',   'Lower',  3);  -- id 9

-- Marco (scheda 4 — Forza Funzionale)
INSERT INTO GIORNATE (giorno, tipo, idScheda) VALUES
    ('Lunedi',   'Full Body',  4),  -- id 10
    ('Giovedi',  'Full Body',  4);  -- id 11

-- Anna (scheda 5 — Ipertrofia)
INSERT INTO GIORNATE (giorno, tipo, idScheda) VALUES
    ('Lunedi',    'Push',  5),  -- id 12
    ('Mercoledi', 'Pull',  5),  -- id 13
    ('Venerdi',   'Leg',   5);  -- id 14

-- ==============================================================================
-- PIANIFICAZIONI
-- ==============================================================================

-- Federico - Giorno A (Squat)
INSERT INTO PIANIFICAZIONI VALUES (1, 'Squat',         1, 5, 5);
INSERT INTO PIANIFICAZIONI VALUES (1, 'Romanian Deadlift', 2, 3, 8);

-- Federico - Giorno B (Panca)
INSERT INTO PIANIFICAZIONI VALUES (2, 'Panca Piana',   1, 5, 5);
INSERT INTO PIANIFICAZIONI VALUES (2, 'Military Press', 2, 4, 8);

-- Federico - Giorno C (Stacco)
INSERT INTO PIANIFICAZIONI VALUES (3, 'Stacco da Terra', 1, 5, 3);
INSERT INTO PIANIFICAZIONI VALUES (3, 'Rematore con Bilanciere', 2, 4, 8);

-- Luca - Push Day
INSERT INTO PIANIFICAZIONI VALUES (4, 'Panca Piana',       1, 4, 8);
INSERT INTO PIANIFICAZIONI VALUES (4, 'Spinte con Manubri',2, 3, 10);
INSERT INTO PIANIFICAZIONI VALUES (4, 'Military Press',    3, 3, 10);
INSERT INTO PIANIFICAZIONI VALUES (4, 'Tricep Pushdown',   4, 3, 12);

-- Luca - Pull Day
INSERT INTO PIANIFICAZIONI VALUES (5, 'Lat Pulldown',             1, 4, 10);
INSERT INTO PIANIFICAZIONI VALUES (5, 'Rematore con Bilanciere',  2, 4, 8);
INSERT INTO PIANIFICAZIONI VALUES (5, 'Curl con Bilanciere',      3, 3, 12);

-- Luca - Leg Day
INSERT INTO PIANIFICAZIONI VALUES (6, 'Squat',           1, 4, 8);
INSERT INTO PIANIFICAZIONI VALUES (6, 'Leg Press',        2, 3, 12);
INSERT INTO PIANIFICAZIONI VALUES (6, 'Romanian Deadlift',3, 3, 10);

-- Sara - Upper A
INSERT INTO PIANIFICAZIONI VALUES (7, 'Chest Press Guidata', 1, 3, 12);
INSERT INTO PIANIFICAZIONI VALUES (7, 'Lat Pulldown',         2, 3, 12);
INSERT INTO PIANIFICAZIONI VALUES (7, 'Cable Crossover',      3, 3, 15);

-- Sara - Upper B
INSERT INTO PIANIFICAZIONI VALUES (8, 'Military Press',   1, 3, 12);
INSERT INTO PIANIFICAZIONI VALUES (8, 'Rematore con Bilanciere', 2, 3, 12);
INSERT INTO PIANIFICAZIONI VALUES (8, 'Curl con Bilanciere', 3, 3, 15);

-- Sara - Lower
INSERT INTO PIANIFICAZIONI VALUES (9, 'Leg Press',        1, 4, 15);
INSERT INTO PIANIFICAZIONI VALUES (9, 'Romanian Deadlift',2, 3, 12);

-- Marco - Full Body A
INSERT INTO PIANIFICAZIONI VALUES (10, 'Squat',        1, 4, 6);
INSERT INTO PIANIFICAZIONI VALUES (10, 'Panca Piana',  2, 4, 6);
INSERT INTO PIANIFICAZIONI VALUES (10, 'Stacco da Terra', 3, 3, 5);

-- Marco - Full Body B
INSERT INTO PIANIFICAZIONI VALUES (11, 'Military Press',  1, 3, 8);
INSERT INTO PIANIFICAZIONI VALUES (11, 'Lat Pulldown',    2, 3, 10);
INSERT INTO PIANIFICAZIONI VALUES (11, 'Leg Press',       3, 3, 10);

-- Anna - Chest & Triceps
INSERT INTO PIANIFICAZIONI VALUES (12, 'Chest Press Guidata', 1, 3, 12);
INSERT INTO PIANIFICAZIONI VALUES (12, 'Cable Crossover',     2, 3, 15);
INSERT INTO PIANIFICAZIONI VALUES (12, 'Tricep Pushdown',     3, 3, 15);

-- Anna - Back & Biceps
INSERT INTO PIANIFICAZIONI VALUES (13, 'Lat Pulldown',    1, 3, 12);
INSERT INTO PIANIFICAZIONI VALUES (13, 'Curl con Bilanciere', 2, 3, 15);

-- Anna - Legs & Shoulders
INSERT INTO PIANIFICAZIONI VALUES (14, 'Leg Press',      1, 4, 15);
INSERT INTO PIANIFICAZIONI VALUES (14, 'Military Press', 2, 3, 12);

-- ==============================================================================
-- SESSIONI (volumeTotale = 0, il trigger lo aggiorna)
-- ==============================================================================

INSERT INTO SESSIONI (data, durata, note, volumeTotale, idGiornata, utenteEmail) VALUES
-- Federico
(DATE_SUB(CURRENT_DATE, INTERVAL 20 DAY), 70, 'Buone sensazioni', 0, 1,
 'federico.locatelli@unibo.it'),   -- id 1: Squat

(DATE_SUB(CURRENT_DATE, INTERVAL 15 DAY), 65, 'Panca solida', 0, 2,
 'federico.locatelli@unibo.it'),   -- id 2: Panca

(DATE_SUB(CURRENT_DATE, INTERVAL 10 DAY), 75, 'Stacco pesante', 0, 3,
 'federico.locatelli@unibo.it'),   -- id 3: Stacco

(DATE_SUB(CURRENT_DATE, INTERVAL 5 DAY),  72, 'Nuovo PR squat!', 0, 1,
 'federico.locatelli@unibo.it'),   -- id 4: Squat (PR attempt)

(DATE_SUB(CURRENT_DATE, INTERVAL 2 DAY),  68, 'Nuovo PR panca!', 0, 2,
 'federico.locatelli@unibo.it'),   -- id 5: Panca (PR attempt)

-- Luca
(DATE_SUB(CURRENT_DATE, INTERVAL 18 DAY), 60, NULL, 0, 4,
 'luca.bianchi@unibo.it'),         -- id 6: Push

(DATE_SUB(CURRENT_DATE, INTERVAL 13 DAY), 55, NULL, 0, 5,
 'luca.bianchi@unibo.it'),         -- id 7: Pull

(DATE_SUB(CURRENT_DATE, INTERVAL 8 DAY),  65, 'Gambe distrutte', 0, 6,
 'luca.bianchi@unibo.it'),         -- id 8: Legs

(DATE_SUB(CURRENT_DATE, INTERVAL 3 DAY),  62, NULL, 0, 4,
 'luca.bianchi@unibo.it'),         -- id 9: Push

-- Sara
(DATE_SUB(CURRENT_DATE, INTERVAL 14 DAY), 50, NULL, 0, 7,
 'sara.verde@unibo.it'),           -- id 10: Upper A

(DATE_SUB(CURRENT_DATE, INTERVAL 9 DAY),  45, NULL, 0, 9,
 'sara.verde@unibo.it'),           -- id 11: Lower

(DATE_SUB(CURRENT_DATE, INTERVAL 4 DAY),  48, NULL, 0, 8,
 'sara.verde@unibo.it'),           -- id 12: Upper B

-- Marco
(DATE_SUB(CURRENT_DATE, INTERVAL 16 DAY), 55, NULL, 0, 10,
 'marco.neri@unibo.it'),           -- id 13: Full Body A

(DATE_SUB(CURRENT_DATE, INTERVAL 9 DAY),  50, NULL, 0, 11,
 'marco.neri@unibo.it'),           -- id 14: Full Body B

-- Anna
(DATE_SUB(CURRENT_DATE, INTERVAL 12 DAY), 45, NULL, 0, 12,
 'anna.blu@unibo.it'),             -- id 15: Chest & Tri

(DATE_SUB(CURRENT_DATE, INTERVAL 5 DAY),  42, NULL, 0, 13,
 'anna.blu@unibo.it');             -- id 16: Back & Bi

-- ==============================================================================
-- MISURAZIONI
-- ==============================================================================

INSERT INTO MISURAZIONI (data, valorePeso, percentualeGrasso, utenteEmail) VALUES
(DATE_SUB(CURRENT_DATE, INTERVAL 30 DAY), 80.0, 15.5,
 'federico.locatelli@unibo.it'),
(DATE_SUB(CURRENT_DATE, INTERVAL 15 DAY), 80.5, 15.0,
 'federico.locatelli@unibo.it'),
(CURRENT_DATE,                             81.0, 14.5,
 'federico.locatelli@unibo.it'),

(DATE_SUB(CURRENT_DATE, INTERVAL 20 DAY), 75.5, 13.0,
 'luca.bianchi@unibo.it'),
(DATE_SUB(CURRENT_DATE, INTERVAL 5 DAY),  76.0, 12.5,
 'luca.bianchi@unibo.it'),

(DATE_SUB(CURRENT_DATE, INTERVAL 14 DAY), 58.0, 22.0,
 'sara.verde@unibo.it'),
(DATE_SUB(CURRENT_DATE, INTERVAL 4 DAY),  57.2, 21.5,
 'sara.verde@unibo.it'),

(DATE_SUB(CURRENT_DATE, INTERVAL 16 DAY), 82.0, 17.0,
 'marco.neri@unibo.it'),

(DATE_SUB(CURRENT_DATE, INTERVAL 12 DAY), 55.0, 24.0,
 'anna.blu@unibo.it'),
(DATE_SUB(CURRENT_DATE, INTERVAL 5 DAY),  54.5, 23.5,
 'anna.blu@unibo.it');

-- ==============================================================================
-- SERIE — Sessione 1: Federico - Squat (20 gg fa)
-- ==============================================================================
INSERT INTO SERIE (ordineEsecuzione, caricoKG, reps, isWarmUp,
                   noteWarmUp, idSessione, nomeEsercizio) VALUES
(1, 60.0, 10, TRUE, 'Riscaldamento leggero', 1, 'Squat');

INSERT INTO SERIE (ordineEsecuzione, caricoKG, reps, isWarmUp,
                   isFailure, idSessione, nomeEsercizio) VALUES
(2, 100.0, 5, FALSE, FALSE, 1, 'Squat'),
(3, 110.0, 5, FALSE, FALSE, 1, 'Squat'),
(4, 120.0, 5, FALSE, FALSE, 1, 'Squat'),
(5, 125.0, 5, FALSE, FALSE, 1, 'Squat'),
(6, 130.0, 5, FALSE, TRUE,  1, 'Squat');

-- ==============================================================================
-- SERIE — Sessione 2: Federico - Panca (15 gg fa)
-- ==============================================================================
INSERT INTO SERIE (ordineEsecuzione, caricoKG, reps, isWarmUp,
                   noteWarmUp, idSessione, nomeEsercizio) VALUES
(1, 60.0, 10, TRUE, 'Spalle ok', 2, 'Panca Piana');

INSERT INTO SERIE (ordineEsecuzione, caricoKG, reps, isWarmUp,
                   isFailure, idSessione, nomeEsercizio) VALUES
(2,  90.0, 5, FALSE, FALSE, 2, 'Panca Piana'),
(3, 100.0, 5, FALSE, FALSE, 2, 'Panca Piana'),
(4, 105.0, 5, FALSE, FALSE, 2, 'Panca Piana'),
(5, 110.0, 5, FALSE, TRUE,  2, 'Panca Piana');

-- ==============================================================================
-- SERIE — Sessione 3: Federico - Stacco (10 gg fa)
-- ==============================================================================
INSERT INTO SERIE (ordineEsecuzione, caricoKG, reps, isWarmUp,
                   noteWarmUp, idSessione, nomeEsercizio) VALUES
(1, 80.0, 5, TRUE, 'Schiena scaldata', 3, 'Stacco da Terra');

INSERT INTO SERIE (ordineEsecuzione, caricoKG, reps, isWarmUp,
                   isFailure, idSessione, nomeEsercizio) VALUES
(2, 140.0, 3, FALSE, FALSE, 3, 'Stacco da Terra'),
(3, 150.0, 3, FALSE, FALSE, 3, 'Stacco da Terra'),
(4, 160.0, 3, FALSE, FALSE, 3, 'Stacco da Terra'),
(5, 170.0, 2, FALSE, TRUE,  3, 'Stacco da Terra');

-- ==============================================================================
-- SERIE — Sessione 4: Federico - Squat PR (5 gg fa)
-- ==============================================================================
INSERT INTO SERIE (ordineEsecuzione, caricoKG, reps, isWarmUp,
                   noteWarmUp, idSessione, nomeEsercizio) VALUES
(1, 60.0, 10, TRUE, 'Ottimo riscaldamento', 4, 'Squat');

INSERT INTO SERIE (ordineEsecuzione, caricoKG, reps, isWarmUp,
                   isFailure, idSessione, nomeEsercizio) VALUES
(2, 120.0, 5, FALSE, FALSE, 4, 'Squat'),
(3, 130.0, 5, FALSE, FALSE, 4, 'Squat'),
(4, 140.0, 5, FALSE, FALSE, 4, 'Squat'),
(5, 145.0, 5, FALSE, FALSE, 4, 'Squat'),
(6, 150.0, 3, FALSE, TRUE,  4, 'Squat');  -- NUOVO PR: 150kg!

-- ==============================================================================
-- SERIE — Sessione 5: Federico - Panca PR (2 gg fa)
-- ==============================================================================
INSERT INTO SERIE (ordineEsecuzione, caricoKG, reps, isWarmUp,
                   noteWarmUp, idSessione, nomeEsercizio) VALUES
(1, 60.0, 10, TRUE, NULL, 5, 'Panca Piana');

INSERT INTO SERIE (ordineEsecuzione, caricoKG, reps, isWarmUp,
                   isFailure, idSessione, nomeEsercizio) VALUES
(2, 100.0, 5, FALSE, FALSE, 5, 'Panca Piana'),
(3, 110.0, 5, FALSE, FALSE, 5, 'Panca Piana'),
(4, 115.0, 5, FALSE, FALSE, 5, 'Panca Piana'),
(5, 120.0, 3, FALSE, TRUE,  5, 'Panca Piana');  -- NUOVO PR: 120kg!

-- ==============================================================================
-- SERIE — Sessione 6: Luca - Push (18 gg fa)
-- ==============================================================================
INSERT INTO SERIE (ordineEsecuzione, caricoKG, reps, isWarmUp,
                   noteWarmUp, idSessione, nomeEsercizio) VALUES
(1, 50.0, 12, TRUE, NULL, 6, 'Panca Piana');

INSERT INTO SERIE (ordineEsecuzione, caricoKG, reps, isWarmUp,
                   isFailure, idSessione, nomeEsercizio) VALUES
(2, 75.0, 8, FALSE, FALSE, 6, 'Panca Piana'),
(3, 77.5, 8, FALSE, FALSE, 6, 'Panca Piana'),
(4, 80.0, 8, FALSE, FALSE, 6, 'Panca Piana'),
(5, 80.0, 7, FALSE, TRUE,  6, 'Panca Piana'),
(6, 30.0, 10, FALSE, FALSE, 6, 'Military Press'),
(7, 35.0, 10, FALSE, FALSE, 6, 'Military Press'),
(8, 35.0, 10, FALSE, FALSE, 6, 'Military Press');

-- ==============================================================================
-- SERIE — Sessione 7: Luca - Pull (13 gg fa)
-- ==============================================================================
INSERT INTO SERIE (ordineEsecuzione, caricoKG, reps, isWarmUp,
                   noteWarmUp, idSessione, nomeEsercizio) VALUES
(1, 30.0, 15, TRUE, NULL, 7, 'Lat Pulldown');

INSERT INTO SERIE (ordineEsecuzione, caricoKG, reps, isWarmUp,
                   isFailure, idSessione, nomeEsercizio) VALUES
(2, 55.0, 10, FALSE, FALSE, 7, 'Lat Pulldown'),
(3, 60.0, 10, FALSE, FALSE, 7, 'Lat Pulldown'),
(4, 62.5, 10, FALSE, FALSE, 7, 'Lat Pulldown'),
(5, 65.0, 8,  FALSE, TRUE,  7, 'Lat Pulldown'),
(6, 40.0, 12, FALSE, FALSE, 7, 'Curl con Bilanciere'),
(7, 40.0, 12, FALSE, FALSE, 7, 'Curl con Bilanciere'),
(8, 42.5, 10, FALSE, TRUE,  7, 'Curl con Bilanciere');

-- ==============================================================================
-- SERIE — Sessione 8: Luca - Legs (8 gg fa)
-- ==============================================================================
INSERT INTO SERIE (ordineEsecuzione, caricoKG, reps, isWarmUp,
                   noteWarmUp, idSessione, nomeEsercizio) VALUES
(1, 40.0, 15, TRUE, NULL, 8, 'Squat');

INSERT INTO SERIE (ordineEsecuzione, caricoKG, reps, isWarmUp,
                   isFailure, idSessione, nomeEsercizio) VALUES
(2, 80.0, 8, FALSE, FALSE, 8, 'Squat'),
(3, 85.0, 8, FALSE, FALSE, 8, 'Squat'),
(4, 87.5, 8, FALSE, FALSE, 8, 'Squat'),
(5, 90.0, 6, FALSE, TRUE,  8, 'Squat'),
(6, 100.0, 12, FALSE, FALSE, 8, 'Leg Press'),
(7, 110.0, 12, FALSE, FALSE, 8, 'Leg Press'),
(8, 120.0, 10, FALSE, FALSE, 8, 'Leg Press');

-- ==============================================================================
-- SERIE — Sessione 9: Luca - Push (3 gg fa)
-- ==============================================================================
INSERT INTO SERIE (ordineEsecuzione, caricoKG, reps, isWarmUp,
                   noteWarmUp, idSessione, nomeEsercizio) VALUES
(1, 50.0, 12, TRUE, NULL, 9, 'Panca Piana');

INSERT INTO SERIE (ordineEsecuzione, caricoKG, reps, isWarmUp,
                   isFailure, idSessione, nomeEsercizio) VALUES
(2, 80.0, 8, FALSE, FALSE, 9, 'Panca Piana'),
(3, 82.5, 8, FALSE, FALSE, 9, 'Panca Piana'),
(4, 82.5, 8, FALSE, FALSE, 9, 'Panca Piana'),
(5, 85.0, 6, FALSE, TRUE,  9, 'Panca Piana');  -- PR Luca: 85kg

-- ==============================================================================
-- SERIE — Sessione 10: Sara - Upper A (14 gg fa)
-- ==============================================================================
INSERT INTO SERIE (ordineEsecuzione, caricoKG, reps, isWarmUp,
                   isFailure, idSessione, nomeEsercizio) VALUES
(1, 30.0, 12, FALSE, FALSE, 10, 'Chest Press Guidata'),
(2, 32.5, 12, FALSE, FALSE, 10, 'Chest Press Guidata'),
(3, 35.0, 10, FALSE, TRUE,  10, 'Chest Press Guidata'),
(4, 30.0, 12, FALSE, FALSE, 10, 'Lat Pulldown'),
(5, 32.5, 12, FALSE, FALSE, 10, 'Lat Pulldown'),
(6, 35.0, 10, FALSE, TRUE,  10, 'Lat Pulldown');

-- ==============================================================================
-- SERIE — Sessione 11: Sara - Lower (9 gg fa)
-- ==============================================================================
INSERT INTO SERIE (ordineEsecuzione, caricoKG, reps, isWarmUp,
                   isFailure, idSessione, nomeEsercizio) VALUES
(1, 60.0,  15, FALSE, FALSE, 11, 'Leg Press'),
(2, 70.0,  15, FALSE, FALSE, 11, 'Leg Press'),
(3, 80.0,  12, FALSE, FALSE, 11, 'Leg Press'),
(4, 85.0,  10, FALSE, TRUE,  11, 'Leg Press'),
(5, 30.0,  12, FALSE, FALSE, 11, 'Romanian Deadlift'),
(6, 35.0,  12, FALSE, FALSE, 11, 'Romanian Deadlift'),
(7, 37.5,  10, FALSE, TRUE,  11, 'Romanian Deadlift');

-- ==============================================================================
-- SERIE — Sessione 12: Sara - Upper B (4 gg fa)
-- ==============================================================================
INSERT INTO SERIE (ordineEsecuzione, caricoKG, reps, isWarmUp,
                   isFailure, idSessione, nomeEsercizio) VALUES
(1, 20.0, 12, FALSE, FALSE, 12, 'Military Press'),
(2, 22.5, 12, FALSE, FALSE, 12, 'Military Press'),
(3, 25.0, 10, FALSE, TRUE,  12, 'Military Press'),
(4, 30.0, 12, FALSE, FALSE, 12, 'Curl con Bilanciere'),
(5, 32.5, 12, FALSE, FALSE, 12, 'Curl con Bilanciere'),
(6, 32.5, 10, FALSE, TRUE,  12, 'Curl con Bilanciere');

-- ==============================================================================
-- SERIE — Sessione 13: Marco - Full Body A (16 gg fa)
-- ==============================================================================
INSERT INTO SERIE (ordineEsecuzione, caricoKG, reps, isWarmUp,
                   isFailure, idSessione, nomeEsercizio) VALUES
(1, 90.0, 6, FALSE, FALSE, 13, 'Squat'),
(2, 95.0, 6, FALSE, FALSE, 13, 'Squat'),
(3, 100.0,6, FALSE, FALSE, 13, 'Squat'),
(4, 105.0,4, FALSE, TRUE,  13, 'Squat'),
(5, 80.0, 6, FALSE, FALSE, 13, 'Panca Piana'),
(6, 85.0, 6, FALSE, FALSE, 13, 'Panca Piana'),
(7, 90.0, 4, FALSE, TRUE,  13, 'Panca Piana');

-- ==============================================================================
-- SERIE — Sessione 14: Marco - Full Body B (9 gg fa)
-- ==============================================================================
INSERT INTO SERIE (ordineEsecuzione, caricoKG, reps, isWarmUp,
                   isFailure, idSessione, nomeEsercizio) VALUES
(1, 40.0, 10, FALSE, FALSE, 14, 'Military Press'),
(2, 42.5, 8,  FALSE, FALSE, 14, 'Military Press'),
(3, 45.0, 6,  FALSE, TRUE,  14, 'Military Press'),
(4, 55.0, 10, FALSE, FALSE, 14, 'Lat Pulldown'),
(5, 60.0, 10, FALSE, FALSE, 14, 'Lat Pulldown'),
(6, 65.0, 8,  FALSE, TRUE,  14, 'Lat Pulldown');

-- ==============================================================================
-- SERIE — Sessione 15: Anna - Chest & Tri (12 gg fa)
-- ==============================================================================
INSERT INTO SERIE (ordineEsecuzione, caricoKG, reps, isWarmUp,
                   isFailure, idSessione, nomeEsercizio) VALUES
(1, 25.0, 12, FALSE, FALSE, 15, 'Chest Press Guidata'),
(2, 27.5, 12, FALSE, FALSE, 15, 'Chest Press Guidata'),
(3, 30.0, 10, FALSE, TRUE,  15, 'Chest Press Guidata'),
(4, 15.0, 15, FALSE, FALSE, 15, 'Tricep Pushdown'),
(5, 17.5, 15, FALSE, FALSE, 15, 'Tricep Pushdown'),
(6, 20.0, 12, FALSE, TRUE,  15, 'Tricep Pushdown');

-- ==============================================================================
-- SERIE — Sessione 16: Anna - Back & Bi (5 gg fa)
-- ==============================================================================
INSERT INTO SERIE (ordineEsecuzione, caricoKG, reps, isWarmUp,
                   isFailure, idSessione, nomeEsercizio) VALUES
(1, 28.0, 12, FALSE, FALSE, 16, 'Lat Pulldown'),
(2, 30.0, 12, FALSE, FALSE, 16, 'Lat Pulldown'),
(3, 32.5, 10, FALSE, TRUE,  16, 'Lat Pulldown'),
(4, 15.0, 15, FALSE, FALSE, 16, 'Curl con Bilanciere'),
(5, 17.5, 15, FALSE, FALSE, 16, 'Curl con Bilanciere'),
(6, 20.0, 12, FALSE, TRUE,  16, 'Curl con Bilanciere');

-- ==============================================================================
-- MESSAGGI (feedback dei coach)
-- ==============================================================================

INSERT INTO MESSAGGI (testo, timeStamp, autoreEmail, idSessione) VALUES
('Ottimo PR al Squat! Mantieni la progressione lineare ancora 2-3 settimane.',
 DATE_SUB(CURRENT_DATE, INTERVAL 4 DAY),
 'mario.rossi@unibo.it', 4),

('Nuovo record alla panca, bravo! Controlla la traiettoria del bilanciere.',
 DATE_SUB(CURRENT_DATE, INTERVAL 1 DAY),
 'mario.rossi@unibo.it', 5),

('Buon lavoro Luca, volume push in crescita. Aumenta il carico alla panca.',
 DATE_SUB(CURRENT_DATE, INTERVAL 2 DAY),
 'mario.rossi@unibo.it', 9),

('Sara, ottima sessione upper. Aumentiamo il volume al lat pulldown.',
 DATE_SUB(CURRENT_DATE, INTERVAL 3 DAY),
 'giulia.ferrari@unibo.it', 12),

('Marco, forza in crescita costante. La prossima sessione proviamo 110kg.',
 DATE_SUB(CURRENT_DATE, INTERVAL 8 DAY),
 'giulia.ferrari@unibo.it', 13);
 