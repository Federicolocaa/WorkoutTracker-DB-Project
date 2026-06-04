package it.unibo.workout.view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.text.JTextComponent;

public final class StyleUtil {

  public static final Color BG_DARK = new Color(18, 18, 18);
  public static final Color PANEL_DARK = new Color(28, 28, 28);
  public static final Color YELLOW_ACC = new Color(255, 204, 0);
  public static final Color RED_ACC = new Color(255, 59, 48);
  public static final Color TEXT_MAIN = new Color(240, 240, 240);
  public static final Color TEXT_MUTED = new Color(150, 150, 150);

  public static final Font FONT_TITLE = new Font("Helvetica Neue", Font.BOLD, 18);
  public static final Font FONT_LABEL = new Font("Helvetica Neue", Font.BOLD, 13);
  public static final Font FONT_INPUT = new Font("Helvetica Neue", Font.PLAIN, 14);

  private StyleUtil() {}

  // Bottone Grande (Login, Registrati)
  public static JButton createRoundedButton(String text, Color bg, Color fg) {
    JButton btn = createBaseButton(text, bg, fg, new Font("Helvetica Neue", Font.BOLD, 14));
    btn.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
    return btn;
  }

  // Bottone Piccolo (➤ dell'AI)
  public static JButton createSmallRoundedButton(String text, Color bg, Color fg) {
    JButton btn = createBaseButton(text, bg, fg, new Font("Helvetica Neue", Font.BOLD, 14));
    btn.setBorder(
        BorderFactory.createEmptyBorder(4, 10, 4, 10)); // Bordi piccoli per non sballare il layout
    return btn;
  }

  // Bottone Circolare (Info, Aiuto ID)
  public static JButton createCircularIconButton(String text, Color bg, Color fg) {
    JButton btn =
        new JButton(text) {
          @Override
          protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bg);
            g2.fillOval(0, 0, getWidth(), getHeight());
            super.paintComponent(g2);
            g2.dispose();
          }
        };
    btn.setForeground(fg);
    btn.setFont(new Font("Helvetica Neue", Font.BOLD, 14));
    btn.setOpaque(false);
    btn.setContentAreaFilled(false);
    btn.setBorderPainted(false);
    btn.setFocusPainted(false);
    btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    btn.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
    return btn;
  }

  private static JButton createBaseButton(String text, Color bg, Color fg, Font font) {
    JButton btn =
        new JButton(text) {
          @Override
          protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            super.paintComponent(g2);
            g2.dispose();
          }
        };
    btn.setForeground(fg);
    btn.setFont(font);
    btn.setOpaque(false);
    btn.setContentAreaFilled(false);
    btn.setBorderPainted(false);
    btn.setFocusPainted(false);
    btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    return btn;
  }

  public static void applyRoundedBorder(JComponent comp) {
    comp.setBorder(
        BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(TEXT_MUTED, 1, true),
            BorderFactory.createEmptyBorder(6, 12, 6, 12)));
    if (comp instanceof JTextComponent) {
      ((JTextComponent) comp).setCaretColor(Color.WHITE);
    }
  }
}
