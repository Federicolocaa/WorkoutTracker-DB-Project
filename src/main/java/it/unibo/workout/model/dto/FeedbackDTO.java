package it.unibo.workout.model.dto;

/**
 * Record DTO che rappresenta un messaggio di feedback inviato dal Coach a un atleta su una
 * specifica sessione. Risultato della seconda parte dell'OP 11.
 */
public record FeedbackDTO(
    String nome, String cognome, String dataSessione, String testo, String timeStamp) {

  /** Restituisce il nome completo dell'atleta destinatario. */
  public String nomeCompleto() {
    return nome + " " + cognome;
  }

  /** Restituisce una riga formattata per la dashboard coach. */
  public String label() {
    return String.format("[%s] %s → %s", dataSessione, nomeCompleto(), testo);
  }
}
