package org.example;

import java.util.Scanner;

public class Main {
    private static final DatabaseManager dbManager = new DatabaseManager();
    private static final Scanner scanner = new Scanner(System.in);

    // Codes couleurs ANSI pour la console
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";
    public static final String BOLD = "\u001B[1m";
    public static final String UNDERLINE = "\u001B[4m";

    // Méthodes utilitaires pour l'affichage
    private static void clearScreen() {
        System.out.print("\033[2J\033[H");
        System.out.flush();
    }

    private static void printSeparator() {
        System.out.println(CYAN + "═".repeat(60) + RESET);
    }

    private static void printTitle(String title) {
        printSeparator();
        System.out.println(BOLD + BLUE + "║" + " ".repeat(20) + title + " ".repeat(20 - title.length() % 20) + "║" + RESET);
        printSeparator();
    }

    private static void printSuccess(String message) {
        System.out.println(GREEN + "✓ " + message + RESET);
    }

    private static void printError(String message) {
        System.out.println(RED + "✗ " + message + RESET);
    }

    private static void printWarning(String message) {
        System.out.println(YELLOW + "⚠ " + message + RESET);
    }

    private static void printInfo(String message) {
        System.out.println(CYAN + "ℹ " + message + RESET);
    }

    public static void main(String[] args) {
        clearScreen();
        dbManager.initializeDatabase();

        // Bannière de bienvenue
        System.out.println(BOLD + PURPLE +
                "╔═══════════════════════════════════════════════════════════╗\n" +
                "║                                                           ║\n" +
                "║           🎮 BIENVENUE DANS XOU DOU QI 🎮                ║\n" +
                "║                                                           ║\n" +
                "║           Jeu de stratégie chinois traditionnel           ║\n" +
                "║                                                           ║\n" +
                "╚═══════════════════════════════════════════════════════════╝" + RESET);

        System.out.println("\n" + YELLOW + "⚡ Initialisation du jeu..." + RESET);

        // Authentification des joueurs
        String player1 = authenticatePlayer("Joueur 1");
        String player2 = authenticatePlayer("Joueur 2");

        printSuccess("Tous les joueurs sont connectés!");

        // Menu principal
        while (true) {
            clearScreen();
            printTitle("MENU PRINCIPAL");
            System.out.println(BOLD + WHITE + "┌─────────────────────────────────────────┐" + RESET);
            System.out.println(BOLD + WHITE + "│  " + GREEN + "1." + WHITE + " 🎯 Nouvelle partie                  │" + RESET);
            System.out.println(BOLD + WHITE + "│  " + BLUE + "2." + WHITE + " 📊 Voir l'historique               │" + RESET);
            System.out.println(BOLD + WHITE + "│  " + RED + "3." + WHITE + " 🚪 Quitter                         │" + RESET);
            System.out.println(BOLD + WHITE + "└─────────────────────────────────────────┘" + RESET);
            System.out.print(BOLD + CYAN + "\n➤ Votre choix: " + RESET);

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consommer la ligne

            switch (choice) {
                case 1 -> playGame(player1, player2);
                case 2 -> showHistory();
                case 3 -> {
                    clearScreen();
                    System.out.println(BOLD + PURPLE +
                            "╔═══════════════════════════════════════════════════════════╗\n" +
                            "║                                                           ║\n" +
                            "║                    👋 AU REVOIR !                        ║\n" +
                            "║                                                           ║\n" +
                            "║              Merci d'avoir joué à Xou Dou Qi             ║\n" +
                            "║                                                           ║\n" +
                            "╚═══════════════════════════════════════════════════════════╝" + RESET);
                    return;
                }
                default -> {
                    printError("Choix invalide! Veuillez choisir une option valide.");
                    System.out.println("\nAppuyez sur Entrée pour continuer...");
                    scanner.nextLine();
                }
            }
        }
    }

    private static String authenticatePlayer(String playerName) {
        clearScreen();
        printTitle("AUTHENTIFICATION " + playerName.toUpperCase());

        System.out.println(BOLD + YELLOW + "🔐 Connexion requise pour " + playerName + RESET + "\n");

        while (true) {
            System.out.println(BOLD + WHITE + "┌─────────────────────────────────────────┐" + RESET);
            System.out.println(BOLD + WHITE + "│  " + GREEN + "1." + WHITE + " 🔑 Se connecter                     │" + RESET);
            System.out.println(BOLD + WHITE + "│  " + BLUE + "2." + WHITE + " 📝 Créer un compte                  │" + RESET);
            System.out.println(BOLD + WHITE + "└─────────────────────────────────────────┘" + RESET);
            System.out.print(BOLD + CYAN + "\n➤ Votre choix: " + RESET);

            int choice = scanner.nextInt();
            scanner.nextLine();

            System.out.print(BOLD + WHITE + "\n👤 Nom d'utilisateur: " + RESET);
            String username = scanner.nextLine();
            System.out.print(BOLD + WHITE + "🔒 Mot de passe: " + RESET);
            String password = scanner.nextLine();

            if (choice == 1) {
                System.out.print(YELLOW + "\n⏳ Vérification en cours..." + RESET);
                try {
                    Thread.sleep(1000); // Simulation de vérification
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                if (dbManager.authenticatePlayer(username, password)) {
                    System.out.println("\r" + " ".repeat(30)); // Effacer la ligne
                    printSuccess("Connexion réussie pour " + username + "!");
                    System.out.println(GREEN + "🎉 Bienvenue " + username + "!" + RESET);
                    System.out.println("\nAppuyez sur Entrée pour continuer...");
                    scanner.nextLine();
                    return username;
                } else {
                    printError("Échec de la connexion! Vérifiez vos identifiants.");
                    System.out.println("\nAppuyez sur Entrée pour réessayer...");
                    scanner.nextLine();
                }
            } else if (choice == 2) {
                System.out.print(YELLOW + "\n⏳ Création du compte..." + RESET);
                try {
                    Thread.sleep(1000); // Simulation de création
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                if (dbManager.registerPlayer(username, password)) {
                    System.out.println("\r" + " ".repeat(30)); // Effacer la ligne
                    printSuccess("Compte créé avec succès!");
                    System.out.println(GREEN + "🎉 Bienvenue " + username + "!" + RESET);
                    System.out.println("\nAppuyez sur Entrée pour continuer...");
                    scanner.nextLine();
                    return username;
                } else {
                    printError("Échec de la création du compte! Ce nom d'utilisateur existe peut-être déjà.");
                    System.out.println("\nAppuyez sur Entrée pour réessayer...");
                    scanner.nextLine();
                }
            } else {
                printError("Choix invalide!");
                System.out.println("\nAppuyez sur Entrée pour continuer...");
                scanner.nextLine();
            }
            clearScreen();
            printTitle("AUTHENTIFICATION " + playerName.toUpperCase());
        }
    }

    private static void playGame(String player1, String player2) {
        clearScreen();
        XouDouQiGame game = new XouDouQiGame();

        // En-tête de la partie
        System.out.println(BOLD + PURPLE +
                "╔═══════════════════════════════════════════════════════════╗\n" +
                "║                                                           ║\n" +
                "║                    🎯 NOUVELLE PARTIE                    ║\n" +
                "║                                                           ║\n" +
                "╚═══════════════════════════════════════════════════════════╝" + RESET);

        System.out.println(BOLD + CYAN + "\n🔥 COMBAT ÉPIQUE:" + RESET);
        System.out.println(BOLD + GREEN + "   🛡️  " + player1 + " (Joueur 1)" + RESET);
        System.out.println(BOLD + WHITE + "        VS" + RESET);
        System.out.println(BOLD + RED + "   ⚔️  " + player2 + " (Joueur 2)" + RESET);

        printSeparator();
        printInfo("Format des commandes: 'fromRow fromCol toRow toCol'");
        printInfo("Exemple: '6 0 5 0' pour déplacer une pièce de (6,0) vers (5,0)");
        System.out.println(BOLD + GREEN + "\n🚀 " + player1 + " commence la partie!" + RESET);
        printSeparator();

        System.out.println("\nAppuyez sur Entrée pour commencer...");
        scanner.nextLine();

        int moveCount = 1;

        while (!game.isGameOver()) {
            clearScreen();

            // Affichage du plateau
            System.out.println(BOLD + BLUE + "═══════════════ PLATEAU DE JEU ═══════════════" + RESET);
            game.displayBoard();
            System.out.println(BOLD + BLUE + "══════════════════════════════════════════════" + RESET);

            // Informations sur le tour
            String currentPlayerName = game.getCurrentPlayer() == Player.PLAYER1 ? player1 : player2;
            String playerNumber = game.getCurrentPlayer() == Player.PLAYER1 ? "Joueur 1" : "Joueur 2";
            String playerColor = game.getCurrentPlayer() == Player.PLAYER1 ? GREEN : RED;
            String playerIcon = game.getCurrentPlayer() == Player.PLAYER1 ? "🛡️" : "⚔️";

            System.out.println(BOLD + YELLOW + "\n📊 TOUR #" + moveCount + RESET);
            System.out.println(playerColor + "🎯 C'est au tour de " + playerIcon + " " + currentPlayerName + " (" + playerNumber + ")" + RESET);

            System.out.println(BOLD + WHITE + "\n┌─────────────────────────────────────────┐" + RESET);
            System.out.println(BOLD + WHITE + "│ Entrez vos coordonnées de mouvement:    │" + RESET);
            System.out.println(BOLD + WHITE + "│ Format: fromRow fromCol toRow toCol     │" + RESET);
            System.out.println(BOLD + WHITE + "└─────────────────────────────────────────┘" + RESET);
            System.out.print(BOLD + playerColor + "➤ Votre mouvement: " + RESET);

            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("quit") || input.equalsIgnoreCase("q")) {
                System.out.println(YELLOW + "🏃‍♂️ Abandon de la partie..." + RESET);
                return;
            }

            String[] parts = input.split(" ");

            if (parts.length != 4) {
                printError("Format invalide! Utilisez: fromRow fromCol toRow toCol");
                printWarning("Exemple: 6 0 5 0");
                System.out.println("\nAppuyez sur Entrée pour continuer...");
                scanner.nextLine();
                continue;
            }

            try {
                int fromRow = Integer.parseInt(parts[0]);
                int fromCol = Integer.parseInt(parts[1]);
                int toRow = Integer.parseInt(parts[2]);
                int toCol = Integer.parseInt(parts[3]);

                // Animation de traitement
                System.out.print(YELLOW + "\n⏳ Traitement du mouvement..." + RESET);
                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                // Tentative de mouvement
                if (game.makeMove(fromRow, fromCol, toRow, toCol)) {
                    System.out.println("\r" + " ".repeat(35)); // Effacer la ligne
                    printSuccess("Mouvement effectué avec succès!");
                    System.out.println(GREEN + "✨ Belle stratégie " + currentPlayerName + "!" + RESET);
                    moveCount++;
                    System.out.println("\nAppuyez sur Entrée pour continuer...");
                    scanner.nextLine();
                } else {
                    System.out.println("\r" + " ".repeat(35)); // Effacer la ligne
                    printError("Mouvement invalide! Vérifiez vos coordonnées.");
                    printWarning("Assurez-vous que:");
                    System.out.println("   • La pièce de départ vous appartient");
                    System.out.println("   • Le mouvement respecte les règles du jeu");
                    System.out.println("   • La destination est valide");
                    System.out.println("\nAppuyez sur Entrée pour réessayer...");
                    scanner.nextLine();
                }
            } catch (NumberFormatException e) {
                printError("Veuillez entrer des nombres valides!");
                printWarning("Utilisez uniquement des chiffres pour les coordonnées.");
                System.out.println("\nAppuyez sur Entrée pour réessayer...");
                scanner.nextLine();
            }
        }

        // Affichage final
        clearScreen();
        game.displayBoard();

        System.out.println(BOLD + PURPLE +
                "╔═══════════════════════════════════════════════════════════╗\n" +
                "║                                                           ║\n" +
                "║                  🏁 FIN DE LA PARTIE                     ║\n" +
                "║                                                           ║\n" +
                "╚═══════════════════════════════════════════════════════════╝" + RESET);

        // Résultats
        String winner = game.getWinner();
        if (winner.equals("Joueur 1")) {
            System.out.println(BOLD + GREEN + "🎉🏆 VICTOIRE DE " + player1.toUpperCase() + "! 🏆🎉" + RESET);
            System.out.println(GREEN + "🛡️  Félicitations pour cette victoire éclatante!" + RESET);
            dbManager.saveGame(player1, player2, player1);
        } else if (winner.equals("Joueur 2")) {
            System.out.println(BOLD + RED + "🎉🏆 VICTOIRE DE " + player2.toUpperCase() + "! 🏆🎉" + RESET);
            System.out.println(RED + "⚔️  Félicitations pour cette victoire éclatante!" + RESET);
            dbManager.saveGame(player1, player2, player2);
        } else {
            System.out.println(BOLD + YELLOW + "🤝 MATCH NUL! 🤝" + RESET);
            System.out.println(YELLOW + "⚖️  Partie équilibrée, bien joué à tous les deux!" + RESET);
            dbManager.saveGame(player1, player2, "Match nul");
        }

        System.out.println(CYAN + "\n💾 Partie sauvegardée dans l'historique." + RESET);
        System.out.println("\nAppuyez sur Entrée pour retourner au menu...");
        scanner.nextLine();
    }

    private static void showHistory() {
        clearScreen();
        printTitle("HISTORIQUE DES PARTIES");

        System.out.println(BOLD + CYAN + "📚 Consultation de l'historique" + RESET + "\n");

        System.out.println(BOLD + WHITE + "┌─────────────────────────────────────────┐" + RESET);
        System.out.println(BOLD + WHITE + "│ Entrez le nom d'utilisateur:            │" + RESET);
        System.out.println(BOLD + WHITE + "└─────────────────────────────────────────┘" + RESET);
        System.out.print(BOLD + CYAN + "➤ Nom d'utilisateur: " + RESET);

        String username = scanner.nextLine();

        System.out.print(YELLOW + "\n⏳ Recherche de l'historique..." + RESET);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("\r" + " ".repeat(35)); // Effacer la ligne

        printSeparator();
        System.out.println(BOLD + GREEN + "📊 Historique de " + username + ":" + RESET);
        printSeparator();

        dbManager.showGameHistory(username);

        printSeparator();
        System.out.println(CYAN + "\nAppuyez sur Entrée pour retourner au menu..." + RESET);
        scanner.nextLine();
    }
}