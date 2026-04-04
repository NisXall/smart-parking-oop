package com.smartparking.service;

public class RemoveResult {
    private final boolean success;
    private final String message;
    private final int slotNumber;
    private final double fee;

    public RemoveResult(boolean success, String message, int slotNumber, double fee) {
        this.success = success;
        this.message = message;
        this.slotNumber = slotNumber;
        this.fee = fee;
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

    public double getFee() {
        return fee;
    }
}
