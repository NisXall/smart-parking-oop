package com.smartparking.model;

public class Vehicle extends AbstractVehicle {
    public Vehicle() {
        super();
    }

    public Vehicle(String vehicleNumber, String ownerName, int slotId, String entryTime) {
        super(vehicleNumber, ownerName, slotId, entryTime);
    }

    @Override
    public String getVehicleType() {
        return "Standard";
    }
}
