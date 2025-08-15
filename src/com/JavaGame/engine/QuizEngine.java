package com.JavaGame.engine;

import com.JavaGame.model.Card;
import java.util.*;

public class QuizEngine {
    private final List<Card> deck;
    private final int roundLength;
    private final Random rng = new Random();

    private final Set<Integer> used = new HashSet<>();
    private int currentIndex = -1;
    private int asked = 0;
    private int score = 0;
    private int streak = 0;

    public QuizEngine(List<Card> deck, int roundLength) {
        this.deck = new ArrayList<>(deck);
        Collections.shuffle(this.deck, rng);
        this.roundLength = Math.min(roundLength, this.deck.size());
    }

    public int getRoundLength()     { return roundLength; }
    public int getAsked()           { return asked; }
    public int getScore()           { return score; }
    public int getStreak()          { return streak; }
    public int getProgress()        { return asked; }
    public Card getCurrent()        { return deck.get(currentIndex); }

    public boolean hasMore()        { return asked < roundLength; }

    public Card nextCard() {
        if (!hasMore()) return null;
        int tries = 0;
        do {
            currentIndex = rng.nextInt(deck.size());
        } while (used.contains(currentIndex) && tries++ < 1000);
        used.add(currentIndex);
        return deck.get(currentIndex);
    }

    public boolean check(String userInput) {
        String u = normalize(userInput);
        Card c = deck.get(currentIndex);
        boolean ok = false;
        for (String a : c.answers) {
            if (u.equals(normalize(a))) { ok = true; break; }
        }
        asked++;
        if (ok) { score++; streak++; } else { streak = 0; }
        return ok;
    }

    public String correctAnswer() { return deck.get(currentIndex).answers[0]; }

    public void restart() {
        used.clear();
        asked = 0; score = 0; streak = 0;
        Collections.shuffle(deck, rng);
    }

    // tolerant comparison for syntax answers
    public static String normalize(String s) {
        if (s == null) return "";
        s = s.trim().toLowerCase(Locale.ROOT);
        s = s.replaceAll("[<>.()]", "");   // remove brackets/dots/parens
        s = s.replaceAll("[-_\\s]+", "");  // collapse spaces, dashes, underscores
        return s;
    }
}
