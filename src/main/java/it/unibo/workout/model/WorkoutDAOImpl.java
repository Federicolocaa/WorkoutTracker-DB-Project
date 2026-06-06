package it.unibo.workout.model;

import it.unibo.workout.model.api.WorkoutDAO;
import it.unibo.workout.model.dto.AtletaDTO;
import it.unibo.workout.model.dto.AttrezzaturaDTO;
import it.unibo.workout.model.dto.FeedbackDTO;
import it.unibo.workout.model.dto.GiornataDTO;
import it.unibo.workout.model.dto.PRAtletaDTO;
import it.unibo.workout.model.dto.PianificazioneDTO;
import it.unibo.workout.model.dto.RecordPersonaleDTO;
import it.unibo.workout.model.dto.ReportVolumeDTO;
import it.unibo.workout.model.dto.SessioneRecenteDTO;
import it.unibo.workout.model.dto.VarianteDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementazione di WorkoutDAO per le 12 operazioni principali del sistema di monitoraggio
 * atletico tramite MySQL.
 */
public class WorkoutDAOImpl implements WorkoutDAO {

  // =========================================================================
  // OP 2 — Fase 1: crea il record SCHEDE
  // =========================================================================
  @Override
  public Optional<Integer> creaSchedaIniziale(String nomeScheda, String atletaEmail) {
    final String query =
        "INSERT INTO SCHEDE (nomeScheda, dataCreazione, utenteEmail) "
            + "VALUES (?, CURRENT_DATE, ?)";
    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
      ps.setString(1, nomeScheda);
      ps.setString(2, atletaEmail);
      ps.executeUpdate();
      try (ResultSet rs = ps.getGeneratedKeys()) {
        if (rs.next()) return Optional.of(rs.getInt(1));
      }
    } catch (SQLException e) {
      System.err.println("Errore creaSchedaIniziale: " + e.getMessage());
    }
    return Optional.empty();
  }

  // =========================================================================
  // OP 2 — Fase 2: aggiunge una giornata alla scheda
  // =========================================================================
  @Override
  public Optional<Integer> aggiungiGiornata(int idScheda, String giorno, String tipo) {
    final String query = "INSERT INTO GIORNATE (giorno, tipo, idScheda) VALUES (?, ?, ?)";
    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
      ps.setString(1, giorno);
      ps.setString(2, tipo);
      ps.setInt(3, idScheda);
      ps.executeUpdate();
      try (ResultSet rs = ps.getGeneratedKeys()) {
        if (rs.next()) return Optional.of(rs.getInt(1));
      }
    } catch (SQLException e) {
      System.err.println("Errore aggiungiGiornata: " + e.getMessage());
    }
    return Optional.empty();
  }

  @Override
  public Optional<GiornataDTO> getDettaglioGiornata(int idGiornata) {
    final String qHeader =
        "SELECT g.giorno, g.tipo, s.utenteEmail "
            + "FROM GIORNATE g JOIN SCHEDE s ON s.idScheda = g.idScheda "
            + "WHERE g.idGiornata = ?";
    final String qEsercizi =
        "SELECT s.nomeScheda, g.idGiornata, g.giorno, g.tipo, "
            + "p.nomeEsercizio, p.ordine, p.serieTarget, p.repsTarget "
            + "FROM PIANIFICAZIONI p "
            + "JOIN GIORNATE g ON g.idGiornata = p.idGiornata "
            + "JOIN SCHEDE s ON s.idScheda = g.idScheda "
            + "WHERE p.idGiornata = ? ORDER BY p.ordine";

    try (Connection conn = DatabaseConnection.getConnection()) {
      String giorno, tipo, email;
      try (PreparedStatement ps = conn.prepareStatement(qHeader)) {
        ps.setInt(1, idGiornata);
        try (ResultSet rs = ps.executeQuery()) {
          if (!rs.next()) return Optional.empty();
          giorno = rs.getString("giorno");
          tipo = rs.getString("tipo");
          email = rs.getString("utenteEmail");
        }
      }

      List<PianificazioneDTO> esercizi = new ArrayList<>();
      try (PreparedStatement ps = conn.prepareStatement(qEsercizi)) {
        ps.setInt(1, idGiornata);
        try (ResultSet rs = ps.executeQuery()) {
          while (rs.next()) {
            esercizi.add(
                new PianificazioneDTO(
                    rs.getString("nomeScheda"),
                    rs.getInt("idGiornata"),
                    rs.getString("giorno"),
                    rs.getString("tipo"),
                    rs.getString("nomeEsercizio"),
                    rs.getInt("ordine"),
                    rs.getInt("serieTarget"),
                    rs.getInt("repsTarget")));
          }
        }
      }
      return Optional.of(new GiornataDTO(idGiornata, giorno, tipo, email, esercizi));
    } catch (SQLException e) {
      System.err.println("Errore getDettaglioGiornata: " + e.getMessage());
      return Optional.empty();
    }
  }

  // =========================================================================
  // OP 2 — Fase 3: aggiunge un esercizio alla giornata
  // =========================================================================
  @Override
  public boolean aggiungiEsercizio(
      int idGiornata, String nomeEsercizio, int ordine, int serieTarget, int repsTarget) {
    final String del = "DELETE FROM PIANIFICAZIONI WHERE idGiornata = ? AND ordine = ?";
    final String ins =
        "INSERT INTO PIANIFICAZIONI (idGiornata, nomeEsercizio, ordine, serieTarget, repsTarget) "
            + "VALUES (?, ?, ?, ?, ?) "
            + "ON DUPLICATE KEY UPDATE ordine = VALUES(ordine), "
            + "serieTarget = VALUES(serieTarget), repsTarget = VALUES(repsTarget)";
    try (Connection conn = DatabaseConnection.getConnection()) {
      try (PreparedStatement ps = conn.prepareStatement(del)) {
        ps.setInt(1, idGiornata);
        ps.setInt(2, ordine);
        ps.executeUpdate();
      }
      try (PreparedStatement ps = conn.prepareStatement(ins)) {
        ps.setInt(1, idGiornata);
        ps.setString(2, nomeEsercizio);
        ps.setInt(3, ordine);
        ps.setInt(4, serieTarget);
        ps.setInt(5, repsTarget);
        return ps.executeUpdate() > 0;
      }
    } catch (SQLException e) {
      System.err.println("Errore aggiungiEsercizio: " + e.getMessage());
      return false;
    }
  }

  // =========================================================================
  // Validazione
  // =========================================================================
  @Override
  public boolean nomeSchedaEsistente(String nomeScheda) {
    final String query = "SELECT 1 FROM SCHEDE WHERE nomeScheda = ?";
    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(query)) {
      ps.setString(1, nomeScheda);
      try (ResultSet rs = ps.executeQuery()) {
        return rs.next();
      }
    } catch (SQLException e) {
      System.err.println("Errore nomeSchedaEsistente: " + e.getMessage());
      return false;
    }
  }

  @Override
  public int countGiornate(int idScheda) {
    final String query = "SELECT COUNT(DISTINCT giorno) FROM GIORNATE WHERE idScheda = ?";
    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(query)) {
      ps.setInt(1, idScheda);
      try (ResultSet rs = ps.executeQuery()) {
        return rs.next() ? rs.getInt(1) : 0;
      }
    } catch (SQLException e) {
      System.err.println("Errore countGiornate: " + e.getMessage());
      return 0;
    }
  }

  @Override
  public int countEsercizi(int idGiornata) {
    final String query = "SELECT COUNT(*) FROM PIANIFICAZIONI WHERE idGiornata = ?";
    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(query)) {
      ps.setInt(1, idGiornata);
      try (ResultSet rs = ps.executeQuery()) {
        return rs.next() ? rs.getInt(1) : 0;
      }
    } catch (SQLException e) {
      System.err.println("Errore countEsercizi: " + e.getMessage());
      return 0;
    }
  }

  @Override
  public List<PianificazioneDTO> getSchedaAtleta(String atletaEmail) {
    final String query =
        "SELECT s.nomeScheda, g.idGiornata, g.giorno, g.tipo, "
            + "p.nomeEsercizio, p.ordine, p.serieTarget, p.repsTarget "
            + "FROM SCHEDE s "
            + "JOIN GIORNATE g ON g.idScheda = s.idScheda "
            + "JOIN PIANIFICAZIONI p ON p.idGiornata = g.idGiornata "
            + "WHERE s.utenteEmail = ? "
            + "ORDER BY FIELD(g.giorno,'Lunedi','Martedi','Mercoledi',"
            + "'Giovedi','Venerdi','Sabato','Domenica'), g.tipo, p.ordine";

    final List<PianificazioneDTO> result = new ArrayList<>();
    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(query)) {
      ps.setString(1, atletaEmail);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          result.add(
              new PianificazioneDTO(
                  rs.getString("nomeScheda"),
                  rs.getInt("idGiornata"),
                  rs.getString("giorno"),
                  rs.getString("tipo"),
                  rs.getString("nomeEsercizio"),
                  rs.getInt("ordine"),
                  rs.getInt("serieTarget"),
                  rs.getInt("repsTarget")));
        }
      }
    } catch (SQLException e) {
      System.err.println("Errore getSchedaAtleta: " + e.getMessage());
    }
    return result;
  }

  @Override
  public List<AtletaDTO> getAtletiDelCoach(String coachEmail) {
    final String query =
        "SELECT email, nome, cognome, obiettivo "
            + "FROM UTENTI "
            + "WHERE supervisoreEmail = ? AND tipoUtente = 'Atleta' "
            + "ORDER BY cognome, nome";
    final List<AtletaDTO> result = new ArrayList<>();
    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(query)) {
      ps.setString(1, coachEmail);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          result.add(
              new AtletaDTO(
                  rs.getString("email"),
                  rs.getString("nome"),
                  rs.getString("cognome"),
                  rs.getString("obiettivo")));
        }
      }
    } catch (SQLException e) {
      System.err.println("Errore getAtletiDelCoach: " + e.getMessage());
    }
    return result;
  }

  @Override
  public List<SessioneRecenteDTO> getSessioniAtleta(String atletaEmail) {
    final String query =
        "SELECT s.idSessione, u.nome, u.cognome, s.data, "
            + "s.volumeTotale, s.idGiornata "
            + "FROM SESSIONI s "
            + "JOIN UTENTI u ON u.email = s.utenteEmail "
            + "WHERE s.utenteEmail = ? "
            + "ORDER BY s.data DESC, s.idSessione DESC";

    final List<SessioneRecenteDTO> result = new ArrayList<>();
    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(query)) {
      ps.setString(1, atletaEmail);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          result.add(
              new SessioneRecenteDTO(
                  rs.getInt("idSessione"),
                  rs.getString("nome"),
                  rs.getString("cognome"),
                  rs.getString("data"),
                  rs.getDouble("volumeTotale"),
                  rs.getInt("idGiornata")));
        }
      }
    } catch (SQLException e) {
      System.err.println("Errore getSessioniAtleta: " + e.getMessage());
    }
    return result;
  }

  // =========================================================================
  // OP 3 — Inserimento nuova sessione
  // =========================================================================
  @Override
  public Optional<Integer> inserisciSessione(int idGiornata, String utenteEmail) {
    final String query =
        "INSERT INTO SESSIONI (data, durata, note, volumeTotale, "
            + "idGiornata, utenteEmail) VALUES (CURRENT_DATE, NULL, NULL, 0, ?, ?)";

    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

      pstmt.setInt(1, idGiornata);
      pstmt.setString(2, utenteEmail);
      pstmt.executeUpdate();

      try (ResultSet rs = pstmt.getGeneratedKeys()) {
        if (rs.next()) {
          return Optional.of(rs.getInt(1));
        }
      }
    } catch (SQLException e) {
      System.err.println("Errore OP 3: " + e.getMessage());
    }
    return Optional.empty();
  }

  // =========================================================================
  // OP 4 — Registrazione serie
  // =========================================================================
  @Override
  public boolean registraSerie(
      int ordineEsecuzione,
      double caricoKG,
      int reps,
      boolean isWarmUp,
      Double zavorra,
      Boolean isFailure,
      String noteWarmUp,
      int idSessione,
      String nomeEsercizio) {
    final String query =
        "INSERT INTO SERIE (ordineEsecuzione, caricoKG, reps, isWarmUp, "
            + "zavorra, isFailure, noteWarmUp, idSessione, nomeEsercizio) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query)) {

      pstmt.setInt(1, ordineEsecuzione);
      pstmt.setDouble(2, caricoKG);
      pstmt.setInt(3, reps);
      pstmt.setBoolean(4, isWarmUp);

      if (zavorra != null) pstmt.setDouble(5, zavorra);
      else pstmt.setNull(5, java.sql.Types.DECIMAL);

      if (isFailure != null) pstmt.setBoolean(6, isFailure);
      else pstmt.setNull(6, java.sql.Types.BOOLEAN);

      if (noteWarmUp != null) pstmt.setString(7, noteWarmUp);
      else pstmt.setNull(7, java.sql.Types.VARCHAR);

      pstmt.setInt(8, idSessione);
      pstmt.setString(9, nomeEsercizio);

      return pstmt.executeUpdate() > 0;

    } catch (SQLException e) {
      System.err.println("Errore OP 4: " + e.getMessage());
      return false;
    }
  }

  @Override
  public int prossimoOrdineSerie(int idSessione, String nomeEsercizio) {
    final String query =
        "SELECT COALESCE(MAX(ordineEsecuzione), 0) + 1 "
            + "FROM SERIE WHERE idSessione = ? AND nomeEsercizio = ?";
    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(query)) {
      ps.setInt(1, idSessione);
      ps.setString(2, nomeEsercizio);
      try (ResultSet rs = ps.executeQuery()) {
        return rs.next() ? rs.getInt(1) : 1;
      }
    } catch (SQLException e) {
      System.err.println("Errore prossimoOrdineSerie: " + e.getMessage());
      return 1;
    }
  }

  // =========================================================================
  // OP 5 — Record Personale
  // =========================================================================
  @Override
  public Optional<RecordPersonaleDTO> getRecordPersonale(String utenteEmail, String nomeEsercizio) {
    final String query =
        "SELECT s.caricoKG, s.reps, se.data "
            + "FROM SERIE s "
            + "JOIN SESSIONI se ON s.idSessione = se.idSessione "
            + "WHERE s.nomeEsercizio = ? AND se.utenteEmail = ? "
            + "AND s.isWarmUp = FALSE "
            + "ORDER BY s.caricoKG DESC, s.reps DESC LIMIT 1";

    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query)) {

      pstmt.setString(1, nomeEsercizio);
      pstmt.setString(2, utenteEmail);

      try (ResultSet rs = pstmt.executeQuery()) {
        if (rs.next()) {
          return Optional.of(
              new RecordPersonaleDTO(
                  nomeEsercizio,
                  rs.getDouble("caricoKG"),
                  rs.getInt("reps"),
                  rs.getString("data")));
        }
      }
    } catch (SQLException e) {
      System.err.println("Errore OP 5: " + e.getMessage());
    }
    return Optional.empty();
  }

  // =========================================================================
  // OP 6 — Misurazione biometrica
  // =========================================================================
  @Override
  public boolean inserisciMisurazione(
      double valorePeso, Double percentualeGrasso, String utenteEmail) {
    final String query =
        "INSERT INTO MISURAZIONI (data, valorePeso, percentualeGrasso, utenteEmail) "
            + "VALUES (CURRENT_DATE, ?, ?, ?)";

    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query)) {

      pstmt.setDouble(1, valorePeso);
      if (percentualeGrasso != null) pstmt.setDouble(2, percentualeGrasso);
      else pstmt.setNull(2, java.sql.Types.DECIMAL);
      pstmt.setString(3, utenteEmail);

      return pstmt.executeUpdate() > 0;

    } catch (SQLException e) {
      System.err.println("Errore OP 6: " + e.getMessage());
      return false;
    }
  }

  // =========================================================================
  // OP 7 — Feedback Coach
  // =========================================================================
  @Override
  public boolean inviaFeedback(String testo, String autoreEmail, int idSessione) {
    final String query =
        "INSERT INTO MESSAGGI (testo, timeStamp, autoreEmail, idSessione) "
            + "VALUES (?, CURRENT_TIMESTAMP, ?, ?)";

    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query)) {

      pstmt.setString(1, testo);
      pstmt.setString(2, autoreEmail);
      pstmt.setInt(3, idSessione);

      return pstmt.executeUpdate() > 0;

    } catch (SQLException e) {
      System.err.println("Errore OP 7: " + e.getMessage());
      return false;
    }
  }

  // =========================================================================
  // OP 8 — Varianti esercizio (relazione ricorsiva)
  // =========================================================================
  @Override
  public List<VarianteDTO> getVarianti(String nomeEsercizio) {
    final String query =
        "SELECT e.nomeEsercizio, e.descrizione, e.tipoEsercizio "
            + "FROM VARIANTI v "
            + "JOIN ESERCIZI e ON e.nomeEsercizio = v.nomeEsercizioSostituto "
            + "WHERE v.nomeEsercizioBase = ?";

    final List<VarianteDTO> result = new ArrayList<>();

    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query)) {

      pstmt.setString(1, nomeEsercizio);

      try (ResultSet rs = pstmt.executeQuery()) {
        while (rs.next()) {
          result.add(
              new VarianteDTO(
                  rs.getString("nomeEsercizio"),
                  rs.getString("descrizione"),
                  rs.getString("tipoEsercizio")));
        }
      }
    } catch (SQLException e) {
      System.err.println("Errore OP 8: " + e.getMessage());
    }
    return result;
  }

  // =========================================================================
  // OP 9 — Volume mensile per gruppo muscolare
  // =========================================================================
  @Override
  public Optional<Double> getVolumeMensile(String utenteEmail, String nomeMuscolo) {
    final String query =
        "SELECT SUM(s.caricoKG * s.reps) AS volumeTotaleMensile "
            + "FROM SERIE s "
            + "JOIN SESSIONI se ON se.idSessione = s.idSessione "
            + "JOIN COINVOLGIMENTI c ON c.nomeEsercizio = s.nomeEsercizio "
            + "WHERE c.nomeMuscolo = ? AND se.utenteEmail = ? "
            + "AND s.isWarmUp = FALSE "
            + "AND se.data >= DATE_SUB(CURRENT_DATE, INTERVAL 1 MONTH)";

    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query)) {

      pstmt.setString(1, nomeMuscolo);
      pstmt.setString(2, utenteEmail);

      try (ResultSet rs = pstmt.executeQuery()) {
        if (rs.next() && rs.getObject("volumeTotaleMensile") != null) {
          return Optional.of(rs.getDouble("volumeTotaleMensile"));
        }
      }
    } catch (SQLException e) {
      System.err.println("Errore OP 9: " + e.getMessage());
    }
    return Optional.empty();
  }

  // =========================================================================
  // OP 10 — Attrezzatura necessaria per una giornata
  // =========================================================================
  @Override
  public List<AttrezzaturaDTO> getAttrezzaturaGiornata(int idGiornata) {
    final String query =
        "SELECT DISTINCT a.nomeAttrezzo, a.marca "
            + "FROM PIANIFICAZIONI p "
            + "JOIN ESERCIZI e ON e.nomeEsercizio = p.nomeEsercizio "
            + "JOIN ATTREZZATURE a ON a.idAttrezzo = e.idAttrezzo "
            + "WHERE p.idGiornata = ? AND e.tipoEsercizio = 'Guidato'";

    final List<AttrezzaturaDTO> result = new ArrayList<>();

    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query)) {

      pstmt.setInt(1, idGiornata);

      try (ResultSet rs = pstmt.executeQuery()) {
        while (rs.next()) {
          result.add(new AttrezzaturaDTO(rs.getString("nomeAttrezzo"), rs.getString("marca")));
        }
      }
    } catch (SQLException e) {
      System.err.println("Errore OP 10: " + e.getMessage());
    }
    return result;
  }

  // =========================================================================
  // OP 11 — PR atleti supervisionati
  // =========================================================================
  @Override
  public List<PRAtletaDTO> getPRAtleti(String coachEmail) {
    final String query =
        "SELECT u.nome, u.cognome, s.nomeEsercizio, "
            + "MAX(s.caricoKG) AS pr, MAX(se.data) AS dataUltima "
            + "FROM UTENTI u "
            + "JOIN SESSIONI se ON se.utenteEmail = u.email "
            + "JOIN SERIE s ON s.idSessione = se.idSessione "
            + "WHERE u.supervisoreEmail = ? AND s.isWarmUp = FALSE "
            + "GROUP BY u.email, u.nome, u.cognome, s.nomeEsercizio "
            + "ORDER BY u.cognome, s.nomeEsercizio";

    final List<PRAtletaDTO> result = new ArrayList<>();

    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query)) {

      pstmt.setString(1, coachEmail);

      try (ResultSet rs = pstmt.executeQuery()) {
        while (rs.next()) {
          result.add(
              new PRAtletaDTO(
                  rs.getString("nome"),
                  rs.getString("cognome"),
                  rs.getString("nomeEsercizio"),
                  rs.getDouble("pr"),
                  rs.getString("dataUltima")));
        }
      }
    } catch (SQLException e) {
      System.err.println("Errore OP 11 PR: " + e.getMessage());
    }
    return result;
  }

  // =========================================================================
  // OP 11 — Feedback inviati dal coach
  // =========================================================================
  @Override
  public List<FeedbackDTO> getFeedbackCoach(String coachEmail) {
    final String query =
        "SELECT u.nome, u.cognome, se.data, m.testo, m.timeStamp "
            + "FROM MESSAGGI m "
            + "JOIN SESSIONI se ON se.idSessione = m.idSessione "
            + "JOIN UTENTI u ON u.email = se.utenteEmail "
            + "WHERE m.autoreEmail = ? "
            + "ORDER BY m.timeStamp DESC";

    final List<FeedbackDTO> result = new ArrayList<>();

    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query)) {

      pstmt.setString(1, coachEmail);

      try (ResultSet rs = pstmt.executeQuery()) {
        while (rs.next()) {
          result.add(
              new FeedbackDTO(
                  rs.getString("nome"),
                  rs.getString("cognome"),
                  rs.getString("data"),
                  rs.getString("testo"),
                  rs.getString("timeStamp")));
        }
      }
    } catch (SQLException e) {
      System.err.println("Errore OP 11 feedback: " + e.getMessage());
    }
    return result;
  }

  @Override
  public List<SessioneRecenteDTO> getSessioniRecentiAtleti(String coachEmail) {
    final String query =
        "SELECT s.idSessione, u.nome, u.cognome, s.data, "
            + "s.volumeTotale, s.idGiornata "
            + "FROM SESSIONI s "
            + "JOIN UTENTI u ON u.email = s.utenteEmail "
            + "WHERE u.supervisoreEmail = ? "
            + "ORDER BY s.data DESC, s.idSessione DESC";

    final List<SessioneRecenteDTO> result = new ArrayList<>();
    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(query)) {
      ps.setString(1, coachEmail);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          result.add(
              new SessioneRecenteDTO(
                  rs.getInt("idSessione"),
                  rs.getString("nome"),
                  rs.getString("cognome"),
                  rs.getString("data"),
                  rs.getDouble("volumeTotale"),
                  rs.getInt("idGiornata")));
        }
      }
    } catch (SQLException e) {
      System.err.println("Errore getSessioniRecentiAtleti: " + e.getMessage());
    }
    return result;
  }

  // =========================================================================
  // OP 12 — Report volume teorico vs reale
  // =========================================================================
  @Override
  public List<ReportVolumeDTO> getReportVolume(int idSessione) {
    final String query =
        "SELECT p.nomeEsercizio, p.serieTarget, p.repsTarget, "
            + "COUNT(s.idSerie) AS serieReali, "
            + "COALESCE(SUM(s.reps), 0) AS repsReali, "
            + "COALESCE(SUM(s.caricoKG * s.reps), 0) AS volumeRealeKg, "
            + "se.volumeTotale "
            + "FROM SESSIONI se "
            + "JOIN PIANIFICAZIONI p ON p.idGiornata = se.idGiornata "
            + "LEFT JOIN SERIE s ON s.idSessione = se.idSessione "
            + "  AND s.nomeEsercizio = p.nomeEsercizio AND s.isWarmUp = FALSE "
            + "WHERE se.idSessione = ? "
            + "GROUP BY p.nomeEsercizio, p.serieTarget, p.repsTarget, se.volumeTotale "
            + "ORDER BY p.nomeEsercizio";

    final List<ReportVolumeDTO> result = new ArrayList<>();

    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query)) {

      pstmt.setInt(1, idSessione);

      try (ResultSet rs = pstmt.executeQuery()) {
        while (rs.next()) {
          result.add(
              new ReportVolumeDTO(
                  rs.getString("nomeEsercizio"),
                  rs.getInt("serieTarget"),
                  rs.getInt("repsTarget"),
                  rs.getInt("serieReali"),
                  rs.getInt("repsReali"),
                  rs.getDouble("volumeRealeKg"),
                  rs.getDouble("volumeTotale")));
        }
      }
    } catch (SQLException e) {
      System.err.println("Errore OP 12: " + e.getMessage());
    }
    return result;
  }

  @Override
  public boolean assegnaSupervisore(String atletaEmail, String coachEmail) {
    final String query =
        "UPDATE UTENTI SET supervisoreEmail = ? " + "WHERE email = ? AND tipoUtente = 'Atleta'";
    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(query)) {
      ps.setString(1, coachEmail);
      ps.setString(2, atletaEmail);
      return ps.executeUpdate() > 0;
    } catch (SQLException e) {
      System.err.println("Errore assegnaSupervisore: " + e.getMessage());
      return false;
    }
  }
}
