package org.example;

import java.sql.*;
import java.util.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

class DatabaseManager {
    private final String DB_URL;
    private final String DB_USER;
    private final String DB_PASSWORD;

    public DatabaseManager() {
        Properties props = loadConfig();
        this.DB_URL = props.getProperty("db.url");
        this.DB_USER = props.getProperty("db.user");
        this.DB_PASSWORD = props.getProperty("db.password");

    }

    private Properties loadConfig() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("database.properties")) {
            if (input == null) {
                System.err.println("Fichier database.properties non trouvé !");
                // Valeurs par défaut
                props.setProperty("db.url", "jdbc:h2:~/test;AUTO_SERVER=TRUE");
                props.setProperty("db.user", "sa");
                props.setProperty("db.password", "");
                return props;
            }
            props.load(input);
        } catch (IOException ex) {
            System.err.println("Erreur de lecture du fichier de configuration");
            ex.printStackTrace();
        }
        return props;
    }
    public void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Créer la table des joueurs
            String createPlayersTable = """
                CREATE TABLE IF NOT EXISTS players (
                    id IDENTITY PRIMARY KEY,
                    username VARCHAR(255) UNIQUE NOT NULL,
                    password VARCHAR(255) NOT NULL
                )
            """;

            // Créer la table des parties
            String createGamesTable = """
                CREATE TABLE IF NOT EXISTS games (
                    id IDENTITY PRIMARY KEY,
                    player1 VARCHAR(255) NOT NULL,
                    player2 VARCHAR(255) NOT NULL,
                    winner VARCHAR(255),
                    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """;

            conn.createStatement().execute(createPlayersTable);
            conn.createStatement().execute(createGamesTable);

            System.out.println("Base de données H2 initialisée avec succès!");

        } catch (SQLException e) {
            System.err.println("Erreur lors de l'initialisation de la base de données H2: " + e.getMessage());
        }
    }

    public boolean registerPlayer(String username, String password) {
        try (Connection conn =DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "INSERT INTO players (username, password) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean authenticatePlayer(String username, String password) {
        try (Connection conn =DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT password FROM players WHERE username = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("password").equals(password);
            }
            return false;
        } catch (SQLException e) {
            return false;
        }
    }

    public void saveGame(String player1, String player2, String winner) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "INSERT INTO games (player1, player2, winner) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, player1);
            pstmt.setString(2, player2);
            pstmt.setString(3, winner);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la sauvegarde: " + e.getMessage());
        }
    }

    public void showGameHistory(String username) {
        try (Connection conn =DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = """
                SELECT player1, player2, winner, date 
                FROM games 
                WHERE player1 = ? OR player2 = ? 
                ORDER BY date DESC
            """;
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, username);
            ResultSet rs = pstmt.executeQuery();

            System.out.println("\n=== Historique des parties ===");
            int victories = 0, defeats = 0;

            while (rs.next()) {
                String p1 = rs.getString("player1");
                String p2 = rs.getString("player2");
                String winner = rs.getString("winner");
                String date = rs.getString("date");

                System.out.printf("%s vs %s - Vainqueur: %s (%s)\n", p1, p2, winner, date);

                if (winner.equals(username)) victories++;
                else defeats++;
            }

            System.out.printf("Victoires: %d, Défaites: %d\n", victories, defeats);

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'historique: " + e.getMessage());
        }
    }
}