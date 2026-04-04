package com.smartparking.service;

import com.smartparking.db.DatabaseConnection;
import com.smartparking.db.ParkingSlotDAO;
import com.smartparking.db.VehicleDAO;
import com.smartparking.model.DashboardStats;
import com.smartparking.model.ParkingSlot;
import com.smartparking.model.Vehicle;
import com.smartparking.model.VehicleRecord;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ParkingService implements ParkingManager {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final double HOURLY_RATE = 20.0;

    private final ParkingSlotDAO slotDAO;
    private final VehicleDAO vehicleDAO;

    public ParkingService() {
        this.slotDAO = new ParkingSlotDAO();
        this.vehicleDAO = new VehicleDAO();
    }

    @Override
    public ParkingResult parkVehicle(String vehicleNumber, String ownerName) {
        String cleanVehicleNumber = vehicleNumber == null ? "" : vehicleNumber.trim().toUpperCase();
        String cleanOwnerName = ownerName == null ? "" : ownerName.trim();

        if (cleanVehicleNumber.isEmpty() || cleanOwnerName.isEmpty()) {
            return new ParkingResult(false, "Vehicle number and owner name are required.", -1);
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);

            if (vehicleDAO.isVehicleCurrentlyParked(connection, cleanVehicleNumber)) {
                connection.rollback();
                return new ParkingResult(false, "This vehicle is already parked.", -1);
            }

            ParkingSlot nextSlot = slotDAO.findNextAvailableSlot(connection);
            if (nextSlot == null) {
                connection.rollback();
                return new ParkingResult(false, "No parking slot is available right now.", -1);
            }

            String entryTime = LocalDateTime.now().format(DATE_TIME_FORMATTER);
            Vehicle vehicle = new Vehicle(cleanVehicleNumber, cleanOwnerName, nextSlot.getId(), entryTime);

            vehicleDAO.insertVehicle(connection, vehicle);
            slotDAO.updateSlotStatus(connection, nextSlot.getId(), true);

            connection.commit();
            return new ParkingResult(true, "Vehicle parked successfully.", nextSlot.getSlotNumber());
        } catch (SQLException e) {
            return new ParkingResult(false, "Error while parking vehicle: " + e.getMessage(), -1);
        }
    }

    @Override
    public RemoveResult removeVehicle(String vehicleNumber) {
        String cleanVehicleNumber = vehicleNumber == null ? "" : vehicleNumber.trim().toUpperCase();
        if (cleanVehicleNumber.isEmpty()) {
            return new RemoveResult(false, "Vehicle number is required.", -1, 0.0);
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);

            VehicleRecord record = vehicleDAO.findActiveVehicleByNumber(connection, cleanVehicleNumber);
            if (record == null) {
                connection.rollback();
                return new RemoveResult(false, "Vehicle not found in active parking.", -1, 0.0);
            }

            LocalDateTime entry = LocalDateTime.parse(record.getEntryTime(), DATE_TIME_FORMATTER);
            LocalDateTime exit = LocalDateTime.now();

            long minutes = Duration.between(entry, exit).toMinutes();
            long billableHours = (long) Math.ceil(minutes / 60.0);
            if (billableHours <= 0) {
                billableHours = 1;
            }
            double fee = billableHours * HOURLY_RATE;

            vehicleDAO.updateVehicleExit(connection, record.getId(), exit.format(DATE_TIME_FORMATTER), fee);
            slotDAO.updateSlotStatus(connection, record.getSlotId(), false);

            connection.commit();
            return new RemoveResult(true, "Vehicle removed successfully.", record.getSlotNumber(), fee);
        } catch (Exception e) {
            return new RemoveResult(false, "Error while removing vehicle: " + e.getMessage(), -1, 0.0);
        }
    }

    @Override
    public DashboardStats getDashboardStats() {
        return slotDAO.getDashboardStats();
    }

    @Override
    public List<VehicleRecord> getAllVehicleRecords() {
        return vehicleDAO.getAllVehicleRecords();
    }
}
