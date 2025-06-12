package org.example;

class XouDouQiGame {
    private static final int ROWS = 9;
    private static final int COLS = 7;

    private Cell[][] board;
    private Player currentPlayer;
    private boolean gameOver;
    private String winner;

    public XouDouQiGame() {
        initializeBoard();
        currentPlayer = Player.PLAYER1;
        gameOver = false;
    }

    private void initializeBoard() {
        board = new Cell[ROWS][COLS];

        // Initialiser toutes les cases comme normales
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                board[i][j] = new Cell(CellType.NORMAL);
            }
        }

        // Placer les lacs (eau)
        for (int i = 3; i <= 5; i++) {
            for (int j = 1; j <= 2; j++) {
                board[i][j] = new Cell(CellType.WATER);
            }
            for (int j = 4; j <= 5; j++) {
                board[i][j] = new Cell(CellType.WATER);
            }
        }

        // Placer les sanctuaires
        board[0][3] = new Cell(CellType.SANCTUARY);
        board[8][3] = new Cell(CellType.SANCTUARY);

        // Placer les pièges du joueur 1 (en haut)
        board[0][2] = new Cell(CellType.TRAP, Player.PLAYER1);
        board[0][4] = new Cell(CellType.TRAP, Player.PLAYER1);
        board[1][3] = new Cell(CellType.TRAP, Player.PLAYER1);

        // Placer les pièges du joueur 2 (en bas)
        board[8][2] = new Cell(CellType.TRAP, Player.PLAYER2);
        board[8][4] = new Cell(CellType.TRAP, Player.PLAYER2);
        board[7][3] = new Cell(CellType.TRAP, Player.PLAYER2);

        // Placer les pièces initiales
        placePieces();
    }

    private void placePieces() {
        // Pièces du joueur 2 (en haut)
        board[0][0].setPiece(new Piece(AnimalType.LION, Player.PLAYER2));
        board[0][6].setPiece(new Piece(AnimalType.TIGRE, Player.PLAYER2));
        board[1][1].setPiece(new Piece(AnimalType.CHIEN, Player.PLAYER2));
        board[1][5].setPiece(new Piece(AnimalType.CHAT, Player.PLAYER2));
        board[2][0].setPiece(new Piece(AnimalType.ELEPHANT, Player.PLAYER2));
        board[2][2].setPiece(new Piece(AnimalType.LOUP, Player.PLAYER2));
        board[2][4].setPiece(new Piece(AnimalType.PANTHERE, Player.PLAYER2));
        board[2][6].setPiece(new Piece(AnimalType.RAT, Player.PLAYER2));

        // Pièces du joueur 1 (en bas)
        board[8][0].setPiece(new Piece(AnimalType.TIGRE, Player.PLAYER1));
        board[8][6].setPiece(new Piece(AnimalType.LION, Player.PLAYER1));
        board[7][1].setPiece(new Piece(AnimalType.CHAT, Player.PLAYER1));
        board[7][5].setPiece(new Piece(AnimalType.CHIEN, Player.PLAYER1));
        board[6][0].setPiece(new Piece(AnimalType.RAT, Player.PLAYER1));
        board[6][2].setPiece(new Piece(AnimalType.PANTHERE, Player.PLAYER1));
        board[6][4].setPiece(new Piece(AnimalType.LOUP, Player.PLAYER1));
        board[6][6].setPiece(new Piece(AnimalType.ELEPHANT, Player.PLAYER1));
    }

    public void displayBoard() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("Tour de " + currentPlayer.getName());
        System.out.println("=".repeat(50));

        // Affichage des coordonnées des colonnes
        System.out.print("   ");
        for (int j = 0; j < COLS; j++) {
            System.out.printf("%3d", j);
        }
        System.out.println();

        for (int i = 0; i < ROWS; i++) {
            System.out.printf("%2d ", i);
            for (int j = 0; j < COLS; j++) {
                Cell cell = board[i][j];
                String display = "  .";

                if (cell.getPiece() != null) {
                    display = String.format("%3s", cell.getPiece().toString());
                } else {
                    switch (cell.getType()) {
                        case WATER -> display = "  ~";
                        case TRAP -> display = "  X";
                        case SANCTUARY -> display = "  S";
                        default -> display = "  .";
                    }
                }
                System.out.print(display);
            }
            System.out.println();
        }
        System.out.println();
    }

    public boolean makeMove(int fromRow, int fromCol, int toRow, int toCol) {
        // Vérifier les limites
        if (!isValidPosition(fromRow, fromCol) || !isValidPosition(toRow, toCol)) {
            System.out.println("Position invalide!");
            return false;
        }

        Cell fromCell = board[fromRow][fromCol];
        Cell toCell = board[toRow][toCol];

        // Vérifier qu'il y a une pièce à déplacer
        if (fromCell.getPiece() == null) {
            System.out.println("Aucune pièce à cette position!");
            return false;
        }

        Piece piece = fromCell.getPiece();

        // Vérifier que c'est bien la pièce du joueur actuel
        if (piece.getOwner() != currentPlayer) {
            System.out.println("Ce n'est pas votre pièce!");
            return false;
        }

        // Vérifier si le mouvement est valide
        if (!isValidMove(fromRow, fromCol, toRow, toCol)) {
            return false;
        }

        // Effectuer le mouvement
        fromCell.setPiece(null);
        toCell.setPiece(piece);

        // Vérifier les conditions de victoire
        checkWinCondition();

        // Changer de joueur
        if (!gameOver) {
            currentPlayer = currentPlayer.getOpponent();
        }

        return true;
    }

    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < ROWS && col >= 0 && col < COLS;
    }

    private boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol) {
        Piece piece = board[fromRow][fromCol].getPiece();
        Cell toCell = board[toRow][toCol];

        // Vérifier que ce n'est qu'un mouvement d'une case (sauf pour lion/tigre sautant)
        int rowDiff = Math.abs(toRow - fromRow);
        int colDiff = Math.abs(toCol - fromCol);

        // Mouvement diagonal interdit
        if (rowDiff > 0 && colDiff > 0) {
            System.out.println("Mouvement diagonal interdit!");
            return false;
        }

        // Vérifier les sauts de lion/tigre
        if ((piece.getType() == AnimalType.LION || piece.getType() == AnimalType.TIGRE) &&
                (rowDiff > 1 || colDiff > 1)) {
            if (!canJumpOverWater(fromRow, fromCol, toRow, toCol)) {
                return false;
            }
        } else if (rowDiff > 1 || colDiff > 1) {
            System.out.println("Mouvement trop long!");
            return false;
        }

        // Vérifier les restrictions de case
        if (!canEnterCell(piece, toCell)) {
            return false;
        }

        // Vérifier les captures
        if (toCell.getPiece() != null) {
            if (!canCapturePiece(piece, toCell.getPiece(), toCell)) {
                return false;
            }
        }

        return true;
    }

    private boolean canJumpOverWater(int fromRow, int fromCol, int toRow, int toCol) {
        // Vérifier s'il y a un rat dans l'eau sur la trajectoire
        if (fromRow == toRow) { // Saut horizontal
            int startCol = Math.min(fromCol, toCol) + 1;
            int endCol = Math.max(fromCol, toCol) - 1;
            for (int col = startCol; col <= endCol; col++) {
                if (board[fromRow][col].getType() == CellType.WATER &&
                        board[fromRow][col].getPiece() != null &&
                        board[fromRow][col].getPiece().getType() == AnimalType.RAT) {
                    System.out.println("Un rat bloque le saut!");
                    return false;
                }
            }
        } else if (fromCol == toCol) { // Saut vertical
            int startRow = Math.min(fromRow, toRow) + 1;
            int endRow = Math.max(fromRow, toRow) - 1;
            for (int row = startRow; row <= endRow; row++) {
                if (board[row][fromCol].getType() == CellType.WATER &&
                        board[row][fromCol].getPiece() != null &&
                        board[row][fromCol].getPiece().getType() == AnimalType.RAT) {
                    System.out.println("Un rat bloque le saut!");
                    return false;
                }
            }
        }
        return true;
    }

    private boolean canEnterCell(Piece piece, Cell cell) {
        // Ne peut pas entrer dans son propre sanctuaire
        if (cell.getType() == CellType.SANCTUARY) {
            if ((piece.getOwner() == Player.PLAYER1 && board[8][3] == cell) ||
                    (piece.getOwner() == Player.PLAYER2 && board[0][3] == cell)) {
                System.out.println("Impossible d'entrer dans son propre sanctuaire!");
                return false;
            }
        }

        // Seul le rat peut entrer dans l'eau
        if (cell.getType() == CellType.WATER && piece.getType() != AnimalType.RAT) {
            System.out.println("Seul le rat peut aller dans l'eau!");
            return false;
        }

        return true;
    }

    private boolean canCapturePiece(Piece attacker, Piece defender, Cell defenderCell) {
        // Dans un piège, la pièce ennemie peut être prise par n'importe qui
        if (defenderCell.getType() == CellType.TRAP &&
                defenderCell.getTrapOwner() != defender.getOwner()) {
            return true;
        }

        // Le rat ne peut pas capturer en sortant de l'eau
        if (attacker.getType() == AnimalType.RAT) {
            // Vérifier si le rat vient de l'eau (il faudrait garder l'état précédent)
            // Pour simplifier, on considère cette règle comme optionnelle
        }

        if (!attacker.canCapture(defender)) {
            System.out.println("Impossible de capturer cette pièce!");
            return false;
        }

        return true;
    }

    private void checkWinCondition() {
        // Vérifier si une pièce a atteint le sanctuaire ennemi
        if (board[0][3].getPiece() != null && board[0][3].getPiece().getOwner() == Player.PLAYER1) {
            gameOver = true;
            winner = Player.PLAYER1.getName();
        } else if (board[8][3].getPiece() != null && board[8][3].getPiece().getOwner() == Player.PLAYER2) {
            gameOver = true;
            winner = Player.PLAYER2.getName();
        }

        if (gameOver) {
            System.out.println("\n🎉 " + winner + " a gagné! 🎉");
        }
    }

    public boolean isGameOver() { return gameOver; }
    public String getWinner() { return winner; }
    public Player getCurrentPlayer() { return currentPlayer; }
}