package it.unibo.workout.view;

import it.unibo.workout.controller.WorkoutController;
import it.unibo.workout.model.DatabaseConnection;
import it.unibo.workout.model.dto.AtletaDTO;
import it.unibo.workout.model.dto.SessioneRecenteDTO;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import java.util.stream.IntStream;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class MainView {

  private static final Color GREEN_ACC = new Color(0x4CD964);

  private static final String[] ESERCIZI_DB = {
    "Panca Piana",
    "Spinte con Manubri",
    "Chest Press Guidata",
    "Incline Chest Press",
    "Croci con Manubri",
    "Pec Fly Machine",
    "Cable Crossover",
    "Low-High Cable Fly",
    "Dips",
    "Military Press",
    "Shoulder Press Panatta",
    "Alzate Laterali",
    "Cable Lateral Raise",
    "Tricep Pushdown",
    "French Press",
    "Stacco da Terra",
    "Rematore con Bilanciere",
    "T-Bar Row",
    "High Row",
    "Lat Pulldown",
    "Prone Lat Machine",
    "Pulley Basso",
    "Prone Low-Pulley",
    "Trazioni",
    "Face Pull",
    "Mono Rear Delt",
    "Curl con Bilanciere",
    "Curl con Manubri",
    "Preacher Curl Cable",
    "Hammer Curl",
    "Wrist Curl",
    "Squat",
    "Hack Squat",
    "Leg Press",
    "Romanian Deadlift",
    "Affondi",
    "Leg Curl",
    "Leg Extension",
    "Adductor Machine",
    "Hip Thrust",
    "Calf Raise"
  };

  private static final String[] MUSCOLI_DB = {
    "Petto", "Dorso", "Gambe", "Spalle", "Tricipiti", "Bicipiti", "Glutei", "Core", "Trapezio"
  };

  private static final Map<String, String[]> ESERCIZI_PER_TIPO =
      Map.of(
          "Push",
          new String[] {
            "Panca Piana", "Spinte con Manubri", "Chest Press Guidata", "Incline Chest Press",
            "Croci con Manubri", "Pec Fly Machine", "Cable Crossover", "Low-High Cable Fly",
            "Dips", "Military Press", "Shoulder Press Panatta", "Alzate Laterali",
            "Cable Lateral Raise", "Tricep Pushdown", "French Press"
          },
          "Pull",
          new String[] {
            "Stacco da Terra", "Rematore con Bilanciere", "T-Bar Row", "High Row",
            "Lat Pulldown", "Prone Lat Machine", "Pulley Basso", "Prone Low-Pulley",
            "Trazioni", "Face Pull", "Mono Rear Delt", "Curl con Bilanciere",
            "Curl con Manubri", "Preacher Curl Cable", "Hammer Curl"
          },
          "Leg",
          new String[] {
            "Squat", "Hack Squat", "Leg Press", "Romanian Deadlift", "Affondi",
            "Leg Curl", "Leg Extension", "Adductor Machine", "Hip Thrust", "Calf Raise"
          },
          "Lower",
          new String[] {
            "Squat", "Hack Squat", "Leg Press", "Romanian Deadlift", "Affondi",
            "Leg Curl", "Leg Extension", "Adductor Machine", "Hip Thrust", "Calf Raise"
          },
          "Upper",
          new String[] {
            "Panca Piana", "Spinte con Manubri", "Chest Press Guidata", "Incline Chest Press",
            "Croci con Manubri", "Pec Fly Machine", "Cable Crossover", "Low-High Cable Fly",
            "Dips", "Military Press", "Shoulder Press Panatta", "Alzate Laterali",
            "Cable Lateral Raise", "Tricep Pushdown", "French Press", "Stacco da Terra",
            "Rematore con Bilanciere", "T-Bar Row", "High Row", "Lat Pulldown",
            "Prone Lat Machine", "Pulley Basso", "Prone Low-Pulley", "Trazioni",
            "Face Pull", "Mono Rear Delt", "Curl con Bilanciere", "Curl con Manubri",
            "Preacher Curl Cable", "Hammer Curl", "Wrist Curl"
          },
          "Torso",
          new String[] {
            "Panca Piana", "Spinte con Manubri", "Chest Press Guidata", "Incline Chest Press",
            "Croci con Manubri", "Pec Fly Machine", "Cable Crossover", "Low-High Cable Fly",
            "Dips", "Military Press", "Shoulder Press Panatta", "Alzate Laterali",
            "Cable Lateral Raise", "Stacco da Terra", "Rematore con Bilanciere", "T-Bar Row",
            "High Row", "Lat Pulldown", "Prone Lat Machine", "Pulley Basso",
            "Prone Low-Pulley", "Trazioni", "Face Pull", "Mono Rear Delt"
          },
          "Limbs",
          new String[] {
            "Curl con Bilanciere", "Curl con Manubri", "Preacher Curl Cable", "Hammer Curl",
            "Wrist Curl", "Tricep Pushdown", "French Press", "Dips",
            "Leg Curl", "Leg Extension", "Calf Raise", "Affondi",
            "Hack Squat", "Adductor Machine"
          },
          "Full Body",
          ESERCIZI_DB);

  private Integer idSessioneAttiva = null;
  private String atletaEmailPerScheda = null;

  private final WorkoutController controller;
  private JFrame frame;
  private CardLayout cardLayout;
  private JPanel cardsPanel;
  private JTextArea resultArea;
  private JTextArea schedaArea;
  private JButton activeButton = null;
  private JLabel lblSessione;
  private JComboBox<String> cmbEsercOP2;
  private JComboBox<String> cmbEsercOP4;
  private JComboBox<String> cmbOrdineOP2;
  private List<SessioneRecenteDTO> sessioniOP7 = new ArrayList<>();
  private final Map<String, JButton> navButtons = new HashMap<>();

  public MainView(WorkoutController controller) {
    this.controller = controller;
  }

  public void avvia() {
    SwingUtilities.invokeLater(
        () -> {
          frame = new JFrame("Fitness Tracker — " + controller.getUtenteCorrente().nomeCompleto());
          frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
          frame.addWindowListener(
              new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                  DatabaseConnection.closeConnection();
                  System.exit(0);
                }
              });
          frame.setLayout(new BorderLayout());
          frame.add(buildHeader(), BorderLayout.NORTH);
          frame.add(buildSidebar(), BorderLayout.WEST);
          frame.add(buildMainPanel(), BorderLayout.CENTER);
          frame.setSize(1100, 750);
          frame.setMinimumSize(new Dimension(950, 650));
          frame.setLocationRelativeTo(null);
          frame.setVisible(true);
        });
  }

  @SuppressWarnings("CallToPrintStackTrace")
  private JPanel buildHeader() {
    JPanel header = new JPanel(new BorderLayout());
    header.setBackground(StyleUtil.BG_DARK);
    header.setPreferredSize(new Dimension(0, 60));
    header.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

    JLabel title = new JLabel("⚡ FITNESS TRACKER");
    title.setFont(StyleUtil.FONT_TITLE);
    title.setForeground(StyleUtil.YELLOW_ACC);

    var utente = controller.getUtenteCorrente();
    JLabel userInfo = new JLabel(utente.ruoloLabel() + "   " + utente.nomeCompleto());
    userInfo.setFont(StyleUtil.FONT_LABEL);
    userInfo.setForeground(StyleUtil.TEXT_MAIN);

    lblSessione = new JLabel();
    lblSessione.setFont(StyleUtil.FONT_LABEL);
    aggiornaLabelSessione();

    JButton btnLogout =
        StyleUtil.createRoundedButton("Logout", StyleUtil.RED_ACC, StyleUtil.TEXT_MAIN);
    btnLogout.addActionListener(
        e -> {
          try {
            Preferences.userNodeForPackage(LoginView.class).clear();
          } catch (BackingStoreException e1) {
            e1.printStackTrace();
          }
          frame.dispose();
          new LoginView().avvia();
        });

    JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
    right.setBackground(StyleUtil.BG_DARK);
    if (utente.isAtleta()) {
      right.add(lblSessione);
    }
    right.add(userInfo);
    right.add(btnLogout);

    header.add(title, BorderLayout.WEST);
    header.add(right, BorderLayout.EAST);
    return header;
  }

  private JPanel buildSidebar() {
    JPanel sidebar = new JPanel();
    sidebar.setBackground(StyleUtil.PANEL_DARK);
    sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
    sidebar.setPreferredSize(new Dimension(240, 0));
    sidebar.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

    JLabel lblSec = new JLabel("OPERAZIONI");
    lblSec.setFont(StyleUtil.FONT_LABEL);
    lblSec.setForeground(StyleUtil.TEXT_MUTED);
    lblSec.setBorder(BorderFactory.createEmptyBorder(0, 5, 15, 0));
    sidebar.add(lblSec);

    var utente = controller.getUtenteCorrente();
    if (utente.isAtleta()) {
      addNavBtn(sidebar, "scheda", "📋 La Mia Scheda");
      addNavBtn(sidebar, "op3", "OP 03 — Nuova Sessione");
      addNavBtn(sidebar, "op4", "OP 04 — Registra Serie");
      addNavBtn(sidebar, "op5", "OP 05 — Record Personale");
      addNavBtn(sidebar, "op6", "OP 06 — Misurazione");
      addNavBtn(sidebar, "op8", "OP 08 — Varianti");
      addNavBtn(sidebar, "op9", "OP 09 — Volume Mensile");
      addNavBtn(sidebar, "op10", "OP 10 — Attrezzatura");
      addNavBtn(sidebar, "op12", "OP 12 — Report Volume");
    } else {
      addNavBtn(sidebar, "atleti", "👥 I Miei Atleti");
      addNavBtn(sidebar, "op2", "OP 02 — Crea Scheda");
      addNavBtn(sidebar, "op7", "OP 07 — Invia Feedback");
      addNavBtn(sidebar, "op11", "OP 11 — Dashboard Coach");
    }

    sidebar.add(Box.createVerticalGlue());
    return sidebar;
  }

  private void addNavBtn(JPanel sidebar, String cardName, String text) {
    JButton btn = StyleUtil.createRoundedButton(text, StyleUtil.BG_DARK, StyleUtil.TEXT_MAIN);
    btn.setHorizontalAlignment(SwingConstants.LEFT);
    btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
    btn.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseEntered(MouseEvent e) {
            if (btn != activeButton) btn.setForeground(StyleUtil.YELLOW_ACC);
          }

          @Override
          public void mouseExited(MouseEvent e) {
            if (btn != activeButton) btn.setForeground(StyleUtil.TEXT_MAIN);
          }
        });
    btn.addActionListener(
        e -> {
          navigaA(cardName);
          resultArea.setText("");
          if ("scheda".equals(cardName)) {
            aggiornaSchedaArea();
          }
        });
    navButtons.put(cardName, btn);
    sidebar.add(btn);
    sidebar.add(Box.createVerticalStrut(5));
  }

  /** Cambia card e aggiorna l'evidenziazione del pulsante nella sidebar. */
  private void navigaA(String cardName) {
    JButton target = navButtons.get(cardName);
    if (target != null) {
      if (activeButton != null) activeButton.setForeground(StyleUtil.TEXT_MAIN);
      activeButton = target;
      activeButton.setForeground(StyleUtil.YELLOW_ACC);
    }
    cardLayout.show(cardsPanel, cardName);
  }

  private JPanel buildMainPanel() {
    JPanel main = new JPanel(new BorderLayout());

    cardLayout = new CardLayout();
    cardsPanel = new JPanel(cardLayout);
    cardsPanel.setBackground(StyleUtil.BG_DARK);

    cardsPanel.add(buildWelcomePanel(), "welcome");
    cardsPanel.add(buildOP2Panel(), "op2");
    cardsPanel.add(buildAtletiPanel(), "atleti");
    cardsPanel.add(buildSchedaPanel(), "scheda");
    cardsPanel.add(buildOP3Panel(), "op3");
    cardsPanel.add(buildOP4Panel(), "op4");
    cardsPanel.add(buildOP5Panel(), "op5");
    cardsPanel.add(buildOP6Panel(), "op6");
    cardsPanel.add(buildOP7Panel(), "op7");
    cardsPanel.add(buildOP8Panel(), "op8");
    cardsPanel.add(buildOP9Panel(), "op9");
    cardsPanel.add(buildOP10Panel(), "op10");
    cardsPanel.add(buildOP11Panel(), "op11");
    cardsPanel.add(buildOP12Panel(), "op12");

    resultArea = new JTextArea(8, 0);
    resultArea.setEditable(false);
    resultArea.setBackground(StyleUtil.PANEL_DARK);
    resultArea.setForeground(StyleUtil.YELLOW_ACC);
    resultArea.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

    JPanel resultPanel = new JPanel(new BorderLayout());
    resultPanel.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, StyleUtil.YELLOW_ACC));
    resultPanel.add(new JScrollPane(resultArea), BorderLayout.CENTER);
    resultPanel.setPreferredSize(new Dimension(0, 200));

    main.add(cardsPanel, BorderLayout.CENTER);
    main.add(resultPanel, BorderLayout.SOUTH);
    return main;
  }

  private JPanel buildWelcomePanel() {
    JPanel p = new JPanel(new GridBagLayout());
    p.setBackground(StyleUtil.BG_DARK);
    var u = controller.getUtenteCorrente();
    String suggerimento =
        u.isAtleta()
            ? "Inizia da <b>OP 03</b> per avviare una sessione, poi registra le serie in OP 04."
            : "Inizia da <b>OP 02</b> per creare una scheda, o apri la <b>Dashboard</b> in OP 11.";
    JLabel lbl =
        new JLabel(
            "<html><center><h1 style='color:#FFCC00;'>⚡ Benvenuto, "
                + u.nome()
                + "!</h1>"
                + "<p style='color:#A0A0A0;'>"
                + u.ruoloLabel()
                + "</p>"
                + "<p style='color:#A0A0A0; width:400px;'>"
                + suggerimento
                + "</p></center></html>");
    p.add(lbl);
    return p;
  }

  private GridBagConstraints defaultGBC() {
    GridBagConstraints g = new GridBagConstraints();
    g.insets = new Insets(8, 10, 8, 10);
    g.anchor = GridBagConstraints.WEST;
    return g;
  }

  private JTextField addField(JPanel p, GridBagConstraints g, int row, String label, String hint) {
    g.gridx = 0;
    g.gridy = row;
    g.weightx = 0;
    g.fill = GridBagConstraints.NONE;
    JLabel lbl = new JLabel(label + (hint.isEmpty() ? "" : " " + hint));
    lbl.setFont(StyleUtil.FONT_LABEL);
    lbl.setForeground(StyleUtil.TEXT_MUTED);
    p.add(lbl, g);
    JTextField f = new JTextField(20);
    f.setPreferredSize(new Dimension(300, 35));
    f.setFont(StyleUtil.FONT_INPUT);
    f.setBackground(StyleUtil.PANEL_DARK);
    f.setForeground(StyleUtil.TEXT_MAIN);
    StyleUtil.applyRoundedBorder(f);
    g.gridx = 1;
    g.weightx = 1.0;
    g.fill = GridBagConstraints.HORIZONTAL;
    p.add(f, g);
    return f;
  }

  private JTextField addFieldWithHelp(
      JPanel p, GridBagConstraints g, int row, String label, String helpMsg) {
    g.gridx = 0;
    g.gridy = row;
    g.weightx = 0;
    g.fill = GridBagConstraints.NONE;

    JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
    labelPanel.setBackground(StyleUtil.BG_DARK);
    JLabel lbl = new JLabel(label);
    lbl.setFont(StyleUtil.FONT_LABEL);
    lbl.setForeground(StyleUtil.TEXT_MUTED);
    labelPanel.add(lbl);

    JButton btnHelp =
        StyleUtil.createCircularIconButton("?", StyleUtil.PANEL_DARK, StyleUtil.YELLOW_ACC);
    btnHelp.setPreferredSize(new Dimension(24, 24));
    btnHelp.addActionListener(
        e ->
            JOptionPane.showMessageDialog(
                frame, helpMsg, "Cosa devo inserire?", JOptionPane.INFORMATION_MESSAGE));
    labelPanel.add(btnHelp);

    p.add(labelPanel, g);

    JTextField f = new JTextField(20);
    f.setPreferredSize(new Dimension(300, 35));
    f.setFont(StyleUtil.FONT_INPUT);
    f.setBackground(StyleUtil.PANEL_DARK);
    f.setForeground(StyleUtil.TEXT_MAIN);
    StyleUtil.applyRoundedBorder(f);
    g.gridx = 1;
    g.weightx = 1.0;
    g.fill = GridBagConstraints.HORIZONTAL;
    p.add(f, g);
    return f;
  }

  private JComboBox<String> addComboField(
      JPanel p, GridBagConstraints g, int row, String label, String[] items, boolean editable) {
    g.gridx = 0;
    g.gridy = row;
    g.weightx = 0;
    g.fill = GridBagConstraints.NONE;
    JLabel lbl = new JLabel(label);
    lbl.setFont(StyleUtil.FONT_LABEL);
    lbl.setForeground(StyleUtil.TEXT_MUTED);
    p.add(lbl, g);
    JComboBox<String> combo = new JComboBox<>(items);
    combo.setEditable(editable);
    combo.setPreferredSize(new Dimension(300, 35));
    combo.setFont(StyleUtil.FONT_INPUT);
    g.gridx = 1;
    g.weightx = 1.0;
    g.fill = GridBagConstraints.HORIZONTAL;
    p.add(combo, g);
    return combo;
  }

  private JPanel wrapForm(String opTitle, JPanel form, JButton btn, String titleHelpMsg) {
    JPanel outer = new JPanel(new BorderLayout(0, 15));
    outer.setBackground(StyleUtil.BG_DARK);
    outer.setBorder(BorderFactory.createEmptyBorder(25, 30, 20, 30));

    JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
    titlePanel.setBackground(StyleUtil.BG_DARK);
    JLabel title = new JLabel(opTitle);
    title.setFont(StyleUtil.FONT_TITLE);
    title.setForeground(StyleUtil.YELLOW_ACC);
    titlePanel.add(title);

    if (titleHelpMsg != null && !titleHelpMsg.isEmpty()) {
      JButton btnHelp =
          StyleUtil.createCircularIconButton("i", StyleUtil.PANEL_DARK, StyleUtil.TEXT_MAIN);
      btnHelp.setPreferredSize(new Dimension(28, 28));
      btnHelp.addActionListener(
          e ->
              JOptionPane.showMessageDialog(
                  frame, titleHelpMsg, "Guida all'Operazione", JOptionPane.INFORMATION_MESSAGE));
      titlePanel.add(btnHelp);
    }

    form.setBackground(StyleUtil.BG_DARK);
    JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
    btnRow.setBackground(StyleUtil.BG_DARK);
    btnRow.add(btn);
    btn.setPreferredSize(new Dimension(150, 40));

    outer.add(titlePanel, BorderLayout.NORTH);
    outer.add(new JScrollPane(form), BorderLayout.CENTER);
    outer.add(btnRow, BorderLayout.SOUTH);
    return outer;
  }

  // ===========================================================================
  // Helper — feedback all'utente
  // ===========================================================================

  /** Mostra il risultato colorato: verde = OK, rosso = errore, giallo = info. */
  private void showResult(String text) {
    if (text.startsWith("[ERRORE]")) {
      resultArea.setForeground(StyleUtil.RED_ACC);
    } else if (text.startsWith("[OK]")) {
      resultArea.setForeground(GREEN_ACC);
    } else {
      resultArea.setForeground(StyleUtil.YELLOW_ACC);
    }
    resultArea.setText(text);
    resultArea.setCaretPosition(0);
  }

  /** Aggiorna la tendina esercizi della sezione ③ in base al tipo di giornata scelto. */
  private void aggiornaEserciziPerTipo(String tipo) {
    if (cmbEsercOP2 == null) return;
    cmbEsercOP2.setModel(
        new DefaultComboBoxModel<>(ESERCIZI_PER_TIPO.getOrDefault(tipo, ESERCIZI_DB)));
  }

  /** Pre-imposta in OP 4 i soli esercizi pianificati per la giornata avviata, in ordine. */
  private void impostaEserciziOP4(int idGiornata) {
    if (cmbEsercOP4 == null) return;
    controller
        .getDettaglioGiornata(idGiornata)
        .ifPresent(
            gdto -> {
              String[] esercizi =
                  gdto.esercizi().stream().map(p -> p.nomeEsercizio()).toArray(String[]::new);
              if (esercizi.length > 0) {
                cmbEsercOP4.setModel(new DefaultComboBoxModel<>(esercizi));
              }
            });
  }

  /** Avanza la tendina dell'ordine al valore successivo (max 6). */
  private void avanzaOrdine() {
    int idx = cmbOrdineOP2.getSelectedIndex();
    if (idx < cmbOrdineOP2.getItemCount() - 1) {
      cmbOrdineOP2.setSelectedIndex(idx + 1);
    }
  }

  /** Aggiorna l'etichetta della sessione attiva nell'header. */
  private void aggiornaLabelSessione() {
    if (lblSessione == null) return;
    if (idSessioneAttiva == null) {
      lblSessione.setText("⚪ Nessuna sessione attiva");
      lblSessione.setForeground(StyleUtil.TEXT_MUTED);
    } else {
      lblSessione.setText("🟢 Sessione #" + idSessioneAttiva + " attiva");
      lblSessione.setForeground(GREEN_ACC);
    }
  }

  private void setSessioneAttiva(Integer id) {
    this.idSessioneAttiva = id;
    aggiornaLabelSessione();
  }

  /** Converte testo in intero; in caso di errore mostra un messaggio e ritorna null. */
  private Integer parseIntOrError(String text, String campo) {
    try {
      return Integer.valueOf(text.trim());
    } catch (NumberFormatException e) {
      showResult("[ERRORE] Il campo \"" + campo + "\" deve contenere un numero valido.");
      return null;
    }
  }

  /** Converte testo in double (accetta anche la virgola); errore → messaggio e null. */
  private Double parseDoubleOrError(String text, String campo) {
    try {
      return Double.valueOf(text.trim().replace(",", "."));
    } catch (NumberFormatException e) {
      showResult("[ERRORE] Il campo \"" + campo + "\" deve contenere un numero valido.");
      return null;
    }
  }

  private final String[] num1to20 =
      IntStream.rangeClosed(1, 20).mapToObj(String::valueOf).toArray(String[]::new);
  private final String[] carichi =
      IntStream.rangeClosed(0, 240)
          .mapToObj(i -> String.format(Locale.US, "%.2f", i * 1.25))
          .toArray(String[]::new);

  // ===========================================================================
  // OP 2 — Crea Scheda (coach)
  // ===========================================================================
  private JPanel buildOP2Panel() {
    JPanel outer = new JPanel(new BorderLayout(0, 12));
    outer.setBackground(StyleUtil.BG_DARK);
    outer.setBorder(BorderFactory.createEmptyBorder(20, 25, 15, 25));

    JLabel title = new JLabel("OP 2 — Crea Scheda per Atleta");
    title.setFont(StyleUtil.FONT_TITLE);
    title.setForeground(StyleUtil.YELLOW_ACC);
    outer.add(title, BorderLayout.NORTH);

    JPanel sections = new JPanel();
    sections.setLayout(new BoxLayout(sections, BoxLayout.Y_AXIS));
    sections.setBackground(StyleUtil.BG_DARK);

    // ① Crea Scheda
    sections.add(buildSectionLabel("① Crea Nuova Scheda"));
    JPanel s1 = new JPanel(new GridBagLayout());
    s1.setBackground(StyleUtil.BG_DARK);
    var g1 = defaultGBC();
    var fNomeScheda = addField(s1, g1, 0, "Nome Scheda", "(univoco)");
    var fAtletaEmail = addField(s1, g1, 1, "Email Atleta", "");
    JButton btnCrea =
        StyleUtil.createRoundedButton("Crea Scheda", StyleUtil.YELLOW_ACC, StyleUtil.BG_DARK);
    btnCrea.addActionListener(
        e -> {
          String email = fAtletaEmail.getText().trim();
          if (email.isEmpty()) {
            showResult("[ERRORE] Inserisci l'email dell'atleta.");
            return;
          }
          String res = controller.creaSchedaIniziale(fNomeScheda.getText().trim(), email);
          if (res.startsWith("[OK]")) {
            atletaEmailPerScheda = email;
          }
          showResult(res);
        });
    g1.gridx = 0;
    g1.gridy = 2;
    g1.gridwidth = 2;
    s1.add(btnCrea, g1);
    sections.add(s1);
    sections.add(Box.createVerticalStrut(12));

    // ② Aggiungi Giornata
    sections.add(buildSectionLabel("② Aggiungi Giornata (max 5, min 2)"));
    JPanel s2 = new JPanel(new GridBagLayout());
    s2.setBackground(StyleUtil.BG_DARK);
    var g2 = defaultGBC();
    String[] giorni = {
      "Lunedi", "Martedi", "Mercoledi", "Giovedi", "Venerdi", "Sabato", "Domenica"
    };
    String[] tipi = {"Upper", "Lower", "Push", "Pull", "Leg", "Full Body", "Torso", "Limbs"};
    var cmbGiorno = addComboField(s2, g2, 0, "Giorno", giorni, false);
    var cmbTipo = addComboField(s2, g2, 1, "Tipo", tipi, false);
    JButton btnGiornata =
        StyleUtil.createRoundedButton("Aggiungi Giornata", StyleUtil.YELLOW_ACC, StyleUtil.BG_DARK);
    btnGiornata.addActionListener(
        e -> {
          if (atletaEmailPerScheda == null) {
            showResult("[ERRORE] Devi prima creare una scheda (sezione ①).");
            return;
          }
          String tipo = cmbTipo.getSelectedItem().toString();
          String res = controller.aggiungiGiornata(cmbGiorno.getSelectedItem().toString(), tipo);
          if (res.startsWith("[OK]")) {
            aggiornaEserciziPerTipo(tipo);
            cmbOrdineOP2.setSelectedIndex(0);
          }
          showResult(res + "\n\n" + controller.getSchedaAtleta(atletaEmailPerScheda));
        });
    g2.gridx = 0;
    g2.gridy = 2;
    g2.gridwidth = 2;
    s2.add(btnGiornata, g2);
    sections.add(s2);
    sections.add(Box.createVerticalStrut(12));

    // ③ Aggiungi Esercizio
    sections.add(buildSectionLabel("③ Aggiungi Esercizio (max 6 per giornata)"));
    JPanel s3 = new JPanel(new GridBagLayout());
    s3.setBackground(StyleUtil.BG_DARK);
    var g3 = defaultGBC();
    String[] ordini = {"1", "2", "3", "4", "5", "6"};
    cmbEsercOP2 = addComboField(s3, g3, 0, "Esercizio", ESERCIZI_DB, false);
    cmbOrdineOP2 = addComboField(s3, g3, 1, "Ordine", ordini, false);
    var cmbSerie = addComboField(s3, g3, 2, "Serie", num1to20, false);
    var cmbReps = addComboField(s3, g3, 3, "Reps", num1to20, false);
    JButton btnEserc =
        StyleUtil.createRoundedButton(
            "Aggiungi Esercizio", StyleUtil.YELLOW_ACC, StyleUtil.BG_DARK);
    btnEserc.addActionListener(
        e -> {
          if (atletaEmailPerScheda == null) {
            showResult("[ERRORE] Devi prima creare una scheda (sezione ①).");
            return;
          }
          String res =
              controller.aggiungiEsercizio(
                  cmbEsercOP2.getSelectedItem().toString(),
                  Integer.parseInt(cmbOrdineOP2.getSelectedItem().toString()),
                  Integer.parseInt(cmbSerie.getSelectedItem().toString()),
                  Integer.parseInt(cmbReps.getSelectedItem().toString()));
          if (res.startsWith("[OK]")) {
            avanzaOrdine();
          }
          showResult(res + "\n\n" + controller.getSchedaAtleta(atletaEmailPerScheda));
        });
    g3.gridx = 0;
    g3.gridy = 4;
    g3.gridwidth = 2;
    s3.add(btnEserc, g3);
    sections.add(s3);
    sections.add(Box.createVerticalStrut(12));

    // ④ Torna a giornata esistente
    sections.add(buildSectionLabel("④ Torna a Giornata Esistente"));
    JPanel s4 = new JPanel(new GridBagLayout());
    s4.setBackground(StyleUtil.BG_DARK);
    var g4 = defaultGBC();
    var fIdGiornata = addField(s4, g4, 0, "ID Giornata", "(vedi output)");
    JButton btnCambia =
        StyleUtil.createRoundedButton(
            "Seleziona Giornata", StyleUtil.YELLOW_ACC, StyleUtil.BG_DARK);
    btnCambia.addActionListener(
        e -> {
          Integer id = parseIntOrError(fIdGiornata.getText(), "ID Giornata");
          if (id == null) return;
          controller
              .selezionaGiornata(id)
              .ifPresentOrElse(
                  g -> {
                    cmbGiorno.setSelectedItem(g.giorno());
                    cmbTipo.setSelectedItem(g.tipo());
                    aggiornaEserciziPerTipo(g.tipo());
                    cmbOrdineOP2.setSelectedIndex(0);
                    atletaEmailPerScheda = g.utenteEmail();
                    showResult(
                        "[OK] Stai modificando: "
                            + g.label()
                            + "\n\nContenuto attuale:\n"
                            + g.contenutoFormattato());
                  },
                  () -> showResult("[ERRORE] Giornata #" + id + " non trovata."));
        });
    g4.gridx = 0;
    g4.gridy = 1;
    g4.gridwidth = 2;
    s4.add(btnCambia, g4);
    sections.add(s4);

    outer.add(new JScrollPane(sections), BorderLayout.CENTER);
    return outer;
  }

  private JPanel buildSchedaPanel() {
    JPanel outer = new JPanel(new BorderLayout(0, 12));
    outer.setBackground(StyleUtil.BG_DARK);
    outer.setBorder(BorderFactory.createEmptyBorder(20, 25, 15, 25));

    JLabel title = new JLabel("📋 La Mia Scheda");
    title.setFont(StyleUtil.FONT_TITLE);
    title.setForeground(StyleUtil.YELLOW_ACC);
    outer.add(title, BorderLayout.NORTH);

    schedaArea = new JTextArea();
    schedaArea.setEditable(false);
    schedaArea.setBackground(StyleUtil.PANEL_DARK);
    schedaArea.setForeground(StyleUtil.TEXT_MAIN);
    schedaArea.setFont(StyleUtil.FONT_INPUT);
    schedaArea.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
    outer.add(new JScrollPane(schedaArea), BorderLayout.CENTER);

    JButton btn =
        StyleUtil.createRoundedButton("Aggiorna", StyleUtil.YELLOW_ACC, StyleUtil.BG_DARK);
    btn.setPreferredSize(new Dimension(150, 40));
    btn.addActionListener(e -> aggiornaSchedaArea());

    JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
    btnRow.setBackground(StyleUtil.BG_DARK);
    btnRow.add(btn);
    outer.add(btnRow, BorderLayout.SOUTH);

    return outer;
  }

  /** Ricarica la scheda dell'atleta nell'area centrale. */
  private void aggiornaSchedaArea() {
    if (schedaArea != null) {
      schedaArea.setText(controller.getSchedaAtleta());
      schedaArea.setCaretPosition(0);
    }
  }

  private JLabel buildSectionLabel(String text) {
    JLabel lbl = new JLabel(text);
    lbl.setForeground(StyleUtil.TEXT_MUTED);
    lbl.setBorder(BorderFactory.createEmptyBorder(4, 0, 2, 0));
    lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
    return lbl;
  }

  // ===========================================================================
  // OP 3 — Nuova Sessione
  // ===========================================================================
  private JPanel buildOP3Panel() {
    JPanel form = new JPanel(new GridBagLayout());
    var g = defaultGBC();
    var fId = addFieldWithHelp(form, g, 0, "ID Giornata", "Es. 1 = Lunedì, 2 = Mercoledì...");

    JButton btn =
        StyleUtil.createRoundedButton("Avvia Sessione", StyleUtil.YELLOW_ACC, StyleUtil.BG_DARK);
    btn.addActionListener(
        e -> {
          Integer idGiornata = parseIntOrError(fId.getText(), "ID Giornata");
          if (idGiornata == null) return;
          var optId = controller.inserisciSessione(idGiornata);
          if (optId.isPresent()) {
            setSessioneAttiva(optId.get());
            fId.setText("");
            impostaEserciziOP4(idGiornata);
            navigaA("op4");
            showResult(
                "[OK] Sessione #"
                    + idSessioneAttiva
                    + " avviata! 💪 Gli esercizi della giornata sono già pronti qui sotto.");
          } else {
            showResult("[ERRORE] Impossibile avviare la sessione. Verifica l'ID giornata.");
          }
        });
    return wrapForm(
        "OP 3 — Nuova Sessione",
        form,
        btn,
        "Avvia una sessione scegliendo una giornata della tua scheda. Dopo l'avvio passi"
            + " automaticamente alla registrazione delle serie.");
  }

  // ===========================================================================
  // OP 4 — Registra Serie
  // ===========================================================================
  private JPanel buildOP4Panel() {
    JPanel form = new JPanel(new GridBagLayout());
    var g = defaultGBC();
    cmbEsercOP4 = addComboField(form, g, 0, "Esercizio", ESERCIZI_DB, false);
    var cmbCarico = addComboField(form, g, 1, "Carico (kg)", carichi, true);
    var cmbReps = addComboField(form, g, 2, "Reps", num1to20, false);

    JButton btn =
        StyleUtil.createRoundedButton("Registra Serie", StyleUtil.YELLOW_ACC, StyleUtil.BG_DARK);
    btn.addActionListener(
        e -> {
          if (idSessioneAttiva == null) {
            showResult("[ERRORE] Devi prima avviare una sessione (OP 3)!");
            return;
          }
          Double carico = parseDoubleOrError(cmbCarico.getSelectedItem().toString(), "Carico");
          if (carico == null) return;
          showResult(
              controller.registraSerieWorkingSet(
                  carico,
                  Integer.parseInt(cmbReps.getSelectedItem().toString()),
                  idSessioneAttiva,
                  cmbEsercOP4.getSelectedItem().toString()));
        });
    return wrapForm(
        "OP 4 — Registra Serie",
        form,
        btn,
        "Registra una serie alla volta: il numero si incrementa da solo. "
            + "Per fare 3 set, premi \"Registra Serie\" 3 volte.");
  }

  // ===========================================================================
  // OP 5 — Record Personale
  // ===========================================================================
  private JPanel buildOP5Panel() {
    JPanel form = new JPanel(new GridBagLayout());
    var g = defaultGBC();
    var cmbEserc = addComboField(form, g, 0, "Nome esercizio", ESERCIZI_DB, false);
    JButton btn =
        StyleUtil.createRoundedButton("Cerca PR", StyleUtil.YELLOW_ACC, StyleUtil.BG_DARK);
    btn.addActionListener(
        e -> showResult(controller.getRecordPersonale(cmbEserc.getSelectedItem().toString())));
    return wrapForm("OP 5 — Record Personale (PR)", form, btn, null);
  }

  // ===========================================================================
  // OP 6 — Misurazione
  // ===========================================================================
  private JPanel buildOP6Panel() {
    JPanel form = new JPanel(new GridBagLayout());
    var g = defaultGBC();
    var fPeso = addField(form, g, 0, "Peso (kg)", "(es. 75.5)");
    JButton btn =
        StyleUtil.createRoundedButton("Registra", StyleUtil.YELLOW_ACC, StyleUtil.BG_DARK);
    btn.addActionListener(
        e -> {
          Double peso = parseDoubleOrError(fPeso.getText(), "Peso");
          if (peso == null) return;
          showResult(controller.inserisciMisurazione(peso, null));
          fPeso.setText("");
        });
    return wrapForm("OP 6 — Misurazione Biometrica", form, btn, null);
  }

  // ===========================================================================
  // OP 7 — Invio Feedback (coach)
  // ===========================================================================
  private JPanel buildOP7Panel() {
    JPanel form = new JPanel(new GridBagLayout());
    var g = defaultGBC();

    List<AtletaDTO> atleti = controller.getAtleti();
    String[] atletiLabels =
        atleti.isEmpty()
            ? new String[] {"(nessun atleta)"}
            : atleti.stream()
                .map(a -> a.nomeCompleto() + " (" + a.email() + ")")
                .toArray(String[]::new);

    var cmbAtleta = addComboField(form, g, 0, "Atleta", atletiLabels, false);
    var cmbSessione =
        addComboField(form, g, 1, "Sessione", new String[] {"— scegli prima l'atleta —"}, false);
    var fTesto = addField(form, g, 2, "Testo feedback", "");

    // Quando cambia l'atleta, ricarica le sue sessioni nella seconda tendina
    cmbAtleta.addActionListener(
        ev -> {
          int idx = cmbAtleta.getSelectedIndex();
          if (idx < 0 || idx >= atleti.size()) return;
          caricaSessioniOP7(atleti.get(idx).email(), cmbSessione);
        });

    // Popola subito le sessioni del primo atleta
    if (!atleti.isEmpty()) {
      caricaSessioniOP7(atleti.get(0).email(), cmbSessione);
    }

    JButton btn = StyleUtil.createRoundedButton("Invia", StyleUtil.YELLOW_ACC, StyleUtil.BG_DARK);
    btn.addActionListener(
        e -> {
          if (atleti.isEmpty()) {
            showResult("[ERRORE] Non hai atleti supervisionati.");
            return;
          }
          if (sessioniOP7.isEmpty()) {
            showResult("[ERRORE] Questo atleta non ha ancora sessioni.");
            return;
          }
          if (fTesto.getText().trim().isEmpty()) {
            showResult("[ERRORE] Il testo del feedback non può essere vuoto.");
            return;
          }
          int idxSess = cmbSessione.getSelectedIndex();
          if (idxSess < 0 || idxSess >= sessioniOP7.size()) {
            showResult("[ERRORE] Seleziona una sessione valida.");
            return;
          }
          int idSessione = sessioniOP7.get(idxSess).idSessione();
          showResult(controller.inviaFeedback(fTesto.getText().trim(), idSessione));
          fTesto.setText("");
        });

    return wrapForm(
        "OP 7 — Invio Feedback",
        form,
        btn,
        "Scegli un atleta e una sua sessione, poi scrivi il feedback. "
            + "Nessun ID da inserire a mano.");
  }

  /** Carica nella tendina le sessioni dell'atleta selezionato (OP 7). */
  private void caricaSessioniOP7(String email, JComboBox<String> cmbSessione) {
    sessioniOP7 = controller.getSessioniAtleta(email);
    if (sessioniOP7.isEmpty()) {
      cmbSessione.setModel(new DefaultComboBoxModel<>(new String[] {"(nessuna sessione)"}));
    } else {
      cmbSessione.setModel(
          new DefaultComboBoxModel<>(
              sessioniOP7.stream().map(SessioneRecenteDTO::label).toArray(String[]::new)));
    }
  }

  // ===========================================================================
  // OP 8 — Varianti
  // ===========================================================================
  private JPanel buildOP8Panel() {
    JPanel form = new JPanel(new GridBagLayout());
    var g = defaultGBC();
    var cmbEserc = addComboField(form, g, 0, "Esercizio Base", ESERCIZI_DB, false);
    JButton btn =
        StyleUtil.createRoundedButton("Cerca Varianti", StyleUtil.YELLOW_ACC, StyleUtil.BG_DARK);
    btn.addActionListener(
        e -> showResult(controller.getVarianti(cmbEserc.getSelectedItem().toString())));
    return wrapForm("OP 8 — Varianti", form, btn, null);
  }

  // ===========================================================================
  // OP 9 — Volume Mensile
  // ===========================================================================
  private JPanel buildOP9Panel() {
    JPanel form = new JPanel(new GridBagLayout());
    var g = defaultGBC();
    var cmbMuscolo = addComboField(form, g, 0, "Gruppo Muscolare", MUSCOLI_DB, false);
    JButton btn =
        StyleUtil.createRoundedButton("Calcola Volume", StyleUtil.YELLOW_ACC, StyleUtil.BG_DARK);
    btn.addActionListener(
        e -> showResult(controller.getVolumeMensile(cmbMuscolo.getSelectedItem().toString())));
    return wrapForm("OP 9 — Volume Mensile", form, btn, null);
  }

  // ===========================================================================
  // OP 10 — Attrezzatura
  // ===========================================================================
  private JPanel buildOP10Panel() {
    JPanel form = new JPanel(new GridBagLayout());
    var g = defaultGBC();
    var fId =
        addFieldWithHelp(
            form,
            g,
            0,
            "ID Giornata",
            "L'ID della giornata di cui vuoi conoscere l'attrezzatura necessaria: lo trovi in"
                + " \"La Mia Scheda\".");
    JButton btn =
        StyleUtil.createRoundedButton("Verifica", StyleUtil.YELLOW_ACC, StyleUtil.BG_DARK);
    btn.addActionListener(
        e -> {
          Integer id = parseIntOrError(fId.getText(), "ID Giornata");
          if (id == null) return;
          showResult(controller.getAttrezzaturaGiornata(id));
        });
    return wrapForm("OP 10 — Attrezzatura Necessaria", form, btn, null);
  }

  // ===========================================================================
  // OP 11 — Dashboard Coach
  // ===========================================================================
  private JPanel buildOP11Panel() {
    JPanel form = new JPanel(new GridBagLayout());
    form.setBackground(StyleUtil.BG_DARK);
    JLabel info = new JLabel("Record personali e sessioni recenti degli atleti che supervisioni.");
    info.setFont(StyleUtil.FONT_INPUT);
    info.setForeground(StyleUtil.TEXT_MUTED);
    form.add(info);
    JButton btn =
        StyleUtil.createRoundedButton("Carica Dashboard", StyleUtil.YELLOW_ACC, StyleUtil.BG_DARK);
    btn.addActionListener(e -> showResult(controller.getDashboardCoach()));

    String guidaCoach =
        """
        DASHBOARD COACH:

        Mostra i record personali dei tuoi atleti e le loro sessioni recenti.

        Per inviare un feedback vai in OP 07: scegli atleta e sessione dalle tendine, senza copiare alcun ID.""";
    return wrapForm("OP 11 — Dashboard Coach", form, btn, guidaCoach);
  }

  private JPanel buildAtletiPanel() {
    JPanel form = new JPanel(new GridBagLayout());
    form.setBackground(StyleUtil.BG_DARK);
    JLabel info = new JLabel("Elenco degli atleti che supervisioni, con il loro obiettivo.");
    info.setFont(StyleUtil.FONT_INPUT);
    info.setForeground(StyleUtil.TEXT_MUTED);
    form.add(info);
    JButton btn =
        StyleUtil.createRoundedButton("Carica Atleti", StyleUtil.YELLOW_ACC, StyleUtil.BG_DARK);
    btn.addActionListener(e -> showResult(controller.getElencoAtleti()));
    return wrapForm("👥 I Miei Atleti", form, btn, null);
  }

  // ===========================================================================
  // OP 12 — Report Volume
  // ===========================================================================
  private JPanel buildOP12Panel() {
    JPanel form = new JPanel(new GridBagLayout());
    var g = defaultGBC();
    var fId =
        addFieldWithHelp(
            form,
            g,
            0,
            "ID Sessione",
            "Numero della sessione terminata di cui vuoi analizzare i volumi.");
    JButton btn =
        StyleUtil.createRoundedButton("Genera Report", StyleUtil.YELLOW_ACC, StyleUtil.BG_DARK);
    btn.addActionListener(
        e -> {
          Integer id = parseIntOrError(fId.getText(), "ID Sessione");
          if (id == null) return;
          showResult(controller.getReportVolume(id));
        });
    return wrapForm("OP 12 — Report Volume Reale", form, btn, null);
  }
}
