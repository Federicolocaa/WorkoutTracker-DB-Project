USE WorkoutDB;

DROP TRIGGER IF EXISTS AggiornaVolumeTotaleSessione;

DELIMITER //
CREATE TRIGGER AggiornaVolumeTotaleSessione
AFTER INSERT ON SERIE
FOR EACH ROW
BEGIN
    IF NEW.isWarmUp = FALSE THEN
        UPDATE SESSIONI
        SET volumeTotale = volumeTotale + (NEW.caricoKG * NEW.reps)
        WHERE idSessione = NEW.idSessione;
    END IF;
END;
//
DELIMITER ;