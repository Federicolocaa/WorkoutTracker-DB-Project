package it.unibo.workout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.unibo.workout.model.DatabaseConnection;
import it.unibo.workout.model.WorkoutDAOImpl;
import it.unibo.workout.model.api.WorkoutDAO;
import it.unibo.workout.model.dto.AttrezzaturaDTO;
import it.unibo.workout.model.dto.FeedbackDTO;
import it.unibo.workout.model.dto.PRAtletaDTO;
import it.unibo.workout.model.dto.PianificazioneDTO;
import it.unibo.workout.model.dto.RecordPersonaleDTO;
import it.unibo.workout.model.dto.ReportVolumeDTO;
import it.unibo.workout.model.dto.VarianteDTO;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.DisplayName.class)
class WorkoutDAOTest {

  private static WorkoutDAO dao;

  @BeforeAll
  static void setup() {
    assertNotNull(DatabaseConnection.getConnection(), "Connessione al database non disponibile.");
    dao = new WorkoutDAOImpl();
  }

  // =========================================================================
  // OP 2 — Creazione scheda incrementale
  // =========================================================================

  @Test
  @DisplayName("OP2 — Fase 1: creaSchedaIniziale ritorna ID positivo")
  void testCreaSchedaIniziale_success() {
    String nomeUnico = "Scheda JUnit " + System.currentTimeMillis();
    Optional<Integer> id = dao.creaSchedaIniziale(nomeUnico, "federico.locatelli@unibo.it");
    assertTrue(id.isPresent(), "Deve essere restituito un ID scheda");
    assertTrue(id.get() > 0, "L'ID scheda deve essere positivo");
  }

  @Test
  @DisplayName("OP2 — Fase 1: nome scheda duplicato fallisce")
  void testCreaSchedaIniziale_nomeDuplicato() {
    Optional<Integer> id =
        dao.creaSchedaIniziale("Powerlifting Base", "federico.locatelli@unibo.it");
    assertTrue(id.isEmpty(), "Un nome scheda già esistente deve fallire (UNIQUE constraint)");
  }

  @Test
  @DisplayName("OP2 — Fase 2: aggiungiGiornata ritorna ID positivo")
  void testAggiungiGiornata_success() {
    // Crea una scheda nuova per rendere il test ripetibile a ogni esecuzione
    String nome = "Scheda Giornata " + System.currentTimeMillis();
    int idScheda = dao.creaSchedaIniziale(nome, "federico.locatelli@unibo.it").orElseThrow();
    Optional<Integer> id = dao.aggiungiGiornata(idScheda, "Domenica", "Limbs");
    assertTrue(id.isPresent(), "Deve essere restituito un ID giornata");
    assertTrue(id.get() > 0, "L'ID giornata deve essere positivo");
  }

  @Test
  @DisplayName("OP2 — Fase 2: giorno+tipo duplicato nella stessa scheda fallisce")
  void testAggiungiGiornata_duplicato() {
    Optional<Integer> id = dao.aggiungiGiornata(1, "Lunedi", "Lower");
    assertTrue(id.isEmpty(), "Combinazione giorno+tipo già presente deve fallire");
  }

  @Test
  @DisplayName("OP2 — Fase 3: aggiungiEsercizio aggiunge alla pianificazione")
  void testAggiungiEsercizio_success() {
    // Crea scheda + giornata nuove per rendere il test ripetibile
    String nome = "Scheda Esercizio " + System.currentTimeMillis();
    int idScheda = dao.creaSchedaIniziale(nome, "federico.locatelli@unibo.it").orElseThrow();
    int idGiornata = dao.aggiungiGiornata(idScheda, "Lunedi", "Push").orElseThrow();
    boolean result = dao.aggiungiEsercizio(idGiornata, "Military Press", 1, 4, 8);
    assertTrue(result, "L'aggiunta dell'esercizio deve avere successo");
  }

  @Test
  @DisplayName("OP2 — Validazione: nomeSchedaEsistente corretto")
  void testNomeSchedaEsistente() {
    assertTrue(dao.nomeSchedaEsistente("Powerlifting Base"), "Powerlifting Base esiste nel DB");
    assertFalse(
        dao.nomeSchedaEsistente("SchedaCheNonEsiste999"),
        "Una scheda inesistente deve restituire false");
  }

  @Test
  @DisplayName("OP2 — Validazione: countGiornate corretto per scheda 1")
  void testCountGiornate_scheda1() {
    int count = dao.countGiornate(1);
    assertTrue(count >= 3, "La scheda 1 ha almeno 3 giornate");
    assertTrue(count <= 5, "Non si può superare il limite di 5 giornate");
  }

  @Test
  @DisplayName("OP2 — Validazione: countEsercizi corretto per giornata 1")
  void testCountEsercizi_giornata1() {
    int count = dao.countEsercizi(1);
    assertTrue(count >= 2, "La giornata 1 ha almeno 2 esercizi");
    assertTrue(count <= 6, "Non si può superare il limite di 6 esercizi");
  }

  @Test
  @DisplayName("OP2 — getSchedaAtleta ritorna dati per Federico")
  void testGetSchedaAtleta_federico() {
    List<PianificazioneDTO> scheda = dao.getSchedaAtleta("federico.locatelli@unibo.it");
    assertFalse(scheda.isEmpty(), "Federico deve avere una scheda");
    assertEquals("Powerlifting Base", scheda.get(0).nomeScheda());
    List<String> giornate =
        scheda.stream().map(PianificazioneDTO::giornatLabel).distinct().toList();
    assertTrue(giornate.size() >= 2, "Devono esserci almeno 2 giornate");
  }

  @Test
  @DisplayName("OP2 — getSchedaAtleta atleta senza scheda ritorna lista vuota")
  void testGetSchedaAtleta_senzaScheda() {
    List<PianificazioneDTO> scheda = dao.getSchedaAtleta("inesistente@unibo.it");
    assertTrue(scheda.isEmpty(), "Un atleta senza scheda ritorna lista vuota");
  }

  // =========================================================================
  // OP 3 — Sessione
  // =========================================================================

  @Test
  @DisplayName("OP3 — Inserimento sessione restituisce ID positivo")
  void testInserisciSessione_success() {
    Optional<Integer> id = dao.inserisciSessione(1, "federico.locatelli@unibo.it");
    assertTrue(id.isPresent(), "Deve essere restituito un ID sessione");
    assertTrue(id.get() > 0, "L'ID generato deve essere positivo");
  }

  // =========================================================================
  // OP 4 — Serie
  // =========================================================================

  @Test
  @DisplayName("OP4 — Registrazione working set su sessione esistente")
  void testRegistraSerie_workingSet() {
    boolean result = dao.registraSerie(99, 100.0, 5, false, null, false, null, 1, "Squat");
    assertTrue(result, "La registrazione della serie deve avere successo");
  }

  @Test
  @DisplayName("OP4 — Registrazione warm-up con note")
  void testRegistraSerie_warmUp() {
    boolean result =
        dao.registraSerie(1, 60.0, 10, true, null, null, "Riscaldamento test JUnit", 1, "Squat");
    assertTrue(result, "La registrazione del warm-up deve avere successo");
  }

  // =========================================================================
  // OP 5 — Record Personale
  // =========================================================================

  @Test
  @DisplayName("OP5 — PR esistente: Federico su Panca Piana = 120 kg")
  void testGetRecordPersonale_esistente() {
    Optional<RecordPersonaleDTO> pr =
        dao.getRecordPersonale("federico.locatelli@unibo.it", "Panca Piana");
    assertTrue(pr.isPresent(), "Il PR deve essere presente");
    assertEquals("Panca Piana", pr.get().nomeEsercizio());
    assertEquals(120.0, pr.get().caricoKG(), 0.01, "PR atteso: 120 kg");
    assertEquals(3, pr.get().reps(), "Reps al PR: 3");
    assertTrue(pr.get().volume() > 0, "Il volume deve essere positivo");
  }

  @Test
  @DisplayName("OP5 — PR assente per esercizio mai svolto")
  void testGetRecordPersonale_inesistente() {
    Optional<RecordPersonaleDTO> pr =
        dao.getRecordPersonale("federico.locatelli@unibo.it", "EsercizioCheNonEsiste");
    assertTrue(pr.isEmpty(), "Non deve esistere un PR per esercizio mai svolto");
  }

  // =========================================================================
  // OP 6 — Misurazione
  // =========================================================================

  @Test
  @DisplayName("OP6 — Inserimento misurazione biometrica completa")
  void testInserisciMisurazione_success() {
    boolean result = dao.inserisciMisurazione(79.5, 14.0, "federico.locatelli@unibo.it");
    assertTrue(result, "L'inserimento della misurazione deve avere successo");
  }

  @Test
  @DisplayName("OP6 — Inserimento misurazione senza percentuale grasso")
  void testInserisciMisurazione_senzaGrasso() {
    boolean result = dao.inserisciMisurazione(80.0, null, "luca.bianchi@unibo.it");
    assertTrue(result, "L'inserimento senza % grasso deve avere successo");
  }

  // =========================================================================
  // OP 7 — Feedback
  // =========================================================================

  @Test
  @DisplayName("OP7 — Invio feedback su sessione esistente")
  void testInviaFeedback_success() {
    boolean result = dao.inviaFeedback("Feedback di test JUnit!", "mario.rossi@unibo.it", 1);
    assertTrue(result, "L'invio del feedback deve avere successo");
  }

  // =========================================================================
  // OP 8 — Varianti
  // =========================================================================

  @Test
  @DisplayName("OP8 — Panca Piana ha 3 varianti definite")
  void testGetVarianti_pancaPiana() {
    List<VarianteDTO> varianti = dao.getVarianti("Panca Piana");
    assertFalse(varianti.isEmpty(), "Devono esistere varianti");
    assertEquals(3, varianti.size(), "La Panca Piana ha 3 varianti");
    List<String> nomi = varianti.stream().map(VarianteDTO::nomeEsercizio).toList();
    assertTrue(nomi.contains("Spinte con Manubri"));
    assertTrue(nomi.contains("Chest Press Guidata"));
    assertTrue(nomi.contains("Cable Crossover"));
  }

  @Test
  @DisplayName("OP8 — Esercizio senza varianti restituisce lista vuota")
  void testGetVarianti_nessuna() {
    List<VarianteDTO> varianti = dao.getVarianti("Curl con Bilanciere");
    assertTrue(varianti.isEmpty(), "Il Curl non ha varianti definite");
  }

  @Test
  @DisplayName("OP8 — VarianteDTO.isGuidato() corretto")
  void testVarianteDTO_isGuidato() {
    dao.getVarianti("Panca Piana")
        .forEach(
            v -> {
              if ("Chest Press Guidata".equals(v.nomeEsercizio())) {
                assertTrue(v.isGuidato());
              }
              if ("Spinte con Manubri".equals(v.nomeEsercizio())) {
                assertFalse(v.isGuidato());
              }
            });
  }

  // =========================================================================
  // OP 9 — Volume Mensile
  // =========================================================================

  @Test
  @DisplayName("OP9 — Volume mensile Petto per Federico è positivo")
  void testGetVolumeMensile_petto() {
    Optional<Double> volume = dao.getVolumeMensile("federico.locatelli@unibo.it", "Petto");
    assertTrue(volume.isPresent(), "Deve esserci volume per il Petto");
    assertTrue(volume.get() > 0, "Il volume deve essere positivo");
  }

  @Test
  @DisplayName("OP9 — Volume assente per muscolo non allenato")
  void testGetVolumeMensile_muscoloNonAllenato() {
    Optional<Double> volume =
        dao.getVolumeMensile("federico.locatelli@unibo.it", "MuSColoFalso999");
    assertTrue(volume.isEmpty(), "Muscolo inesistente: nessun volume");
  }

  // =========================================================================
  // OP 10 — Attrezzatura
  // =========================================================================

  @Test
  @DisplayName("OP10 — Pull Day (giornata 5) richiede Lat Machine Technogym")
  void testGetAttrezzatura_giornataCon() {
    List<AttrezzaturaDTO> attrezzature = dao.getAttrezzaturaGiornata(5);
    assertFalse(attrezzature.isEmpty(), "Pull Day richiede attrezzatura");
    assertEquals(1, attrezzature.size());
    assertEquals("Lat Machine", attrezzature.get(0).nomeAttrezzo());
    assertEquals("Technogym", attrezzature.get(0).marca());
  }

  @Test
  @DisplayName("OP10 — Squat Day (giornata 1) non richiede attrezzatura")
  void testGetAttrezzatura_giornataSenza() {
    List<AttrezzaturaDTO> attrezzature = dao.getAttrezzaturaGiornata(1);
    assertTrue(attrezzature.isEmpty(), "Squat Day: nessuna attrezzatura guidata");
  }

  // =========================================================================
  // OP 11 — Dashboard Coach
  // =========================================================================

  @Test
  @DisplayName("OP11 — PR atleti di Mario includono Federico e Luca")
  void testGetPRAtleti_mario() {
    List<PRAtletaDTO> prList = dao.getPRAtleti("mario.rossi@unibo.it");
    assertFalse(prList.isEmpty(), "Mario ha atleti con PR registrati");
    List<String> nomi = prList.stream().map(PRAtletaDTO::nomeCompleto).distinct().toList();
    assertTrue(nomi.contains("Federico Locatelli"));
    assertTrue(nomi.contains("Luca Bianchi"));
  }

  @Test
  @DisplayName("OP11 — Mario ha inviato almeno 3 feedback")
  void testGetFeedbackCoach_mario() {
    List<FeedbackDTO> feedbackList = dao.getFeedbackCoach("mario.rossi@unibo.it");
    assertFalse(feedbackList.isEmpty(), "Mario ha inviato feedback");
    assertTrue(feedbackList.size() >= 3, "Almeno 3 feedback attesi");
  }

  @Test
  @DisplayName("OP11 — Coach inesistente restituisce lista PR vuota")
  void testGetPRAtleti_coachSenzaAtleti() {
    List<PRAtletaDTO> prList = dao.getPRAtleti("nessuno@unibo.it");
    assertTrue(prList.isEmpty(), "Coach inesistente: nessun atleta");
  }

  // =========================================================================
  // OP 12 — Report Volume
  // =========================================================================

  @Test
  @DisplayName("OP12 — Report sessione 1 contiene Squat con serie reali")
  void testGetReportVolume_sessione1() {
    List<ReportVolumeDTO> report = dao.getReportVolume(1);
    assertFalse(report.isEmpty(), "La sessione 1 deve avere un report");
    assertTrue(report.stream().map(ReportVolumeDTO::nomeEsercizio).toList().contains("Squat"));
    report.stream()
        .filter(r -> "Squat".equals(r.nomeEsercizio()))
        .findFirst()
        .ifPresent(
            squat -> {
              assertEquals(5, squat.serieTarget());
              assertTrue(squat.serieReali() > 0);
              assertTrue(squat.volumeRealeKg() > 0);
              assertTrue(squat.serieRaggiunte());
            });
  }

  @Test
  @DisplayName("OP12 — Sessione inesistente restituisce lista vuota")
  void testGetReportVolume_sessioneInesistente() {
    assertTrue(dao.getReportVolume(99999).isEmpty(), "Sessione inesistente: report vuoto");
  }
}
