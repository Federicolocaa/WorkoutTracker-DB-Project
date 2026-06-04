package it.unibo.workout.model.dto;

/**
 * Record DTO che rappresenta un esercizio variante (sostituto). Risultato dell'OP 8 — ricerca
 * tramite relazione ricorsiva VARIANTI.
 */
public record VarianteDTO(String nomeEsercizio, String descrizione, String tipoEsercizio) {

  /** Indica se la variante richiede attrezzatura specifica. */
  public boolean isGuidato() {
    return "Guidato".equalsIgnoreCase(tipoEsercizio);
  }

  /** Restituisce un'etichetta leggibile per la UI. */
  public String label() {
    String tipo = isGuidato() ? "[Guidato]" : "[Libero]";
    return String.format("%-30s %s", nomeEsercizio, tipo);
  }
}
