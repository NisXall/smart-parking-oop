package com.smartparking.db;

import com.smartparking.model.DashboardStats;
import com.smartparking.model.ParkingSlot;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ParkingSlotDAO {

    public DashboardStats getDashboardStats() {
        String sql = "SELECT "
                + "COUNT(*) AS total_slots, "
                + "SUM(CASE WHEN is_occupied = 0 THEN 1 ELSE 0 END) AS available_slots, "
                + "SUM(CASE WHEN is_occupied = 1 THEN 1 ELSE 0 END) AS occupied_slots "
                + "FROM parking_slots";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return new DashboardStats(
                        rs.getInt("total_slots"),
                        rs.getInt("available_slots"),
                        rs.getInt("occupied_slots")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load dashboard stats", e);
        }

        return new DashboardStats(0, 0, 0);
    }

    public ParkingSlot findNextAvailableSlot(Connection connection) throws SQLException {
        String sql = "SELECT id, slot_number, is_occupied FROM parking_slots WHERE is_occupied = 0 ORDER BY slot_number ASC LIMIT 1";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return new ParkingSlot(
                        rs.getInt("id"),
                        rs.getInt("slot_number"),
                        rs.getInt("is_occupied") == 1
                );
            }
        }
        return null;
    }

    public void updateSlotStatus(Connection connection, int slotId, boolean occupied) throws SQLException {
        String sql = "UPDATE parking_slots SET is_occupied = ? WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, occupied ? 1 : 0);
            ps.setInt(2, slotId);
            ps.executeUpdate();
        }
    }

    public int findSlotNumberById(Connection connection, int slotId) throws SQLException {
        String sql = "SELECT slot_number FROM parking_slots WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, slotId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("slot_number");
                }
            }
        }
        return -1;
    }
}
