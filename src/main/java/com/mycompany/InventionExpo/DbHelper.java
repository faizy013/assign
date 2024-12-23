package com.mycompany.InventionExpo;
import java.sql.*;
import java.util.ArrayList;

public class DbHelper {
    private Connection connection;

    // Constructor
    public DbHelper(String databasePath) throws ClassNotFoundException, SQLException {
        Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
        connection = DriverManager.getConnection("jdbc:ucanaccess://" + databasePath);
    }

    // Add Inventor to Database
    public boolean addInventor(String name, int score) throws SQLException {
        String query = "INSERT INTO Inventors (Name, Score) VALUES (?, ?)";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, name);
        stmt.setInt(2, score);
        return stmt.executeUpdate() > 0;
    }

    // Fetch All Inventors
    public ArrayList<Inventor> fetchAllInventors() throws SQLException {
        ArrayList<Inventor> inventors = new ArrayList<>();
        String query = "SELECT * FROM Inventors ORDER BY Name ASC";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            inventors.add(new Inventor(rs.getInt("ID"), rs.getString("Name"), rs.getInt("Score")));
        }
        return inventors;
    }

    // Delete All Inventors
    public void deleteAllInventors() throws SQLException {
        String query = "DELETE FROM Inventors";
        Statement stmt = connection.createStatement();
        stmt.executeUpdate(query);
    }

    // Fetch Top 3 Inventors
    public ArrayList<Inventor> fetchTopInventors() throws SQLException {
        ArrayList<Inventor> inventors = new ArrayList<>();
        String query = "SELECT TOP 3 * FROM Inventors ORDER BY Score DESC";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            inventors.add(new Inventor(rs.getInt("ID"), rs.getString("Name"), rs.getInt("Score")));
        }
        return inventors;
    }

    // Close Connection
    public void closeConnection() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}
