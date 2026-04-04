package com.smartparking.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:sqlite:smart_parking.db";

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("SQLite JDBC driver not found.", e);
        }
    }

    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void initializeDatabase(int totalSlots) {
        String createSlotsTable = "CREATE TABLE IF NOT EXISTS parking_slots ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "slot_number INTEGER NOT NULL UNIQUE,"
                + "is_occupied INTEGER NOT NULL DEFAULT 0"
                + ")";

        String createVehiclesTable = "CREATE TABLE IF NOT EXISTS vehicles ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "vehicle_number TEXT NOT NULL,"
                + "owner_name TEXT NOT NULL,"
                + "slot_id INTEGER NOT NULL,"
                + "entry_time TEXT NOT NULL,"
                + "exit_time TEXT,"
                + "fee REAL,"
                + "FOREIGN KEY(slot_id) REFERENCES parking_slots(id)"
                + ")";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(createSlotsTable);
            statement.execute(createVehiclesTable);

            insertDefaultSlotsIfNeeded(connection, totalSlots);
        } catch (SQLException e) {
            throw new RuntimeException("Error initializing database", e);
        }
    }

    private static void insertDefaultSlotsIfNeeded(Connection connection, int totalSlots) throws SQLException {
        int currentCount = 0;
        String countSql = "SELECT COUNT(*) FROM parking_slots";
        try (PreparedStatement ps = connection.prepareStatement(countSql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                currentCount = rs.getInt(1);
            }
        }

        if (currentCount >= totalSlots) {
            return;
        }

        String insertSql = "INSERT INTO parking_slots(slot_number, is_occupied) VALUES(?, 0)";
        try (PreparedStatement ps = connection.prepareStatement(insertSql)) {
            for (int i = currentCount + 1; i <= totalSlots; i++) {
                ps.setInt(1, i);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }
}
