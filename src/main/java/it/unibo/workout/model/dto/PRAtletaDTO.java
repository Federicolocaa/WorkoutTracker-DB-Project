package it.unibo.workout.model.dto;

/**
 * Record DTO che rappresenta il Record Personale di un atleta supervisionato dal Coach. Risultato
 * della prima parte dell'OP 11.
 */
public record PRAtletaDTO(
    String nome, String cognome, String nomeEsercizio, double pr, String dataUltima) {

  /** Restituisce il nome completo dell'atleta. */
  public String nomeCompleto() {
    return nome + " " + cognome;
  }

  /** Restituisce una riga formattata per la dashboard coach. */
  public String label() {
    return String.format(
        "%-20s | %-25s | PR: %6.2f kg | %s", nomeCompleto(), nomeEsercizio, pr, dataUltima);
  }
}
