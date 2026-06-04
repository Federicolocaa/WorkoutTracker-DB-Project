package it.unibo.workout.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** Classe Singleton per gestire la connessione al database MySQL. */
public class DatabaseConnection {

  private static Connection connection = null;

  // Parametri di connessione (modifica username e password in base al tuo MySQL locale)
  private static final String URL = "jdbc:mysql://localhost:3306/WorkoutDB";
  private static final String USER = "root";
  private static final String PASSWORD = "Fedexloca05&";

  // Costruttore privato per impedire l'istanza da fuori (Pattern Singleton)
  private DatabaseConnection() {}

  /**
   * Restituisce l'istanza unica della connessione al database. Se la connessione non esiste o è
   * chiusa, ne apre una nuova.
   */
  public static Connection getConnection() {
    try {
      if (connection == null || connection.isClosed()) {
        // Carica il driver (opzionale nelle versioni recenti di JDBC, ma buona pratica)
        Class.forName("com.mysql.cj.jdbc.Driver");

        // Stabilisce la connessione
        connection = DriverManager.getConnection(URL, USER, PASSWORD);
        System.out.println("[LOG] Connessione al database WorkoutDB stabilita con successo.");
      }
    } catch (ClassNotFoundException e) {
      System.err.println("[ERRORE] Driver JDBC non trovato. Controlla build.gradle.kts.");
      e.printStackTrace();
    } catch (SQLException e) {
      System.err.println(
          "[ERRORE] Impossibile connettersi al database. Controlla URL, USER e PASSWORD.");
      e.printStackTrace();
    }
    return connection;
  }

  /** Chiude la connessione al database in modo sicuro. */
  public static void closeConnection() {
    try {
      if (connection != null && !connection.isClosed()) {
        connection.close();
        System.out.println("[LOG] Connessione al database chiusa correttamente.");
      }
    } catch (SQLException e) {
      System.err.println("[ERRORE] Chiusura della connessione fallita.");
      e.printStackTrace();
    }
  }
}
