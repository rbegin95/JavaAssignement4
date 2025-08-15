package com.JavaGame.ui;

import com.JavaGame.engine.QuizEngine;
import com.JavaGame.model.Card;
import com.JavaGame.model.Decks;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class MainFrame extends JFrame {

    // config
    private static final int QUIZ_LENGTH = 10;
    private static final String ICON_PATH = "assets/icon/DevQuiz_1024.png"; // optional PNG for window/dialog icon
    private static final Color PRIMARY = new Color(35, 116, 225);
    private static final Color BG = new Color(245, 246, 248);

    // state
    private QuizEngine engine;
    private boolean awaitingNext = false;
    private String modeLabel = "Mixed";

    // ui
    private JLabel titleLabel;
    private JLabel questionLabel;
    private JTextField answerField;
    private JButton submitButton;
    private JLabel feedbackLabel;
    private JLabel scoreLabel;
    private JLabel streakLabel;
    private JProgressBar progress;

    public MainFrame() {
        setLookAndFeel();
        chooseMode();           // sets engine + modeLabel
        buildUI();
        startRound();
        setVisible(true);
    }

    private void setLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
            UIManager.put("control", BG);
            UIManager.put("nimbusLightBackground", Color.WHITE);
        } catch (Exception ignored) {}
    }

    private void chooseMode() {
        JRadioButton java = new JRadioButton("Java");
        JRadioButton js   = new JRadioButton("JavaScript");
        JRadioButton mix  = new JRadioButton("Mixed (both)");
        mix.setSelected(true);
        ButtonGroup g = new ButtonGroup();
        g.add(java); g.add(js); g.add(mix);

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(6, 6, 6, 6));
        p.add(new JLabel("Choose quiz deck:"));
        p.add(java); p.add(js); p.add(mix);

        int opt = JOptionPane.showConfirmDialog(
            null, p, "Select Deck", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE
        );
        if (opt == JOptionPane.OK_OPTION) {
            if (java.isSelected()) { engine = new QuizEngine(Decks.javaDeck(), QUIZ_LENGTH); modeLabel = "Java"; }
            else if (js.isSelected()) { engine = new QuizEngine(Decks.jsDeck(), QUIZ_LENGTH); modeLabel = "JavaScript"; }
            else { engine = new QuizEngine(Decks.mixed(), QUIZ_LENGTH); modeLabel = "Mixed"; }
        } else { // default
            engine = new QuizEngine(Decks.mixed(), QUIZ_LENGTH);
            modeLabel = "Mixed";
        }
    }

    private void buildUI() {
        setTitle("Web Dev Flashcards");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(760, 440);
        setLocationByPlatform(true);

        Image icon = loadIcon(ICON_PATH, 128);
        if (icon != null) setIconImage(icon);

        JPanel root = new JPanel(new BorderLayout());
        root.setBorder(new EmptyBorder(12, 12, 12, 12));
        setContentPane(root);

        // header
        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(new EmptyBorder(8,12,8,12));
        header.setBackground(Color.WHITE);
        titleLabel = new JLabel("  " + modeLabel + " • Flashcards");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 22f));
        titleLabel.setIcon(new ImageIcon(loadIcon(ICON_PATH, 24)));
        header.add(titleLabel, BorderLayout.WEST);
        root.add(header, BorderLayout.NORTH);

        // center
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBorder(new EmptyBorder(16,18,16,18));
        cardPanel.setBackground(Color.WHITE);

        questionLabel = new JLabel(" ");
        questionLabel.setFont(questionLabel.getFont().deriveFont(Font.BOLD, 24f));
        questionLabel.setAlignmentX(CENTER_ALIGNMENT);
        questionLabel.setBorder(new EmptyBorder(0,0,14,0));

        answerField = new JTextField(28);
        answerField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        answerField.setFont(answerField.getFont().deriveFont(16f));

        JPanel row = new JPanel();
        row.setOpaque(false);
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        row.add(answerField);
        row.add(Box.createHorizontalStrut(10));
        submitButton = new JButton("Submit");
        stylizePrimary(submitButton);
        row.add(submitButton);

        feedbackLabel = new JLabel(" ");
        feedbackLabel.setFont(feedbackLabel.getFont().deriveFont(Font.PLAIN, 15f));
        feedbackLabel.setAlignmentX(CENTER_ALIGNMENT);
        feedbackLabel.setBorder(new EmptyBorder(10,0,0,0));

        cardPanel.add(questionLabel);
        cardPanel.add(row);
        cardPanel.add(feedbackLabel);
        root.add(cardPanel, BorderLayout.CENTER);

        // footer
        JPanel status = new JPanel(new BorderLayout());
        status.setOpaque(false);
        status.setBorder(new EmptyBorder(10,0,0,0));

        progress = new JProgressBar(0, engine.getRoundLength());
        progress.setValue(0);
        progress.setStringPainted(true);

        JPanel right = new JPanel();
        right.setOpaque(false);
        streakLabel = new JLabel("Streak: 0");
        scoreLabel = new JLabel("Score: 0/" + engine.getRoundLength());
        scoreLabel.setBorder(new EmptyBorder(0,12,0,0));
        right.add(streakLabel);
        right.add(scoreLabel);

        status.add(progress, BorderLayout.CENTER);
        status.add(right, BorderLayout.EAST);
        root.add(status, BorderLayout.SOUTH);

        // events
        submitButton.addActionListener(e -> onSubmitOrNext());
        answerField.addActionListener(e -> onSubmitOrNext());

        // menu
        JMenuBar mb = new JMenuBar();
        JMenu game = new JMenu("Game");
        JMenuItem restart = new JMenuItem("Restart");
        restart.addActionListener(e -> { engine.restart(); resetUI(); nextCard(); });
        JMenuItem quit = new JMenuItem("Quit");
        quit.addActionListener(e -> System.exit(0));
        game.add(restart); game.addSeparator(); game.add(quit);
        mb.add(game);
        setJMenuBar(mb);
    }

    private void stylizePrimary(JButton b) {
        b.setBackground(PRIMARY);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setBorder(BorderFactory.createEmptyBorder(8,14,8,14));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { b.setBackground(PRIMARY.darker()); }
            public void mouseExited (MouseEvent e) { b.setBackground(PRIMARY); }
        });
    }

    private Image loadIcon(String path, int size) {
        try {
            Image img = new ImageIcon(path).getImage();
            return img.getScaledInstance(size, size, Image.SCALE_SMOOTH);
        } catch (Exception ignored) { return null; }
    }

    // --- flow ---
    private void startRound() { resetUI(); nextCard(); }

    private void resetUI() {
        awaitingNext = false;
        progress.setMaximum(engine.getRoundLength());
        progress.setValue(0);
        scoreLabel.setText("Score: 0/" + engine.getRoundLength());
        streakLabel.setText("Streak: 0");
        feedbackLabel.setText(" ");
        submitButton.setText("Submit");
        titleLabel.setText("  " + modeLabel + " • Flashcards");
        titleLabel.setIcon(new ImageIcon(loadIcon(ICON_PATH, 24)));
    }

    private void nextCard() {
        if (!engine.hasMore()) { showResults(); return; }
        Card c = engine.nextCard();
        questionLabel.setText("<html><div style='text-align:center;'>Question "
            + (engine.getAsked() + 1) + ": " + c.question + "</div></html>");
        feedbackLabel.setText(" ");
        answerField.setText("");
        answerField.setEditable(true);
        answerField.requestFocusInWindow();
        submitButton.setText("Submit");
    }

    private void onSubmitOrNext() {
        if (awaitingNext) { awaitingNext = false; nextCard(); return; }
        boolean ok = engine.check(answerField.getText());
        if (ok) {
            feedbackLabel.setText("✅ Correct!");
            feedbackLabel.setForeground(new Color(32,140,68));
        } else {
            feedbackLabel.setText("❌ Correct: " + engine.correctAnswer());
            feedbackLabel.setForeground(new Color(180,36,36));
        }
        progress.setValue(engine.getProgress());
        streakLabel.setText("Streak: " + engine.getStreak());
        scoreLabel.setText("Score: " + engine.getScore() + "/" + engine.getRoundLength());
        answerField.setEditable(false);
        submitButton.setText(engine.hasMore() ? "Next →" : "See Results");
        awaitingNext = true;
    }

    private void showResults() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(new EmptyBorder(14,18,6,18));
        JLabel top = new JLabel("Game Over! Final Score: " + engine.getScore() + "/" + engine.getRoundLength(),
                SwingConstants.CENTER);
        top.setFont(top.getFont().deriveFont(Font.BOLD, 18f));
        p.add(top, BorderLayout.NORTH);

        Image ic = loadIcon(ICON_PATH, 96);
        p.add(new JLabel(ic != null ? new ImageIcon(ic) : null, SwingConstants.CENTER), BorderLayout.CENTER);

        String[] options = {"Play Again", "Quit"};
        int opt = JOptionPane.showOptionDialog(this, p, "Results",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, options, options[0]);
        if (opt == 0) { engine.restart(); resetUI(); nextCard(); }
        else System.exit(0);
    }
}
