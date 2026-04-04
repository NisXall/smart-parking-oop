package com.smartparking.service;

import com.smartparking.model.DashboardStats;
import com.smartparking.model.VehicleRecord;

import java.util.List;

public interface ParkingManager {
    ParkingResult parkVehicle(String vehicleNumber, String ownerName);

    RemoveResult removeVehicle(String vehicleNumber);

    DashboardStats getDashboardStats();

    List<VehicleRecord> getAllVehicleRecords();
}
