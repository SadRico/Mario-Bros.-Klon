package model;

import java.sql.*;
import java.util.*;

public class Database {
    private Connection connection;

    public Database() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:mario_scores.db");
            createTable();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    private void createTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS scores (id INTEGER PRIMARY KEY, name TEXT, time INTEGER, score INTEGER, date TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
        try (Statement stmt = connection.createStatement()) { stmt.execute(sql); }
    }

    public void saveScore(String name, long time, int score) throws SQLException {
        String sql = "INSERT INTO scores(name,time,score) VALUES(?,?,?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setLong(2, time);
            pstmt.setInt(3, score);
            pstmt.executeUpdate();
        }
    }

    public List<ScoreEntry> getTopScores(int limit) throws SQLException {
        String sql = "SELECT name,time,score FROM scores ORDER BY time ASC LIMIT ?";
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

    public void close() throws SQLException { if (connection != null) connection.close(); }
}