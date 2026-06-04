package it.unibo.workout.view;

import it.unibo.workout.controller.AuthController;
import it.unibo.workout.controller.WorkoutController;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.prefs.Preferences;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class LoginView {

  private static final int WIN_WIDTH = 550;
  private static final int WIN_HEIGHT = 650;
  private static final int MAX_EMAIL = 100;
  private static final int MAX_PWD = 255;
  private static final String COACH_SECRET = "020405";
  // Regex rigorosa: niente spazi, tutto minuscolo, chiocciola e dominio finale corretti
  private static final String EMAIL_REGEX = "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,6}$";

  private static final String[] OBIETTIVI = {
    "Forza",
    "Ipertrofia (massa muscolare)",
    "Dimagrimento",
    "Tonificazione",
    "Resistenza",
    "Powerlifting",
    "Benessere generale"
  };
  private static final String[] CERTIFICAZIONI = {
    "ISSA", "NASM", "FIPE (Pesistica)", "CONI", "ACSM", "Personal Trainer Certificato"
  };
  private static final String[] SPECIALIZZAZIONI = {
    "Powerlifting",
    "Bodybuilding",
    "Functional Training",
    "Preparazione Atletica",
    "Fitness Generale",
    "Riabilitazione"
  };

  private final AuthController authController;
  private final Preferences prefs;
  private JFrame frame;
  private CardLayout cardLayout;
  private JPanel mainPanel;

  private JLabel loginTitleLabel;
  private JLabel errorLabel;
  private JTextField emailField;
  private JPasswordField passField;
  private JLabel lblCodiceLogin;
  private JPasswordField codiceLoginField;

  private boolean isCoachMode = false;

  public LoginView() {
    this.authController = new AuthController();
    this.prefs = Preferences.userNodeForPackage(LoginView.class);
  }

  public void avvia() {
    String savedEmail = prefs.get("email", "");
    String savedPwd = prefs.get("pwd", "");
    boolean savedIsCoach = prefs.getBoolean("isCoach", false);

    if (!savedEmail.isEmpty() && !savedPwd.isEmpty()) {
      authController
          .login(savedEmail, savedPwd)
          .ifPresent(
              utente -> {
                if ((savedIsCoach && utente.isCoach()) || (!savedIsCoach && utente.isAtleta())) {
                  SwingUtilities.invokeLater(
                      () -> new MainView(new WorkoutController(utente)).avvia());
                }
              });
    }

    SwingUtilities.invokeLater(
        () -> {
          frame = new JFrame("Fitness Tracker — Accesso");
          frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          frame.setLayout(new BorderLayout());
          frame.getContentPane().setBackground(StyleUtil.BG_DARK);

          frame.add(buildHeader(), BorderLayout.NORTH);

          cardLayout = new CardLayout();
          mainPanel = new JPanel(cardLayout);
          mainPanel.setBackground(StyleUtil.BG_DARK);

          mainPanel.add(buildRoleSelectionPanel(), "SELEZIONE_RUOLO");
          mainPanel.add(buildLoginPanel(), "LOGIN");

          frame.add(mainPanel, BorderLayout.CENTER);
          frame.setSize(WIN_WIDTH, WIN_HEIGHT);
          frame.setLocationRelativeTo(null);
          frame.setResizable(false);

          cardLayout.show(mainPanel, "SELEZIONE_RUOLO");
          frame.setVisible(true);
        });
  }

  private JPanel buildHeader() {
    JPanel header = new JPanel(new BorderLayout());
    header.setBackground(StyleUtil.PANEL_DARK);
    header.setPreferredSize(new Dimension(0, 70));
    JLabel title = new JLabel("⚡ FITNESS TRACKER");
    title.setFont(StyleUtil.FONT_TITLE);
    title.setForeground(StyleUtil.YELLOW_ACC);
    title.setHorizontalAlignment(SwingConstants.CENTER);
    header.add(title, BorderLayout.CENTER);
    return header;
  }

  private JPanel buildRoleSelectionPanel() {
    JPanel outer = new JPanel(new BorderLayout());
    outer.setBackground(StyleUtil.BG_DARK);

    JPanel centerPanel = new JPanel();
    centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
    centerPanel.setBackground(StyleUtil.BG_DARK);
    centerPanel.add(Box.createVerticalGlue());

    JLabel subtitle = new JLabel("Area di Autenticazione");
    subtitle.setFont(StyleUtil.FONT_TITLE);
    subtitle.setForeground(StyleUtil.TEXT_MAIN);
    subtitle.setAlignmentX(JLabel.CENTER_ALIGNMENT);

    JButton btnAtleta =
        StyleUtil.createRoundedButton("💪 Accesso Atleta", StyleUtil.YELLOW_ACC, StyleUtil.BG_DARK);
    btnAtleta.setMaximumSize(new Dimension(300, 50));
    btnAtleta.setAlignmentX(JButton.CENTER_ALIGNMENT);
    btnAtleta.addActionListener(e -> abilitaLogin(false));

    JButton btnCoach =
        StyleUtil.createRoundedButton("🏋 Accesso Coach", StyleUtil.RED_ACC, StyleUtil.TEXT_MAIN);
    btnCoach.setMaximumSize(new Dimension(300, 50));
    btnCoach.setAlignmentX(JButton.CENTER_ALIGNMENT);
    btnCoach.addActionListener(e -> abilitaLogin(true));

    centerPanel.add(subtitle);
    centerPanel.add(Box.createVerticalStrut(40));
    centerPanel.add(btnAtleta);
    centerPanel.add(Box.createVerticalStrut(20));
    centerPanel.add(btnCoach);
    centerPanel.add(Box.createVerticalGlue());

    JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));
    bottomPanel.setBackground(StyleUtil.BG_DARK);
    JButton btnInfo =
        StyleUtil.createCircularIconButton("i", StyleUtil.PANEL_DARK, StyleUtil.TEXT_MAIN);
    btnInfo.setPreferredSize(new Dimension(45, 45));
    btnInfo.addActionListener(e -> mostraInfo());
    bottomPanel.add(btnInfo);

    outer.add(centerPanel, BorderLayout.CENTER);
    outer.add(bottomPanel, BorderLayout.SOUTH);
    return outer;
  }

  private void mostraInfo() {
    String msg =
        """
        FITNESS TRACKER \u2014 Guida rapida:

        ACCESSO:
        \u2022 Scegli Atleta o Coach, poi inserisci email e password.
        \u2022 Per il Coach serve anche il codice segreto (default: 020405).

        REGISTRAZIONE (pulsante "Nuovo Profilo"):
        \u2022 I campi con asterisco (*) sono obbligatori.
        \u2022 L'Atleta sceglie un obiettivo; il Coach certificazione e specializzazione.

        Una volta dentro, usa il menu a sinistra per spostarti tra le operazioni.""";
    JOptionPane.showMessageDialog(
        frame, msg, "Informazioni sul Sistema", JOptionPane.INFORMATION_MESSAGE);
  }

  private void abilitaLogin(boolean coach) {
    isCoachMode = coach;
    loginTitleLabel.setText(coach ? "Accesso Coach" : "Accesso Atleta");
    lblCodiceLogin.setVisible(coach);
    codiceLoginField.setVisible(coach);
    svuotaCampiLogin();
    cardLayout.show(mainPanel, "LOGIN");
  }

  private JPanel buildLoginPanel() {
    JPanel panel = new JPanel(new GridBagLayout());
    panel.setBackground(StyleUtil.BG_DARK);
    GridBagConstraints g = new GridBagConstraints();
    g.insets = new Insets(6, 20, 6, 20);
    g.fill = GridBagConstraints.HORIZONTAL;
    g.gridx = 0;

    loginTitleLabel = new JLabel("Accesso");
    loginTitleLabel.setFont(StyleUtil.FONT_TITLE);
    loginTitleLabel.setForeground(StyleUtil.TEXT_MAIN);
    loginTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
    g.gridy = 0;
    panel.add(loginTitleLabel, g);

    g.gridy = 1;
    panel.add(Box.createVerticalStrut(10), g);

    JLabel lblEmail = new JLabel("Email");
    lblEmail.setFont(StyleUtil.FONT_LABEL);
    lblEmail.setForeground(StyleUtil.TEXT_MUTED);
    g.gridy = 2;
    panel.add(lblEmail, g);

    emailField = new JTextField(20);
    applyLengthLimit(emailField, MAX_EMAIL);
    emailField.setFont(StyleUtil.FONT_INPUT);
    emailField.setBackground(StyleUtil.PANEL_DARK);
    emailField.setForeground(StyleUtil.TEXT_MAIN);
    StyleUtil.applyRoundedBorder(emailField);
    g.gridy = 3;
    panel.add(emailField, g);

    JLabel lblPass = new JLabel("Password");
    lblPass.setFont(StyleUtil.FONT_LABEL);
    lblPass.setForeground(StyleUtil.TEXT_MUTED);
    g.gridy = 4;
    panel.add(lblPass, g);

    JPanel passPanel = new JPanel(new BorderLayout());
    passPanel.setBackground(StyleUtil.PANEL_DARK);
    passField = new JPasswordField(20);
    passField.setEchoChar('\u2022'); // Fix Mac per i pallini
    applyLengthLimit(passField, MAX_PWD);
    passField.setFont(StyleUtil.FONT_INPUT);
    passField.setBackground(StyleUtil.PANEL_DARK);
    passField.setForeground(StyleUtil.TEXT_MAIN);
    StyleUtil.applyRoundedBorder(passField);

    JButton btnEye = StyleUtil.createRoundedButton("👁", StyleUtil.BG_DARK, StyleUtil.TEXT_MUTED);
    btnEye.addActionListener(e -> togglePasswordVisibility(passField));
    passPanel.add(passField, BorderLayout.CENTER);
    passPanel.add(btnEye, BorderLayout.EAST);
    g.gridy = 5;
    panel.add(passPanel, g);

    lblCodiceLogin = new JLabel("Codice Segreto Coach");
    lblCodiceLogin.setFont(StyleUtil.FONT_LABEL);
    lblCodiceLogin.setForeground(StyleUtil.RED_ACC);
    g.gridy = 6;
    panel.add(lblCodiceLogin, g);

    codiceLoginField = new JPasswordField(15);
    codiceLoginField.setEchoChar('\u2022');
    applyLengthLimit(codiceLoginField, 10);
    codiceLoginField.setFont(StyleUtil.FONT_INPUT);
    codiceLoginField.setBackground(StyleUtil.PANEL_DARK);
    codiceLoginField.setForeground(StyleUtil.TEXT_MAIN);
    StyleUtil.applyRoundedBorder(codiceLoginField);
    g.gridy = 7;
    panel.add(codiceLoginField, g);

    errorLabel = new JLabel(" ");
    errorLabel.setFont(StyleUtil.FONT_LABEL);
    errorLabel.setForeground(StyleUtil.RED_ACC);
    errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
    g.gridy = 8;
    panel.add(errorLabel, g);

    JButton btnLogin =
        StyleUtil.createRoundedButton("Accedi", StyleUtil.YELLOW_ACC, StyleUtil.BG_DARK);
    btnLogin.setPreferredSize(new Dimension(300, 45));
    btnLogin.addActionListener(e -> eseguiLogin());
    g.gridy = 9;
    panel.add(btnLogin, g);

    JPanel bottomButtons = new JPanel(new BorderLayout());
    bottomButtons.setBackground(StyleUtil.BG_DARK);
    bottomButtons.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
    JButton btnBack =
        StyleUtil.createRoundedButton("← Indietro", StyleUtil.PANEL_DARK, StyleUtil.TEXT_MAIN);
    btnBack.addActionListener(
        e -> {
          svuotaCampiLogin();
          cardLayout.show(mainPanel, "SELEZIONE_RUOLO");
        });

    JButton btnRegister =
        StyleUtil.createRoundedButton("Nuovo Profilo", StyleUtil.PANEL_DARK, StyleUtil.YELLOW_ACC);
    btnRegister.addActionListener(e -> showRegistrationDialog());

    bottomButtons.add(btnBack, BorderLayout.WEST);
    bottomButtons.add(btnRegister, BorderLayout.EAST);
    g.gridy = 10;
    panel.add(bottomButtons, g);

    return panel;
  }

  private void eseguiLogin() {
    String email = emailField.getText().toLowerCase().trim(); // Forza minuscolo
    String pwd = new String(passField.getPassword()).trim();

    if (email.isBlank() || pwd.isBlank()) {
      mostraErrore("Compila tutti i campi!");
      return;
    }

    if (!Pattern.matches(EMAIL_REGEX, email)) {
      mostraErrore("Formato email non valido! (Es: mario@gmail.com)");
      return;
    }

    if (isCoachMode && !new String(codiceLoginField.getPassword()).trim().equals(COACH_SECRET)) {
      mostraErrore("Codice univoco Coach errato!");
      return;
    }

    authController
        .login(email, pwd)
        .ifPresentOrElse(
            utente -> {
              if ((isCoachMode && !utente.isCoach()) || (!isCoachMode && !utente.isAtleta())) {
                mostraErrore("Ruolo utente non corrispondente al DB!");
              } else {
                // Autosalvataggio
                prefs.put("email", email);
                prefs.put("pwd", pwd);
                prefs.putBoolean("isCoach", isCoachMode);

                frame.dispose();
                new MainView(new WorkoutController(utente)).avvia();
              }
            },
            () -> mostraErrore("Credenziali errate. Riprova."));
  }

  private void mostraErrore(String messaggio) {
    errorLabel.setText(messaggio);
    passField.setText("");
    codiceLoginField.setText("");
    emailField.requestFocusInWindow();
  }

  private void svuotaCampiLogin() {
    errorLabel.setText(" ");
    emailField.setText("");
    passField.setText("");
    codiceLoginField.setText("");
  }

  private void showRegistrationDialog() {
    String roleStr = isCoachMode ? "Coach" : "Atleta";
    JDialog dialog = new JDialog(frame, "Registrazione - " + roleStr, true);
    dialog.setLayout(new BorderLayout());
    dialog.setSize(600, 700);
    dialog.setLocationRelativeTo(frame);
    dialog.getContentPane().setBackground(StyleUtil.BG_DARK);

    JPanel dHeader = new JPanel(new BorderLayout());
    dHeader.setBackground(isCoachMode ? StyleUtil.RED_ACC : StyleUtil.YELLOW_ACC);
    dHeader.setPreferredSize(new Dimension(0, 55));
    JLabel dTitle = new JLabel("Crea Profilo " + roleStr);
    dTitle.setFont(StyleUtil.FONT_TITLE);
    dTitle.setForeground(StyleUtil.BG_DARK);
    dTitle.setHorizontalAlignment(SwingConstants.CENTER);
    dHeader.add(dTitle, BorderLayout.CENTER);
    dialog.add(dHeader, BorderLayout.NORTH);

    JPanel form = new JPanel(new GridBagLayout());
    form.setBackground(StyleUtil.BG_DARK);
    GridBagConstraints g = new GridBagConstraints();
    g.insets = new Insets(8, 10, 8, 10);
    g.fill = GridBagConstraints.HORIZONTAL;
    g.weightx = 1.0;

    JTextField fEmail = addDialogField(form, g, 0, "Email *", MAX_EMAIL);

    g.gridx = 0;
    g.gridy = 1;
    g.weightx = 0.3;
    JLabel lblPw = new JLabel("Password *");
    lblPw.setFont(StyleUtil.FONT_LABEL);
    lblPw.setForeground(StyleUtil.TEXT_MUTED);
    form.add(lblPw, g);

    g.gridx = 1;
    g.weightx = 0.7;
    JPanel pPass = new JPanel(new BorderLayout());
    pPass.setBackground(StyleUtil.PANEL_DARK);
    JPasswordField fPassword = new JPasswordField(15);
    fPassword.setEchoChar('\u2022');
    applyLengthLimit(fPassword, MAX_PWD);
    fPassword.setFont(StyleUtil.FONT_INPUT);
    fPassword.setBackground(StyleUtil.PANEL_DARK);
    fPassword.setForeground(StyleUtil.TEXT_MAIN);
    StyleUtil.applyRoundedBorder(fPassword);

    JButton btnEyeReg =
        StyleUtil.createRoundedButton("👁", StyleUtil.BG_DARK, StyleUtil.TEXT_MUTED);
    btnEyeReg.addActionListener(e -> togglePasswordVisibility(fPassword));
    pPass.add(fPassword, BorderLayout.CENTER);
    pPass.add(btnEyeReg, BorderLayout.EAST);
    form.add(pPass, g);

    JTextField fNome = addDialogField(form, g, 2, "Nome *", 50);
    JTextField fCognome = addDialogField(form, g, 3, "Cognome *", 50);

    g.gridx = 0;
    g.gridy = 4;
    g.weightx = 0.3;
    JLabel lblData = new JLabel("Data Nascita *");
    lblData.setFont(StyleUtil.FONT_LABEL);
    lblData.setForeground(StyleUtil.TEXT_MUTED);
    form.add(lblData, g);

    g.gridx = 1;
    g.weightx = 0.7;
    JPanel pData = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
    pData.setBackground(StyleUtil.BG_DARK);

    JComboBox<String> cmbGiorno =
        new JComboBox<>(
            IntStream.rangeClosed(1, 31)
                .mapToObj(i -> String.format("%02d", i))
                .toArray(String[]::new));
    JComboBox<String> cmbMese =
        new JComboBox<>(
            IntStream.rangeClosed(1, 12)
                .mapToObj(i -> String.format("%02d", i))
                .toArray(String[]::new));
    JComboBox<String> cmbAnno =
        new JComboBox<>(
            IntStream.rangeClosed(1940, 2015)
                .mapToObj(String::valueOf)
                .sorted((a, b) -> b.compareTo(a))
                .toArray(String[]::new));
    pData.add(cmbGiorno);
    pData.add(cmbMese);
    pData.add(cmbAnno);
    form.add(pData, g);

    JComboBox<String> cmbAltezza = null;
    JComboBox<String> cmbPeso = null;
    JComboBox<String> cmbObiettivo = null, cmbCert = null, cmbSpec = null;
    JPasswordField fCodiceSegreto = null;

    if (!isCoachMode) {
      g.gridx = 0;
      g.gridy = 5;
      g.weightx = 0.3;
      JLabel lblAlt = new JLabel("Altezza (cm)");
      lblAlt.setFont(StyleUtil.FONT_LABEL);
      lblAlt.setForeground(StyleUtil.TEXT_MUTED);
      form.add(lblAlt, g);

      g.gridx = 1;
      g.weightx = 0.7;
      cmbAltezza =
          new JComboBox<>(
              IntStream.rangeClosed(140, 220).mapToObj(String::valueOf).toArray(String[]::new));
      cmbAltezza.setSelectedItem("175");
      form.add(cmbAltezza, g);

      g.gridx = 0;
      g.gridy = 6;
      g.weightx = 0.3;
      JLabel lblPeso = new JLabel("Peso (kg)");
      lblPeso.setFont(StyleUtil.FONT_LABEL);
      lblPeso.setForeground(StyleUtil.TEXT_MUTED);
      form.add(lblPeso, g);

      g.gridx = 1;
      g.weightx = 0.7;
      String[] pesi = new String[221];
      double p = 40.0;
      for (int i = 0; i < 221; i++) {
        pesi[i] = String.valueOf(p);
        p += 0.5;
      }
      cmbPeso = new JComboBox<>(pesi);
      cmbPeso.setSelectedItem("70.0");
      form.add(cmbPeso, g);

      cmbObiettivo = addDialogCombo(form, g, 7, "Obiettivo", OBIETTIVI);
    } else {
      cmbCert = addDialogCombo(form, g, 5, "Certificazione", CERTIFICAZIONI);
      cmbSpec = addDialogCombo(form, g, 6, "Specializzazione", SPECIALIZZAZIONI);

      g.gridy = 7;
      g.gridx = 0;
      JLabel lblSegreto = new JLabel("Codice Coach *");
      lblSegreto.setForeground(StyleUtil.RED_ACC);
      lblSegreto.setFont(StyleUtil.FONT_LABEL);
      form.add(lblSegreto, g);

      g.gridx = 1;
      fCodiceSegreto = new JPasswordField(15);
      fCodiceSegreto.setEchoChar('\u2022');
      fCodiceSegreto.setBackground(StyleUtil.PANEL_DARK);
      fCodiceSegreto.setForeground(StyleUtil.TEXT_MAIN);
      StyleUtil.applyRoundedBorder(fCodiceSegreto);
      form.add(fCodiceSegreto, g);
    }

    JLabel dError = new JLabel(" ");
    dError.setFont(StyleUtil.FONT_LABEL);
    dError.setForeground(StyleUtil.RED_ACC);
    dError.setHorizontalAlignment(SwingConstants.CENTER);
    g.gridx = 0;
    g.gridy = 8;
    g.gridwidth = 2;
    form.add(dError, g);

    dialog.add(form, BorderLayout.CENTER);

    JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
    btnPanel.setBackground(StyleUtil.BG_DARK);

    JButton btnAnnulla =
        StyleUtil.createRoundedButton("Annulla", StyleUtil.PANEL_DARK, StyleUtil.TEXT_MAIN);
    JButton btnConferma =
        StyleUtil.createRoundedButton("Registrati", StyleUtil.YELLOW_ACC, StyleUtil.BG_DARK);

    final JComboBox<String> fAlt = cmbAltezza, fPes = cmbPeso;
    final JComboBox<String> fOb = cmbObiettivo, fCrt = cmbCert, fSpc = cmbSpec;
    final JPasswordField fCod = fCodiceSegreto;

    btnConferma.addActionListener(
        e -> {
          String mailFormattata = fEmail.getText().toLowerCase().trim();
          if (!Pattern.matches(EMAIL_REGEX, mailFormattata)) {
            dError.setText("Formato email non valido!");
            return;
          }

          String dataFormat =
              cmbAnno.getSelectedItem()
                  + "-"
                  + cmbMese.getSelectedItem()
                  + "-"
                  + cmbGiorno.getSelectedItem();
          String esito = "";
          if (!isCoachMode) {
            esito =
                authController.registraAtleta(
                    mailFormattata,
                    new String(fPassword.getPassword()),
                    dataFormat,
                    fNome.getText(),
                    fCognome.getText(),
                    fAlt.getSelectedItem().toString(),
                    fPes.getSelectedItem().toString(),
                    fOb.getSelectedItem().toString());
          } else {
            if (!new String(fCod.getPassword()).trim().equals(COACH_SECRET)) {
              dError.setText("CODICE SEGRETO ERRATO!");
              return;
            }
            esito =
                authController.registraCoach(
                    mailFormattata,
                    new String(fPassword.getPassword()),
                    dataFormat,
                    fNome.getText(),
                    fCognome.getText(),
                    fCrt.getSelectedItem().toString(),
                    fSpc.getSelectedItem().toString());
          }

          if (esito.contains("[OK]") || esito.contains("completata")) {
            dialog.dispose();
            errorLabel.setForeground(StyleUtil.YELLOW_ACC);
            errorLabel.setText("Registrazione completata! Effettua il login.");
          } else {
            dError.setText(esito);
          }
        });

    btnAnnulla.addActionListener(e -> dialog.dispose());
    btnPanel.add(btnAnnulla);
    btnPanel.add(btnConferma);
    dialog.add(btnPanel, BorderLayout.SOUTH);
    dialog.setVisible(true);
  }

  private JTextField addDialogField(
      JPanel panel, GridBagConstraints g, int row, String labelText, int limit) {
    g.gridx = 0;
    g.gridy = row;
    g.weightx = 0.3;
    JLabel lbl = new JLabel(labelText);
    lbl.setFont(StyleUtil.FONT_LABEL);
    lbl.setForeground(StyleUtil.TEXT_MUTED);
    panel.add(lbl, g);

    JTextField f = new JTextField(15);
    applyLengthLimit(f, limit);
    f.setFont(StyleUtil.FONT_INPUT);
    f.setBackground(StyleUtil.PANEL_DARK);
    f.setForeground(StyleUtil.TEXT_MAIN);
    StyleUtil.applyRoundedBorder(f);
    g.gridx = 1;
    g.weightx = 0.7;
    panel.add(f, g);
    return f;
  }

  private JComboBox<String> addDialogCombo(
      JPanel panel, GridBagConstraints g, int row, String labelText, String[] items) {
    g.gridx = 0;
    g.gridy = row;
    g.weightx = 0.3;
    JLabel lbl = new JLabel(labelText);
    lbl.setFont(StyleUtil.FONT_LABEL);
    lbl.setForeground(StyleUtil.TEXT_MUTED);
    panel.add(lbl, g);

    JComboBox<String> combo = new JComboBox<>(items);
    combo.setFont(StyleUtil.FONT_INPUT);
    g.gridx = 1;
    g.weightx = 0.7;
    panel.add(combo, g);
    return combo;
  }

  private void togglePasswordVisibility(JPasswordField field) {
    if (field.getEchoChar() == '\u2022') {
      field.setEchoChar((char) 0);
    } else {
      field.setEchoChar('\u2022');
    }
  }

  private void applyLengthLimit(JTextField field, int limit) {
    ((AbstractDocument) field.getDocument())
        .setDocumentFilter(
            new DocumentFilter() {
              @Override
              public void insertString(
                  FilterBypass fb, int offset, String string, AttributeSet attr)
                  throws BadLocationException {
                if (fb.getDocument().getLength() + string.length() <= limit)
                  super.insertString(fb, offset, string, attr);
              }

              @Override
              public void replace(
                  FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                  throws BadLocationException {
                if (fb.getDocument().getLength() + text.length() - length <= limit)
                  super.replace(fb, offset, length, text, attrs);
              }
            });
  }
}
