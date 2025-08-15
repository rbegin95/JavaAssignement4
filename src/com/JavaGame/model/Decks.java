package com.JavaGame.model;

import java.util.*;

public final class Decks {
    private Decks() {}

    // --- Java syntax deck ---
    public static List<Card> javaDeck() {
        return Arrays.asList(
            new Card("Java keyword to create a new object?", "Java", "new"),
            new Card("Method name that is the program entry point?", "Java", "main"),
            new Card("Access modifier for everywhere visibility?", "Java", "public"),
            new Card("Keyword to inherit from a superclass?", "Java", "extends"),
            new Card("Keyword to implement an interface?", "Java", "implements"),
            new Card("Primitive type for true/false values?", "Java", "boolean"),
            new Card("Double-precision floating type?", "Java", "double"),
            new Card("Keyword to make a variable constant or a class non-extendable?", "Java", "final"),
            new Card("Method used to compare objects by content?", "Java", "equals"),
            new Card("Keyword to declare the package at the top of a file?", "Java", "package"),
            new Card("Two-word phrase for handling exceptions (hyphen ok)?", "Java", "try-catch", "trycatch"),
            new Card("Loop keyword that iterates while a condition is true?", "Java", "while"),
            new Card("Keyword to call the superclass constructor?", "Java", "super"),
            new Card("Keyword to refer to the current object?", "Java", "this")
        );
    }

    // --- JavaScript syntax deck ---
    public static List<Card> jsDeck() {
        return Arrays.asList(
            new Card("Declare a block-scoped variable (ES6)?", "JavaScript", "let"),
            new Card("Declare a constant (ES6)?", "JavaScript", "const"),
            new Card("Strict equality operator (no coercion)?", "JavaScript", "==="),
            new Card("Log to the console?", "JavaScript", "console.log", "console log"),
            new Card("Modern function syntax name using => ?", "JavaScript", "arrow function", "arrowfunction", "fat arrow"),
            new Card("Array method to add to the end?", "JavaScript", "push"),
            new Card("Array method to remove the last item?", "JavaScript", "pop"),
            new Card("Convert JSON string to object?", "JavaScript", "json.parse", "jsonparse"),
            new Card("DOM method to select by CSS selector?", "JavaScript", "queryselector", "document.queryselector"),
            new Card("String delimiters for template literals?", "JavaScript", "backticks", "`"),
            new Card("Keyword to exit a loop early?", "JavaScript", "break"),
            new Card("Equality operator that coerces types?", "JavaScript", "==")
        );
    }

    public static List<Card> mixed() {
        List<Card> all = new ArrayList<>();
        all.addAll(javaDeck());
        all.addAll(jsDeck());
        return all;
    }
}
