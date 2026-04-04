package com.smartparking.model;

public class VehicleRecord extends Vehicle {
    private int slotNumber;

    public int getSlotNumber() {
        return slotNumber;
    }

    public void setSlotNumber(int slotNumber) {
        this.slotNumber = slotNumber;
    }

    @Override
    public String getVehicleType() {
        return "Record";
    }
}
