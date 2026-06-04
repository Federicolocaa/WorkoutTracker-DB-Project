package it.unibo.workout.model.dto;

/**
 * Record DTO che rappresenta un'attrezzatura necessaria per completare una giornata di allenamento.
 * Risultato dell'OP 10.
 */
public record AttrezzaturaDTO(String nomeAttrezzo, String marca) {

  /** Restituisce la marca oppure "N/D" se non specificata. */
  public String marcaOrDefault() {
    return (marca != null && !marca.isBlank()) ? marca : "N/D";
  }

  /** Restituisce una riga formattata per la UI. */
  public String label() {
    return String.format("%s [%s]", nomeAttrezzo, marcaOrDefault());
  }
}
