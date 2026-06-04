package it.unibo.workout.model.dto;

import java.util.List;

/**
 * Record DTO con il dettaglio completo di una giornata: giorno, tipo, atleta proprietario e
 * l'elenco ordinato degli esercizi pianificati. Usato in OP 2 (coach: riprendi giornata) e OP 4
 * (atleta: esercizi della sessione).
 */
public record GiornataDTO(
    int idGiornata,
    String giorno,
    String tipo,
    String utenteEmail,
    List<PianificazioneDTO> esercizi) {

  /** Etichetta della giornata con ID. Es: "Lunedi — Push [ID 3]" */
  public String label() {
    return giorno + " — " + tipo + "  [ID " + idGiornata + "]";
  }

  /** Contenuto formattato (esercizi in ordine). */
  public String contenutoFormattato() {
    if (esercizi.isEmpty()) return "(nessun esercizio ancora in questa giornata)";
    StringBuilder sb = new StringBuilder();
    esercizi.forEach(e -> sb.append(e.label()).append("\n"));
    return sb.toString();
  }
}
