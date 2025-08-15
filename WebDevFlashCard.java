import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class WebDevFlashCard {
    private JFrame frame;
    private JLabel questionLabel;
    private JTextField answerField;
    private JButton submitButton;
    private JLabel feedbackLabel;
    private JLabel scoreLabel;

    private String[][] cards = {
        {"HTML stands for?", "HyperText Markup Language"},
        {"Which CSS property changes text color?", "color"},
        {"What does DOM stand for?", "Document Object Model"},
        {"JavaScript keyword to declare a variable (ES6)?", "let"},
        {"HTML tag for the largest heading?", "<h1>"},
        {"CSS unit for relative font size to the parent?", "em"},
        {"What method logs to the console in JavaScript?", "console.log"},
        {"Which HTML attribute is used for alternate text in images?", "alt"},
        {"CSS property to set background color?", "background-color"},
        {"JavaScript method to select element by ID?", "getElementById"}
    };

    private int score = 0;
    private int questionsAsked = 0;
    private ArrayList<Integer> askedIndexes = new ArrayList<>();
    private Random rand = new Random();
    private int currentIndex;

    public WebDevFlashCard() {
        frame = new JFrame("🌐 Web Dev Flashcards");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);
        frame.setLayout(new BorderLayout());

        // Set the window icon
        try {
            Image icon = new ImageIcon("assets/icon/flashicon.png").getImage();
            frame.setIconImage(icon);
        } catch (Exception e){
            System.err.println("Couldn't Load Image");
        }

        // Question Panel
        JPanel topPanel = new JPanel();
        questionLabel = new JLabel("", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 18));
        topPanel.add(questionLabel);

        // Answer Panel
        JPanel centerPanel = new JPanel(new FlowLayout());
        answerField = new JTextField(20);
        submitButton = new JButton("Submit");
        centerPanel.add(answerField);
        centerPanel.add(submitButton);

        // Feedback + Score Panel
        JPanel bottomPanel = new JPanel(new GridLayout(2, 1));
        feedbackLabel = new JLabel(" ", SwingConstants.CENTER);
        scoreLabel = new JLabel("Score: 0", SwingConstants.CENTER);
        bottomPanel.add(feedbackLabel);
        bottomPanel.add(scoreLabel);

        // Add panels to frame
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(centerPanel, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        // Button Action
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                checkAnswer();
            }
        });

        // Allow Enter key to submit
        answerField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                checkAnswer();
            }
        });

        nextQuestion();
        frame.setVisible(true);
    }

    private void nextQuestion() {
        if (questionsAsked >= 5) {
            JOptionPane.showMessageDialog(frame,
                "Game Over! Final Score: " + score + "/5",
                "Results", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
        do {
            currentIndex = rand.nextInt(cards.length);
        } while (askedIndexes.contains(currentIndex));
        askedIndexes.add(currentIndex);
        questionLabel.setText("Question " + (questionsAsked + 1) + ": " + cards[currentIndex][0]);
        answerField.setText("");
        feedbackLabel.setText(" ");
    }

    private void checkAnswer() {
        String answer = answerField.getText().trim();
        if (answer.equalsIgnoreCase(cards[currentIndex][1])) {
            feedbackLabel.setText("✅ Correct!");
            score++;
        } else {
            feedbackLabel.setText("❌ Correct Answer: " + cards[currentIndex][1]);
        }
        questionsAsked++;
        scoreLabel.setText("Score: " + score);
        nextQuestion();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WebDevFlashCard());
    }
}
