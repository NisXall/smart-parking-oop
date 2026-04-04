package com.smartparking.model;

public class DashboardStats {
    private int totalSlots;
    private int availableSlots;
    private int occupiedSlots;

    public DashboardStats(int totalSlots, int availableSlots, int occupiedSlots) {
        this.totalSlots = totalSlots;
        this.availableSlots = availableSlots;
        this.occupiedSlots = occupiedSlots;
    }

    public int getTotalSlots() {
        return totalSlots;
    }

    public int getAvailableSlots() {
        return availableSlots;
    }

    public int getOccupiedSlots() {
        return occupiedSlots;
    }
}
