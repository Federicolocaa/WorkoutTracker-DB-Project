package it.unibo.workout.model.dto;

/**
 * Record DTO che rappresenta un utente autenticato nel sistema. Trasporta le informazioni
 * essenziali tra i layer senza esporre dettagli sensibili come la password.
 */
public record UtenteDTO(String email, String nome, String cognome, String tipoUtente) {

  /** Verifica se l'utente è un Coach. */
  public boolean isCoach() {
    return "Coach".equalsIgnoreCase(tipoUtente);
  }

  /** Verifica se l'utente è un Atleta. */
  public boolean isAtleta() {
    return "Atleta".equalsIgnoreCase(tipoUtente);
  }

  /** Restituisce il nome completo formattato. */
  public String nomeCompleto() {
    return nome + " " + cognome;
  }

  /** Restituisce un'etichetta ruolo leggibile. */
  public String ruoloLabel() {
    return isCoach() ? "🏋 Coach" : "💪 Atleta";
  }
}
