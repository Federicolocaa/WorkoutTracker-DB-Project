package it.unibo.workout.model.dto;

/**
 * Record DTO che rappresenta il Record Personale (PR) di un atleta su un determinato esercizio.
 * Risultato dell'OP 5.
 */
public record RecordPersonaleDTO(String nomeEsercizio, double caricoKG, int reps, String data) {

  /** Calcola il volume del PR (carico × reps). */
  public double volume() {
    return caricoKG * reps;
  }

  /** Restituisce una descrizione leggibile del PR. */
  public String descrizione() {
    return String.format("%.2f kg × %d reps — %s (vol. %.1f kg)", caricoKG, reps, data, volume());
  }
}
