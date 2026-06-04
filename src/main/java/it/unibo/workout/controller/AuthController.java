package it.unibo.workout.controller;

import it.unibo.workout.model.AuthDAOImpl;
import it.unibo.workout.model.api.AuthDAO;
import it.unibo.workout.model.dto.UtenteDTO;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Controller per la gestione dell'autenticazione e della registrazione. Fa da ponte tra LoginView e
 * AuthDAO, applicando la logica di validazione prima di delegare al layer di persistenza.
 */
public class AuthController {

  private final AuthDAO authDAO;

  public AuthController() {
    this.authDAO = new AuthDAOImpl();
  }

  // =========================================================================
  // Login
  // =========================================================================

  /**
   * Tenta il login con le credenziali fornite.
   *
   * @param email email dell'utente
   * @param password password in chiaro
   * @return Optional con l'UtenteDTO se le credenziali sono corrette
   */
  public Optional<UtenteDTO> login(String email, String password) {
    if (email == null || email.isBlank() || password == null || password.isBlank()) {
      return Optional.empty();
    }
    return authDAO.login(email.trim(), password.trim());
  }

  // =========================================================================
  // Registrazione
  // =========================================================================

  /**
   * Registra un nuovo atleta dopo aver validato i campi obbligatori.
   *
   * @return messaggio di esito leggibile dalla view
   */
  public String registraAtleta(
      String email,
      String password,
      String dataNascita,
      String nome,
      String cognome,
      String altezzaStr,
      String pesoStr,
      String obiettivo) {

    // Validazione campi obbligatori
    if (Stream.of(email, password, dataNascita, nome, cognome, altezzaStr, pesoStr, obiettivo)
        .anyMatch(s -> s == null || s.isBlank())) {
      return "Tutti i campi sono obbligatori.";
    }

    // Verifica email duplicata
    if (authDAO.emailEsistente(email.trim())) {
      return "Email già registrata nel sistema.";
    }

    // Parsing valori numerici
    int altezza;
    double peso;
    try {
      altezza = Integer.parseInt(altezzaStr.trim());
      peso = Double.parseDouble(pesoStr.trim());
    } catch (NumberFormatException e) {
      return "Altezza e peso devono essere numeri validi.";
    }

    boolean ok =
        authDAO.registraAtleta(
            email.trim(),
            password.trim(),
            dataNascita.trim(),
            nome.trim(),
            cognome.trim(),
            altezza,
            peso,
            obiettivo.trim());

    return ok
        ? "Registrazione completata con successo!"
        : "Errore durante la registrazione. Riprova.";
  }

  /** Registra un nuovo coach dopo aver validato i campi obbligatori. */
  public String registraCoach(
      String email,
      String password,
      String dataNascita,
      String nome,
      String cognome,
      String certificazione,
      String specializzazione) {

    // Validazione campi
    if (Stream.of(email, password, dataNascita, nome, cognome)
        .anyMatch(s -> s == null || s.isBlank())) {
      return "Tutti i campi base sono obbligatori.";
    }

    if (authDAO.emailEsistente(email.trim())) {
      return "Email già registrata nel sistema.";
    }

    boolean ok =
        authDAO.registraCoach(
            email.trim(),
            password.trim(),
            dataNascita.trim(),
            nome.trim(),
            cognome.trim(),
            certificazione.trim(),
            specializzazione.trim());

    return ok ? "Registrazione Coach completata!" : "Errore durante la registrazione. Riprova.";
  }

  // =========================================================================
  // Utilità
  // =========================================================================

  /** Verifica se un'email è già presente nel sistema. */
  public boolean emailEsistente(String email) {
    return email != null && !email.isBlank() && authDAO.emailEsistente(email.trim());
  }

  public String hashPassword(String pwd) {
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      byte[] hash = md.digest(pwd.getBytes(StandardCharsets.UTF_8));
      StringBuilder hexString = new StringBuilder();
      for (byte b : hash) {
        String hex = Integer.toHexString(0xff & b);
        if (hex.length() == 1) hexString.append('0');
        hexString.append(hex);
      }
      return hexString.toString();
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }
}
