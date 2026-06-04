package it.unibo.workout.model.dto;

/**
 * Record DTO che rappresenta un atleta supervisionato da un coach. Usato per l'elenco atleti del
 * coach e per la selezione nel feedback (OP 7).
 */
public record AtletaDTO(String email, String nome, String cognome, String obiettivo) {

  /** Nome completo dell'atleta. */
  public String nomeCompleto() {
    return nome + " " + cognome;
  }

  /** Riga formattata per l'elenco. */
  public String label() {
    String ob = (obiettivo == null || obiettivo.isBlank()) ? "—" : obiettivo;
    return nomeCompleto() + " (" + email + ") — Obiettivo: " + ob;
  }
}
