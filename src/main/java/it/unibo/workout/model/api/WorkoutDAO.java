package it.unibo.workout.model.api;

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
import java.util.List;
import java.util.Optional;

/**
 * Interfaccia che definisce le 12 operazioni principali del sistema di monitoraggio atletico.
 * Utilizza tipi forti (DTO, Optional, List) al posto di stringhe grezze per garantire type-safety e
 * leggibilità.
 */
public interface WorkoutDAO {

  // =========================================================================
  // Operazioni di scrittura — restituiscono boolean
  // =========================================================================

  /**
   * OP 2 — Creazione scheda incrementale (3 fasi separate) /** Fase 1: crea il record SCHEDE,
   * ritorna idScheda generato.
   */
  Optional<Integer> creaSchedaIniziale(String nomeScheda, String atletaEmail);

  /** Fase 2: aggiunge una giornata alla scheda, ritorna idGiornata generato. */
  Optional<Integer> aggiungiGiornata(int idScheda, String giorno, String tipo);

  /** Dettaglio completo di una giornata: giorno, tipo, atleta, esercizi in ordine. */
  Optional<GiornataDTO> getDettaglioGiornata(int idGiornata);

  /** Fase 3: aggiunge un esercizio alla giornata (pianificazione). */
  boolean aggiungiEsercizio(
      int idGiornata, String nomeEsercizio, int ordine, int serieTarget, int repsTarget);

  // =========================================================================
  // Validazione OP 2
  // =========================================================================

  /** Verifica se un nome scheda è già presente nel sistema. */
  boolean nomeSchedaEsistente(String nomeScheda);

  /** Conta le giornate già create per una scheda (max 5). */
  int countGiornate(int idScheda);

  /** Conta gli esercizi già pianificati in una giornata (max 6). */
  int countEsercizi(int idGiornata);

  // =========================================================================
  // Visualizzazione scheda atleta
  // =========================================================================

  /** Ritorna la struttura completa della scheda attiva dell'atleta. */
  List<PianificazioneDTO> getSchedaAtleta(String atletaEmail);

  /** Atleti supervisionati da un coach, ordinati per cognome. */
  List<AtletaDTO> getAtletiDelCoach(String coachEmail);

  /** Sessioni di un singolo atleta, dalla più recente. */
  List<SessioneRecenteDTO> getSessioniAtleta(String atletaEmail);

  /** OP 3 — Inserisce una nuova sessione, restituisce l'ID generato. */
  Optional<Integer> inserisciSessione(int idGiornata, String utenteEmail);

  /** OP 4 — Registra i dati di una singola serie. */
  boolean registraSerie(
      int ordineEsecuzione,
      double caricoKG,
      int reps,
      boolean isWarmUp,
      Double zavorra,
      Boolean isFailure,
      String noteWarmUp,
      int idSessione,
      String nomeEsercizio);

  /** Prossimo numero di serie per un esercizio nella sessione (1, 2, 3...). */
  int prossimoOrdineSerie(int idSessione, String nomeEsercizio);

  /** OP 6 — Inserisce una misurazione biometrica periodica. */
  boolean inserisciMisurazione(double valorePeso, Double percentualeGrasso, String utenteEmail);

  /** OP 7 — Invia un feedback tecnico dal Coach su una sessione. */
  boolean inviaFeedback(String testo, String autoreEmail, int idSessione);

  // =========================================================================
  // Operazioni di lettura — restituiscono Optional o List
  // =========================================================================

  /** OP 5 — Recupera il Record Personale su un esercizio. */
  Optional<RecordPersonaleDTO> getRecordPersonale(String utenteEmail, String nomeEsercizio);

  /** OP 8 — Recupera gli esercizi varianti tramite relazione ricorsiva. */
  List<VarianteDTO> getVarianti(String nomeEsercizio);

  /** OP 9 — Calcola il volume totale mensile per gruppo muscolare. */
  Optional<Double> getVolumeMensile(String utenteEmail, String nomeMuscolo);

  /** OP 10 — Recupera l'attrezzatura necessaria per una giornata. */
  List<AttrezzaturaDTO> getAttrezzaturaGiornata(int idGiornata);

  /** OP 11 (parte 1) — PR di tutti gli atleti supervisionati dal coach. */
  List<PRAtletaDTO> getPRAtleti(String coachEmail);

  /** OP 11 (parte 2) — Feedback inviati dal coach con contesto sessione. */
  List<FeedbackDTO> getFeedbackCoach(String coachEmail);

  /**
   * Restituisce le sessioni completate dagli atleti supervisionati da un coach, ordinate dalla più
   * recente. Usato in OP 11 per fornire gli ID sessione a OP 7.
   */
  List<SessioneRecenteDTO> getSessioniRecentiAtleti(String coachEmail);

  /** OP 12 — Report confronto volume teorico vs reale per una sessione. */
  List<ReportVolumeDTO> getReportVolume(int idSessione);

  /** Imposta il coach come supervisore dell'atleta (collega l'atleta al coach). */
  boolean assegnaSupervisore(String atletaEmail, String coachEmail);
}
