package it.unibo.workout.model.dto;

/**
 * Record DTO che rappresenta una sessione di allenamento completata da un atleta, usato dal coach
 * nella dashboard (OP 11) per individuare gli ID sessione a cui inviare feedback (OP 7).
 */
public record SessioneRecenteDTO(
    int idSessione, String nome, String cognome, String data, double volumeTotale, int idGiornata) {

  /** Nome completo dell'atleta. */
  public String nomeCompleto() {
    return nome + " " + cognome;
  }

  /**
   * Riga formattata per la dashboard coach. Es: "Sessione #12 — Federico Locatelli — 2026-05-20 —
   * 4500.0 kg"
   */
  public String label() {
    return "Sessione #%d — %s — %s — %.1f kg"
        .formatted(idSessione, nomeCompleto(), data, volumeTotale);
  }
}
