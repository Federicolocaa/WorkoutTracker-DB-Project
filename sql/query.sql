USE WorkoutDB;

-- ==============================================================================
-- 1. TRIGGER PER L'AGGIORNAMENTO AUTOMATICO DELLA RIDONDANZA (Volume_Totale)
-- ==============================================================================
-- Come documentato nella Sezione 3.4.1 e 3.8, ogni volta che viene inserita 
-- una nuova serie allenante (isWarmUp = FALSE), il database aggiorna 
-- automaticamente il volume totale della sessione corrispondente.

DELIMITER //

CREATE TRIGGER AggiornaVolumeTotaleSessione
AFTER INSERT ON SERIE
FOR EACH ROW
BEGIN
    -- Controlla se la serie inserita NON è di riscaldamento
    IF NEW.isWarmUp = FALSE THEN
        UPDATE SESSIONI
        SET volumeTotale = volumeTotale + (NEW.caricoKG * NEW.reps)
        WHERE idSessione = NEW.idSessione;
    END IF;
END;
//

DELIMITER ;


-- ==============================================================================
-- 2. QUERY DI TEST (Dalle Operazioni del Capitolo 3.8)
-- ==============================================================================

-- OP 5: Ricerca del Record Personale (PR) su un esercizio
-- (Esempio per l'utente federico@unibo.it sull'esercizio 'Panca Piana')
SELECT 
    s.caricoKG AS RecordPersonale,
    s.reps,
    se.data AS DataSessione
FROM SERIE s
JOIN SESSIONI se ON s.idSessione = se.idSessione
WHERE s.nomeEsercizio = 'Panca Piana'
  AND se.utenteEmail = 'federico@unibo.it'
  AND s.isWarmUp = FALSE
ORDER BY s.caricoKG DESC, s.reps DESC
LIMIT 1;

-- OP 9: Calcolo del volume totale mensile per gruppo muscolare (es. 'Petto')
SELECT 
    SUM(s.caricoKG * s.reps) AS VolumeTotaleMensile
FROM SERIE s
JOIN SESSIONI se ON se.idSessione = s.idSessione
JOIN COINVOLGIMENTI c ON c.nomeEsercizio = s.nomeEsercizio
WHERE c.nomeMuscolo = 'Petto'
  AND se.utenteEmail = 'federico@unibo.it'
  AND s.isWarmUp = FALSE
  AND se.data >= DATE_SUB(CURRENT_DATE, INTERVAL 1 MONTH);

-- OP 11: Dashboard Coach - Feedback inviati dal coach con dettaglio sessione
SELECT 
    u.nome, 
    u.cognome,
    se.data AS DataSessione,
    m.testo,
    m.timeStamp
FROM MESSAGGI m
JOIN SESSIONI se ON se.idSessione = m.idSessione
JOIN UTENTI u ON u.email = se.utenteEmail
WHERE m.autoreEmail = 'coach@unibo.it'
ORDER BY m.timeStamp DESC;
