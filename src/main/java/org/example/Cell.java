package org.example;

class Cell {
    private CellType type;
    private Piece piece;
    private Player trapOwner; // Pour les pièges

    public Cell(CellType type) {
        this.type = type;
        this.piece = null;
    }

    public Cell(CellType type, Player trapOwner) {
        this.type = type;
        this.trapOwner = trapOwner;
        this.piece = null;
    }

    // Getters et setters
    public CellType getType() { return type; }
    public Piece getPiece() { return piece; }
    public void setPiece(Piece piece) { this.piece = piece; }
    public Player getTrapOwner() { return trapOwner; }

    public boolean isEmpty() { return piece == null; }
}

