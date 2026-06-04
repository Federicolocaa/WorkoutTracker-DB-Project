package it.unibo.workout.model.dto;

/**
 * Record DTO che rappresenta una riga del report di confronto tra volume teorico pianificato e
 * volume reale effettuato. Risultato dell'OP 12.
 */
public record ReportVolumeDTO(
    String nomeEsercizio,
    int serieTarget,
    int repsTarget,
    int serieReali,
    int repsReali,
    double volumeRealeKg,
    double volumeTotaleSessione) {

  /** Calcola il volume teorico atteso (senza carico, solo reps). */
  public int volumeTeoricoReps() {
    return serieTarget * repsTarget;
  }

  /** Indica se le serie reali hanno raggiunto il target. */
  public boolean serieRaggiunte() {
    return serieReali >= serieTarget;
  }

  /** Restituisce una riga formattata per il report nella UI. */
  public String label() {
    String check = serieRaggiunte() ? "✓" : "✗";
    return String.format(
        "%s %-22s Tgt:%dx%d  Eff:%dx%d  Vol:%.1f kg",
        check, nomeEsercizio, serieTarget, repsTarget, serieReali, repsReali, volumeRealeKg);
  }
}
