package com.smartparking;

import com.smartparking.db.DatabaseConnection;
import com.smartparking.gui.DashboardFrame;
import com.smartparking.service.ParkingService;

public class Main {
    public static void main(String[] args) {
        // Initializes tables and creates default slots only on first run.
        DatabaseConnection.initializeDatabase(20);

        ParkingService parkingService = new ParkingService();
        DashboardFrame.open(parkingService);
    }
}
