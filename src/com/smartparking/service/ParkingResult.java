package com.smartparking.service;

public class ParkingResult {
    private final boolean success;
    private final String message;
    private final int slotNumber;

    public ParkingResult(boolean success, String message, int slotNumber) {
        this.success = success;
        this.message = message;
        this.slotNumber = slotNumber;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public int getSlotNumber() {
        return slotNumber;
    }
}
