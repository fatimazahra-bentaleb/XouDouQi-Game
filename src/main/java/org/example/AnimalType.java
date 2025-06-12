package org.example;

public enum AnimalType {
    RAT(1, "R"),
    CHAT(2, "C"),
    LOUP(3, "L"),
    CHIEN(4, "D"),
    PANTHERE(5, "P"),
    TIGRE(6, "T"),
    LION(7, "Li"),
    ELEPHANT(8, "E");

    private final int rank;
    private final String symbol;

    AnimalType(int rank, String symbol) {
        this.rank = rank;
        this.symbol = symbol;
    }

    public int getRank() { return rank; }
    public String getSymbol() { return symbol; }
}

