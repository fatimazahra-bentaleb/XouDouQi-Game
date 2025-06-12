package org.example;

public enum Player {
    PLAYER1("Joueur 1", "1"),
    PLAYER2("Joueur 2", "2");

    private final String name;
    private final String symbol;

    Player(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    public String getName() { return name; }
    public String getSymbol() { return symbol; }

    public Player getOpponent() {
        return this == PLAYER1 ? PLAYER2 : PLAYER1;
    }
}
