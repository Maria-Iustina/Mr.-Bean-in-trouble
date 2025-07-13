package PaooGame.Graphics;

import java.sql.*;
import java.util.ArrayList;
import java.io.File;

public class DataBase {
    private static DataBase dataBaseInstance;
    private Connection c;
    private final String DB_NAME = "Game.db";
    private final String DB_URL = "jdbc:sqlite:" + DB_NAME;

    private DataBase() {
        initializeDatabase();
    }

    public static DataBase getInstance() {
        if (dataBaseInstance == null) {
            dataBaseInstance = new DataBase();
        }
        return dataBaseInstance;
    }

    private void initializeDatabase() {
        try {
            // Check if SQLite JDBC driver is available
            Class.forName("org.sqlite.JDBC");

            // Check if database file exists
            File dbFile = new File(DB_NAME);
            boolean needToCreateTable = !dbFile.exists();

            // Connect to database (creates it if it doesn't exist)
            c = DriverManager.getConnection(DB_URL);
            c.setAutoCommit(false);

            // If database didn't exist before, create table structure
            if (needToCreateTable) {
                System.out.println("Creating database structure...");
                try (Statement stmt = c.createStatement()) {
                    String sql = "CREATE TABLE IF NOT EXISTS GAME " +
                            "(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                            " LEVEL_INDEX INT NOT NULL, " +
                            " HEARTS INT NOT NULL, " +
                            " SCORE INT NOT NULL, " +
                            " X_POSITION INT NOT NULL, " +
                            " Y_POSITION INT NOT NULL)";
                    stmt.executeUpdate(sql);
                }
                c.commit();
                System.out.println("GAME table created successfully!");
            }

            c.close();
            System.out.println("Database initialized at: " + new File(DB_NAME).getAbsolutePath());
        } catch (Exception e) {
            System.err.println("Database initialization error: " + e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void saveGame(int levelIndex, int hearts, int score, int xPos, int yPos) {
        System.out.println("Saving game...");

        try {
            // Connect to database
            c = DriverManager.getConnection(DB_URL);
            c.setAutoCommit(false);

            // Delete previous saves - we only keep the latest one
            try (Statement stmt = c.createStatement()) {
                stmt.executeUpdate("DELETE FROM GAME;");
            }

            // Insert new save data
            String sql = "INSERT INTO GAME (LEVEL_INDEX, HEARTS, SCORE, X_POSITION, Y_POSITION) " +
                    "VALUES (?, ?, ?, ?, ?);";
            try (PreparedStatement pstmt = c.prepareStatement(sql)) {
                pstmt.setInt(1, levelIndex);
                pstmt.setInt(2, hearts);
                pstmt.setInt(3, score);
                pstmt.setInt(4, xPos);
                pstmt.setInt(5, yPos);
                pstmt.executeUpdate();
            }

            c.commit();
            System.out.println("Game saved successfully!");
        } catch (Exception e) {
            System.err.println("Error saving game: " + e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
            try {
                if (c != null) {
                    c.rollback(); // Roll back changes if error occurs
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (c != null) {
                    c.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<Integer> loadGame() {
        ArrayList<Integer> loadGameValues = new ArrayList<>();

        try {
            // Connect to database
            c = DriverManager.getConnection(DB_URL);
            c.setAutoCommit(false);

            try (Statement stmt = c.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM GAME;")) {

                if (rs.next()) {
                    loadGameValues.add(rs.getInt("LEVEL_INDEX"));
                    loadGameValues.add(rs.getInt("HEARTS"));
                    loadGameValues.add(rs.getInt("SCORE"));
                    loadGameValues.add(rs.getInt("X_POSITION"));
                    loadGameValues.add(rs.getInt("Y_POSITION"));
                    System.out.println("Game loaded successfully!");
                } else {
                    System.out.println("No saved game data found!");
                }
            }

            c.commit();
        } catch (Exception e) {
            System.err.println("Error loading game: " + e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (c != null) {
                    c.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return loadGameValues;
    }

    // Helper method to check if there's a saved game
    public boolean hasSavedGame() {
        boolean hasGame = false;

        try {
            c = DriverManager.getConnection(DB_URL);
            try (Statement stmt = c.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS count FROM GAME;")) {

                if (rs.next()) {
                    hasGame = rs.getInt("count") > 0;
                }
            }
        } catch (Exception e) {
            System.err.println("Error checking for saved game: " + e.getMessage());
        } finally {
            try {
                if (c != null) {
                    c.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return hasGame;
    }
}