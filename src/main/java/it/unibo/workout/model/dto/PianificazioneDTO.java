package it.unibo.workout.model.dto;

/**
 * Record DTO che rappresenta una riga della scheda di allenamento, unendo giornata (giorno + tipo)
 * e pianificazione esercizio. Usato per visualizzare la scheda completa dell'atleta.
 */
public record PianificazioneDTO(
    String nomeScheda,
    int idGiornata,
    String giorno,
    String tipo,
    String nomeEsercizio,
    int ordine,
    int serieTarget,
    int repsTarget) {

  /** Chiave della giornata con ID. Es: "Lunedi — Push [ID 3]" */
  public String giornatLabel() {
    return giorno + " — " + tipo + "  [ID " + idGiornata + "]";
  }

  /** Restituisce una riga formattata per la visualizzazione della scheda. */
  public String label() {
    return String.format("  %d. %-25s %d×%d", ordine, nomeEsercizio, serieTarget, repsTarget);
  }
}
