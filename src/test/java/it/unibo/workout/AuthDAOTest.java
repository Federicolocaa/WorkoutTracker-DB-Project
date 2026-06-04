package it.unibo.workout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.unibo.workout.model.AuthDAOImpl;
import it.unibo.workout.model.DatabaseConnection;
import it.unibo.workout.model.api.AuthDAO;
import it.unibo.workout.model.dto.UtenteDTO;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * Test di integrazione per AuthDAO. Prerequisito: schema.sql + query.sql + population.sql eseguiti
 * su WorkoutDB.
 */
@TestMethodOrder(MethodOrderer.DisplayName.class)
class AuthDAOTest {

  private static AuthDAO authDAO;

  @BeforeAll
  static void setup() {
    assertNotNull(DatabaseConnection.getConnection(), "Connessione al database non disponibile.");
    authDAO = new AuthDAOImpl();
  }

  // =========================================================================
  // Login — credenziali corrette
  // =========================================================================

  @Test
  @DisplayName("LOGIN — Atleta con credenziali corrette")
  void testLogin_atletaCorretto() {
    Optional<UtenteDTO> result = authDAO.login("federico.locatelli@unibo.it", "fede123");

    assertTrue(result.isPresent(), "Il login deve avere successo con credenziali corrette");

    UtenteDTO utente = result.get();
    assertEquals("federico.locatelli@unibo.it", utente.email());
    assertEquals("Federico", utente.nome());
    assertEquals("Locatelli", utente.cognome());
    assertEquals("Atleta", utente.tipoUtente());
  }

  @Test
  @DisplayName("LOGIN — Coach con credenziali corrette")
  void testLogin_coachCorretto() {
    Optional<UtenteDTO> result = authDAO.login("mario.rossi@unibo.it", "coach123");

    assertTrue(result.isPresent(), "Il login del Coach deve avere successo");

    UtenteDTO utente = result.get();
    assertEquals("Mario", utente.nome());
    assertEquals("Rossi", utente.cognome());
    assertEquals("Coach", utente.tipoUtente());
    assertTrue(utente.isCoach(), "isCoach() deve restituire true per un Coach");
    assertFalse(utente.isAtleta(), "isAtleta() deve restituire false per un Coach");
  }

  @Test
  @DisplayName("LOGIN — Secondo coach con credenziali corrette")
  void testLogin_coachGiuliaCorretta() {
    Optional<UtenteDTO> result = authDAO.login("giulia.ferrari@unibo.it", "coach456");

    assertTrue(result.isPresent());
    assertEquals("Giulia", result.get().nome());
    assertTrue(result.get().isCoach());
  }

  // =========================================================================
  // Login — credenziali errate
  // =========================================================================

  @Test
  @DisplayName("LOGIN — Password errata restituisce Optional.empty()")
  void testLogin_passwordErrata() {
    Optional<UtenteDTO> result = authDAO.login("federico.locatelli@unibo.it", "passwordSbagliata");

    assertTrue(result.isEmpty(), "Con password errata il login deve fallire");
  }

  @Test
  @DisplayName("LOGIN — Email inesistente restituisce Optional.empty()")
  void testLogin_emailInesistente() {
    Optional<UtenteDTO> result = authDAO.login("utente.inesistente@unibo.it", "qualsiasi");

    assertTrue(result.isEmpty(), "Con email inesistente il login deve fallire");
  }

  @Test
  @DisplayName("LOGIN — Campi vuoti restituiscono Optional.empty()")
  void testLogin_campiVuoti() {
    Optional<UtenteDTO> result = authDAO.login("", "");

    assertTrue(result.isEmpty(), "Login con campi vuoti deve fallire");
  }

  // =========================================================================
  // UtenteDTO — metodi di utilità
  // =========================================================================

  @Test
  @DisplayName("UtenteDTO — nomeCompleto() formattato correttamente")
  void testUtenteDTO_nomeCompleto() {
    UtenteDTO utente = authDAO.login("federico.locatelli@unibo.it", "fede123").orElseThrow();

    assertEquals("Federico Locatelli", utente.nomeCompleto());
  }

  @Test
  @DisplayName("UtenteDTO — ruoloLabel() corretto per Atleta")
  void testUtenteDTO_ruoloLabelAtleta() {
    UtenteDTO utente = authDAO.login("federico.locatelli@unibo.it", "fede123").orElseThrow();

    assertEquals("💪 Atleta", utente.ruoloLabel());
  }

  @Test
  @DisplayName("UtenteDTO — ruoloLabel() corretto per Coach")
  void testUtenteDTO_ruoloLabelCoach() {
    UtenteDTO utente = authDAO.login("mario.rossi@unibo.it", "coach123").orElseThrow();

    assertEquals("🏋 Coach", utente.ruoloLabel());
  }

  // =========================================================================
  // emailEsistente
  // =========================================================================

  @Test
  @DisplayName("EMAIL — Email esistente restituisce true")
  void testEmailEsistente_presente() {
    assertTrue(
        authDAO.emailEsistente("federico.locatelli@unibo.it"),
        "L'email di Federico deve risultare presente");
  }

  @Test
  @DisplayName("EMAIL — Email inesistente restituisce false")
  void testEmailEsistente_assente() {
    assertFalse(
        authDAO.emailEsistente("nonregistrato@unibo.it"),
        "Un'email non registrata deve risultare assente");
  }

  // =========================================================================
  // OP 1 — Registrazione
  // =========================================================================

  @Test
  @DisplayName("OP1 — Registrazione nuovo atleta con email univoca")
  void testRegistraAtleta_success() {
    // Email univoca per evitare conflitti tra esecuzioni
    String email = "test.junit." + System.currentTimeMillis() + "@unibo.it";

    boolean result =
        authDAO.registraAtleta(
            email, "testpass", "2000-06-15", "Test", "JUnit", 175, 70.0, "Test obiettivo");

    assertTrue(result, "La registrazione deve avere successo con email univoca");
    assertTrue(
        authDAO.emailEsistente(email),
        "Dopo la registrazione l'email deve risultare presente nel DB");
  }

  @Test
  @DisplayName("OP1 — Registrazione con email già esistente fallisce")
  void testRegistraAtleta_emailDuplicata() {
    boolean result =
        authDAO.registraAtleta(
            "federico.locatelli@unibo.it",
            "altropass",
            "2001-01-01",
            "Doppio",
            "Test",
            180,
            75.0,
            "Obiettivo");

    assertFalse(result, "La registrazione deve fallire se l'email è già presente");
  }
}
