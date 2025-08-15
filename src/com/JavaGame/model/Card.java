package com.JavaGame.model;

public class Card {
    public final String question;
    public final String[] answers;   // acceptable variants
    public final String category;    // "Java" or "JavaScript"

    public Card(String q, String category, String... answers) {
        this.question = q;
        this.answers = answers;
        this.category = category;
    }
}
