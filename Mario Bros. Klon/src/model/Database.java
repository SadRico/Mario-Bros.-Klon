package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private Connection connection;

    public Database() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:mario_scores.db");
            createTable(); // <- WICHTIG!
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS scores (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "time INTEGER NOT NULL," +
                "score INTEGER NOT NULL," +
                "date TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }

    public void saveScore(String name, long time, int score) {
        try {
            if (connection == null) throw new SQLException("Keine Verbindung zur Datenbank");

            String sql = "INSERT INTO scores (name, time, score) VALUES (?, ?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, name);
                ps.setLong(2, time); // <-- hier wird bereits Sekundenwert erwartet
                ps.setInt(3, score);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ScoreEntry> getTopScores(int limit) throws SQLException {
        String sql = "SELECT name, time, score FROM scores ORDER BY score DESC LIMIT ?";
        List<ScoreEntry> list = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, limit);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new ScoreEntry(rs.getString("name"), rs.getLong("time"), rs.getInt("score")));
            }
        }
        return list;
    }

    public void close() throws SQLException {
        if (connection != null) connection.close();
    }
}
