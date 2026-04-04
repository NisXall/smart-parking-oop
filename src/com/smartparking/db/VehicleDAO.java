package com.smartparking.db;

import com.smartparking.model.Vehicle;
import com.smartparking.model.VehicleRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VehicleDAO {

    public boolean isVehicleCurrentlyParked(Connection connection, String vehicleNumber) throws SQLException {
        String sql = "SELECT id FROM vehicles WHERE vehicle_number = ? AND exit_time IS NULL LIMIT 1";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, vehicleNumber);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void insertVehicle(Connection connection, Vehicle vehicle) throws SQLException {
        String sql = "INSERT INTO vehicles(vehicle_number, owner_name, slot_id, entry_time) VALUES(?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, vehicle.getVehicleNumber());
            ps.setString(2, vehicle.getOwnerName());
            ps.setInt(3, vehicle.getSlotId());
            ps.setString(4, vehicle.getEntryTime());
            ps.executeUpdate();
        }
    }

    public VehicleRecord findActiveVehicleByNumber(Connection connection, String vehicleNumber) throws SQLException {
        String sql = "SELECT v.id, v.vehicle_number, v.owner_name, v.slot_id, v.entry_time, s.slot_number "
                + "FROM vehicles v "
                + "JOIN parking_slots s ON v.slot_id = s.id "
                + "WHERE v.vehicle_number = ? AND v.exit_time IS NULL "
                + "ORDER BY v.id DESC LIMIT 1";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, vehicleNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    VehicleRecord record = new VehicleRecord();
                    record.setId(rs.getInt("id"));
                    record.setVehicleNumber(rs.getString("vehicle_number"));
                    record.setOwnerName(rs.getString("owner_name"));
                    record.setSlotId(rs.getInt("slot_id"));
                    record.setEntryTime(rs.getString("entry_time"));
                    record.setSlotNumber(rs.getInt("slot_number"));
                    return record;
                }
            }
        }
        return null;
    }

    public void updateVehicleExit(Connection connection, int vehicleId, String exitTime, double fee) throws SQLException {
        String sql = "UPDATE vehicles SET exit_time = ?, fee = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, exitTime);
            ps.setDouble(2, fee);
            ps.setInt(3, vehicleId);
            ps.executeUpdate();
        }
    }

    public List<VehicleRecord> getAllVehicleRecords() {
        String sql = "SELECT v.id, v.vehicle_number, v.owner_name, v.slot_id, v.entry_time, v.exit_time, v.fee, s.slot_number "
                + "FROM vehicles v "
                + "JOIN parking_slots s ON v.slot_id = s.id "
                + "ORDER BY v.id DESC";

        List<VehicleRecord> records = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                VehicleRecord record = new VehicleRecord();
                record.setId(rs.getInt("id"));
                record.setVehicleNumber(rs.getString("vehicle_number"));
                record.setOwnerName(rs.getString("owner_name"));
                record.setSlotId(rs.getInt("slot_id"));
                record.setEntryTime(rs.getString("entry_time"));
                record.setExitTime(rs.getString("exit_time"));

                double feeValue = rs.getDouble("fee");
                if (!rs.wasNull()) {
                    record.setFee(feeValue);
                }

                record.setSlotNumber(rs.getInt("slot_number"));
                records.add(record);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch vehicle records", e);
        }

        return records;
    }
}
