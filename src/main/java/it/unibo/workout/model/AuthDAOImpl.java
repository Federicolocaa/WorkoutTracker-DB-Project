package it.unibo.workout.model;

import it.unibo.workout.model.api.AuthDAO;
import it.unibo.workout.model.dto.UtenteDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Implementazione di AuthDAO per la gestione dell'autenticazione e della registrazione utenti
 * tramite MySQL.
 */
public class AuthDAOImpl implements AuthDAO {

  // =========================================================================
  // Login
  // =========================================================================
  @Override
  public Optional<UtenteDTO> login(String email, String password) {
    final String query =
        "SELECT email, nome, cognome, tipoUtente "
            + "FROM UTENTI "
            + "WHERE email = ? AND password = ?";

    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query)) {

      pstmt.setString(1, email.trim());
      pstmt.setString(2, password.trim());

      try (ResultSet rs = pstmt.executeQuery()) {
        if (rs.next()) {
          return Optional.of(
              new UtenteDTO(
                  rs.getString("email"),
                  rs.getString("nome"),
                  rs.getString("cognome"),
                  rs.getString("tipoUtente")));
        }
      }
    } catch (SQLException e) {
      System.err.println("Errore login: " + e.getMessage());
    }
    return Optional.empty();
  }

  // =========================================================================
  // OP 1 — Registrazione nuovo atleta
  // =========================================================================
  @Override
  public boolean registraAtleta(
      String email,
      String password,
      String dataNascita,
      String nome,
      String cognome,
      int altezza,
      double pesoIniziale,
      String obiettivo) {
    final String query =
        "INSERT INTO UTENTI (email, password, dataNascita, nome, cognome, "
            + "tipoUtente, altezza, pesoIniziale, obiettivo) "
            + "VALUES (?, ?, ?, ?, ?, 'Atleta', ?, ?, ?)";

    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query)) {

      pstmt.setString(1, email.trim());
      pstmt.setString(2, password.trim());
      pstmt.setString(3, dataNascita.trim());
      pstmt.setString(4, nome.trim());
      pstmt.setString(5, cognome.trim());
      pstmt.setInt(6, altezza);
      pstmt.setDouble(7, pesoIniziale);
      pstmt.setString(8, obiettivo.trim());

      return pstmt.executeUpdate() > 0;

    } catch (SQLException e) {
      System.err.println("Errore OP 1: " + e.getMessage());
      return false;
    }
  }

  // =========================================================================
  // Registrazione nuovo Coach
  // =========================================================================
  @Override
  public boolean registraCoach(
      String email,
      String password,
      String dataNascita,
      String nome,
      String cognome,
      String certificazione,
      String specializzazione) {
    final String query =
        "INSERT INTO UTENTI (email, password, dataNascita, nome, cognome, "
            + "tipoUtente, certificazione, specializzazione) "
            + "VALUES (?, ?, ?, ?, ?, 'Coach', ?, ?)";

    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query)) {

      pstmt.setString(1, email.trim());
      pstmt.setString(2, password.trim());
      pstmt.setString(3, dataNascita.trim());
      pstmt.setString(4, nome.trim());
      pstmt.setString(5, cognome.trim());
      pstmt.setString(6, certificazione.trim());
      pstmt.setString(7, specializzazione.trim());

      return pstmt.executeUpdate() > 0;

    } catch (SQLException e) {
      System.err.println("Errore Registrazione Coach: " + e.getMessage());
      return false;
    }
  }

  // =========================================================================
  // Verifica email esistente
  // =========================================================================
  @Override
  public boolean emailEsistente(String email) {
    final String query = "SELECT 1 FROM UTENTI WHERE email = ?";

    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query)) {

      pstmt.setString(1, email.trim());

      try (ResultSet rs = pstmt.executeQuery()) {
        return rs.next();
      }
    } catch (SQLException e) {
      System.err.println("Errore verifica email: " + e.getMessage());
      return false;
    }
  }
}
