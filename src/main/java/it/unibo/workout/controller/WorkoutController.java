package it.unibo.workout.controller;

import it.unibo.workout.model.WorkoutDAOImpl;
import it.unibo.workout.model.api.WorkoutDAO;
import it.unibo.workout.model.dto.AtletaDTO;
import it.unibo.workout.model.dto.GiornataDTO;
import it.unibo.workout.model.dto.PianificazioneDTO;
import it.unibo.workout.model.dto.SessioneRecenteDTO;
import it.unibo.workout.model.dto.UtenteDTO;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller principale per le operazioni di workout. Utilizza stream, lambda e method reference
 * per elaborare i DTO restituiti dal DAO e formattarli per la view.
 */
public class WorkoutController {

  private final WorkoutDAO dao;
  private final UtenteDTO utenteCorrente;
  private Integer idSchedaInProgress = null;
  private Integer idGiornataInProgress = null;

  public WorkoutController(UtenteDTO utenteCorrente) {
    this.dao = new WorkoutDAOImpl();
    this.utenteCorrente = utenteCorrente;
  }

  /** Restituisce l'utente attualmente loggato. */
  public UtenteDTO getUtenteCorrente() {
    return utenteCorrente;
  }

  // =========================================================================
  // OP 2 — Fase 1: crea la scheda
  // =========================================================================
  public String creaSchedaIniziale(String nomeScheda, String atletaEmail) {
    if (nomeScheda.isBlank()) return "[ERRORE] Il nome scheda non può essere vuoto.";
    if (dao.nomeSchedaEsistente(nomeScheda))
      return "[ERRORE] Nome già esistente. Scegline un altro.";

    return dao.creaSchedaIniziale(nomeScheda, atletaEmail)
        .map(
            id -> {
              idSchedaInProgress = id;
              return "[OK] Scheda \""
                  + nomeScheda
                  + "\" creata (ID: "
                  + id
                  + ")."
                  + "\nOra aggiungi le giornate.";
            })
        .orElse("[ERRORE] Impossibile creare la scheda.");
  }

  // =========================================================================
  // OP 2 — Fase 2: aggiunge una giornata
  // =========================================================================
  public String aggiungiGiornata(String giorno, String tipo) {
    if (idSchedaInProgress == null) return "[ERRORE] Devi prima creare una scheda.";

    if (dao.countGiornate(idSchedaInProgress) >= 5)
      return "[ERRORE] Limite raggiunto: massimo 5 giornate per scheda.";

    return dao.aggiungiGiornata(idSchedaInProgress, giorno, tipo)
        .map(
            id -> {
              idGiornataInProgress = id;
              return "[OK] "
                  + giorno
                  + " — "
                  + tipo
                  + " aggiunta (ID giornata: "
                  + id
                  + ")."
                  + "\nOra aggiungi gli esercizi.";
            })
        .orElse(
            "[ERRORE] Combinazione " + giorno + " — " + tipo + " già presente in questa scheda.");
  }

  // =========================================================================
  // OP 2 — Fase 3: aggiunge un esercizio alla giornata attiva
  // =========================================================================
  public String aggiungiEsercizio(
      String nomeEsercizio, int ordine, int serieTarget, int repsTarget) {
    if (idGiornataInProgress == null) return "[ERRORE] Devi prima aggiungere una giornata.";

    boolean ok =
        dao.aggiungiEsercizio(idGiornataInProgress, nomeEsercizio, ordine, serieTarget, repsTarget);
    return ok
        ? "[OK] "
            + nomeEsercizio
            + " — "
            + serieTarget
            + "×"
            + repsTarget
            + " (ordine "
            + ordine
            + ") salvato."
        : "[ERRORE] Impossibile salvare l'esercizio.";
  }

  // =========================================================================
  // OP 2 — Cambia giornata attiva (torna su una giornata esistente)
  // =========================================================================
  public Optional<GiornataDTO> selezionaGiornata(int idGiornata) {
    Optional<GiornataDTO> g = dao.getDettaglioGiornata(idGiornata);
    g.ifPresent(dto -> idGiornataInProgress = idGiornata);
    return g;
  }

  /** Dettaglio di una giornata (sola lettura), usato dalla view per pre-impostare OP 4. */
  public Optional<GiornataDTO> getDettaglioGiornata(int idGiornata) {
    return dao.getDettaglioGiornata(idGiornata);
  }

  // Getter usati dalla view
  public Integer getIdSchedaInProgress() {
    return idSchedaInProgress;
  }

  public Integer getIdGiornataInProgress() {
    return idGiornataInProgress;
  }

  // =========================================================================
  // Visualizzazione scheda (atleta vede la propria, coach può passare email)
  // =========================================================================
  public String getSchedaAtleta(String atletaEmail) {
    List<PianificazioneDTO> righe = dao.getSchedaAtleta(atletaEmail);

    if (righe.isEmpty()) return "Nessuna scheda trovata per: " + atletaEmail;

    var grouped =
        righe.stream()
            .collect(
                Collectors.groupingBy(
                    PianificazioneDTO::giornatLabel, LinkedHashMap::new, Collectors.toList()));

    StringBuilder sb = new StringBuilder();
    sb.append("=== SCHEDA: ").append(righe.get(0).nomeScheda()).append(" ===\n\n");

    grouped.forEach(
        (giornata, esercizi) -> {
          sb.append("📅 ").append(giornata).append("\n");
          esercizi.forEach(e -> sb.append(e.label()).append("\n"));
          sb.append("\n");
        });

    return sb.toString();
  }

  // Shortcut per l'atleta loggato
  public String getSchedaAtleta() {
    return getSchedaAtleta(utenteCorrente.email());
  }

  // =========================================================================
  // OP 3 — Inserisci sessione
  // =========================================================================
  public Optional<Integer> inserisciSessione(int idGiornata) {
    return dao.inserisciSessione(idGiornata, utenteCorrente.email());
  }

  // =========================================================================
  // OP 4 — Registra serie
  // =========================================================================
  public String registraSerie(
      int ordine,
      double caricoKG,
      int reps,
      boolean isWarmUp,
      Double zavorra,
      Boolean isFailure,
      String noteWarmUp,
      int idSessione,
      String nomeEsercizio) {
    boolean ok =
        dao.registraSerie(
            ordine,
            caricoKG,
            reps,
            isWarmUp,
            zavorra,
            isFailure,
            noteWarmUp,
            idSessione,
            nomeEsercizio);
    String tipo = isWarmUp ? "riscaldamento" : "working set";
    return ok
        ? "[OK] Serie di " + tipo + " registrata (%.2f kg × %d reps).".formatted(caricoKG, reps)
        : "[ERRORE] Registrazione serie fallita.";
  }

  // =========================================================================
  // OP 5 — Record Personale
  // =========================================================================
  public String getRecordPersonale(String nomeEsercizio) {
    return dao.getRecordPersonale(utenteCorrente.email(), nomeEsercizio)
        .map(pr -> "🏆 PR — " + pr.descrizione())
        .orElse("Nessun record trovato per: " + nomeEsercizio);
  }

  // =========================================================================
  // OP 6 — Misurazione biometrica
  // =========================================================================
  public String inserisciMisurazione(double valorePeso, Double percentualeGrasso) {
    boolean ok = dao.inserisciMisurazione(valorePeso, percentualeGrasso, utenteCorrente.email());
    return ok
        ? "[OK] Misurazione registrata (%.2f kg).".formatted(valorePeso)
        : "[ERRORE] Inserimento misurazione fallito.";
  }

  // =========================================================================
  // OP 7 — Feedback Coach
  // =========================================================================
  public String inviaFeedback(String testo, int idSessione) {
    boolean ok = dao.inviaFeedback(testo, utenteCorrente.email(), idSessione);
    return ok
        ? "[OK] Feedback inviato alla sessione #" + idSessione + "."
        : "[ERRORE] Invio feedback fallito.";
  }

  // =========================================================================
  // OP 8 — Varianti esercizio (stream + method reference)
  // =========================================================================
  public String getVarianti(String nomeEsercizio) {
    List<String> varianti =
        dao.getVarianti(nomeEsercizio).stream().map(v -> v.label()).collect(Collectors.toList());

    if (varianti.isEmpty()) {
      return "Nessuna variante trovata per: " + nomeEsercizio;
    }

    return "Varianti disponibili per \""
        + nomeEsercizio
        + "\":\n"
        + varianti.stream().collect(Collectors.joining("\n"));
  }

  // =========================================================================
  // OP 9 — Volume mensile (Optional + lambda)
  // =========================================================================
  public String getVolumeMensile(String nomeMuscolo) {
    return dao.getVolumeMensile(utenteCorrente.email(), nomeMuscolo)
        .map(v -> "Volume mensile \"%s\": %.2f kg totali.".formatted(nomeMuscolo, v))
        .orElse("Nessun dato questo mese per: " + nomeMuscolo);
  }

  // =========================================================================
  // OP 10 — Attrezzatura giornata (stream + method reference)
  // =========================================================================
  public String getAttrezzaturaGiornata(int idGiornata) {
    List<String> attrezzi =
        dao.getAttrezzaturaGiornata(idGiornata).stream()
            .map(a -> a.label())
            .collect(Collectors.toList());

    if (attrezzi.isEmpty()) {
      return "Nessuna attrezzatura specifica richiesta per la giornata " + idGiornata + ".";
    }

    return "Attrezzatura necessaria (giornata "
        + idGiornata
        + "):\n"
        + attrezzi.stream().collect(Collectors.joining("\n"));
  }

  // =========================================================================
  // OP 11 — Dashboard Coach (PR atleti + sessioni recenti con ID)
  // =========================================================================
  public String getDashboardCoach() {
    var prList = dao.getPRAtleti(utenteCorrente.email());
    List<SessioneRecenteDTO> sessioni = dao.getSessioniRecentiAtleti(utenteCorrente.email());

    if (prList.isEmpty() && sessioni.isEmpty()) return "Nessun dato atleta disponibile al momento.";

    StringBuilder sb = new StringBuilder("=== DASHBOARD COACH ===\n\n");

    // Sezione 1 — Record personali
    sb.append("🏆 RECORD PERSONALI ATLETI\n");
    if (prList.isEmpty()) {
      sb.append("  (nessun PR registrato)\n");
    } else {
      prList.forEach(
          pr ->
              sb.append("  ")
                  .append(pr.nomeCompleto())
                  .append(" — ")
                  .append(pr.nomeEsercizio())
                  .append(": ")
                  .append(pr.pr())
                  .append(" kg\n"));
    }

    // Sezione 2 — Sessioni recenti (con ID per OP7)
    sb.append("\n📋 SESSIONI RECENTI (usa l'ID per inviare feedback in OP 7)\n");
    if (sessioni.isEmpty()) {
      sb.append("  (nessuna sessione registrata)\n");
    } else {
      sessioni.forEach(s -> sb.append("  ").append(s.label()).append("\n"));
    }

    return sb.toString();
  }

  // =========================================================================
  // Elenco atleti supervisionati dal coach
  // =========================================================================
  public String getElencoAtleti() {
    var atleti = dao.getAtletiDelCoach(utenteCorrente.email());
    if (atleti.isEmpty()) return "Nessun atleta supervisionato al momento.";

    StringBuilder sb = new StringBuilder("=== I MIEI ATLETI ===\n\n");
    atleti.forEach(a -> sb.append("• ").append(a.label()).append("\n"));
    return sb.toString();
  }

  /** Lista grezza degli atleti del coach (per la tendina del feedback). */
  public List<AtletaDTO> getAtleti() {
    return dao.getAtletiDelCoach(utenteCorrente.email());
  }

  /** Sessioni di un singolo atleta (per la tendina del feedback). */
  public List<SessioneRecenteDTO> getSessioniAtleta(String atletaEmail) {
    return dao.getSessioniAtleta(atletaEmail);
  }

  // =========================================================================
  // OP 12 — Report volume (stream + method reference)
  // =========================================================================
  public String getReportVolume(int idSessione) {
    var righe = dao.getReportVolume(idSessione);

    if (righe.isEmpty()) {
      return "Nessun dato per la sessione #" + idSessione + ".";
    }

    String header =
        "=== REPORT SESSIONE #%d ===\n".formatted(idSessione)
            + "%s %-22s %6s %6s %6s %6s %12s\n"
                .formatted(" ", "Esercizio", "STgt", "RTgt", "SReal", "RReal", "Vol.Reale");
    String separatore = "─".repeat(72) + "\n";

    String corpo = righe.stream().map(r -> r.label()).collect(Collectors.joining("\n"));

    double totale = righe.stream().mapToDouble(r -> r.volumeTotaleSessione()).findFirst().orElse(0);

    return header
        + separatore
        + corpo
        + "\n"
        + separatore
        + "Volume totale sessione: %.2f kg".formatted(totale);
  }
}
