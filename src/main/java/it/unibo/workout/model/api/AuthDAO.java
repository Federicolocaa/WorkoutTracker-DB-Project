package it.unibo.workout.model.api;

import it.unibo.workout.model.dto.UtenteDTO;
import java.util.Optional;

/**
 * Interfaccia che definisce le operazioni di autenticazione e registrazione degli utenti nel
 * sistema.
 */
public interface AuthDAO {

  /**
   * Verifica le credenziali e restituisce l'utente autenticato. Restituisce Optional.empty() se le
   * credenziali sono errate.
   *
   * @param email l'email dell'utente
   * @param password la password in chiaro
   * @return Optional contenente l'UtenteDTO se il login ha successo
   */
  Optional<UtenteDTO> login(String email, String password);

  /**
   * Registra un nuovo profilo atleta nel sistema (OP 1).
   *
   * @return true se la registrazione è avvenuta con successo
   */
  boolean registraAtleta(
      String email,
      String password,
      String dataNascita,
      String nome,
      String cognome,
      int altezza,
      double pesoIniziale,
      String obiettivo);

  /**
   * Registra un nuovo profilo Coach nel sistema, richiedendo la certificazione e la
   * specializzazione.
   */
  boolean registraCoach(
      String email,
      String password,
      String dataNascita,
      String nome,
      String cognome,
      String certificazione,
      String specializzazione);

  /**
   * Verifica se un'email è già registrata nel sistema.
   *
   * @param email l'email da verificare
   * @return true se l'email è già presente
   */
  boolean emailEsistente(String email);
}
