package org.example;

public class Piece {
    private AnimalType type;
    private Player owner;

    public Piece(AnimalType type, Player owner) {
        this.type = type;
        this.owner = owner;
    }

    public AnimalType getType() { return type; }
    public Player getOwner() { return owner; }

    public boolean canCapture(Piece other) {
        if (other == null || other.owner == this.owner) return false;

        // Le rat peut capturer l'éléphant
        if (this.type == AnimalType.RAT && other.type == AnimalType.ELEPHANT) {
            return true;
        }

        // Sinon, peut capturer si rang égal ou inférieur
        return this.type.getRank() >= other.type.getRank();
    }

    @Override
    public String toString() {
        return owner.getSymbol() + type.getSymbol();
    }
}
