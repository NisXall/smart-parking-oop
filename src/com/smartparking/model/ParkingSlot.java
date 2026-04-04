package com.smartparking.model;

public class ParkingSlot {
    private int id;
    private int slotNumber;
    private boolean occupied;

    public ParkingSlot() {
    }

    public ParkingSlot(int id, int slotNumber, boolean occupied) {
        this.id = id;
        this.slotNumber = slotNumber;
        this.occupied = occupied;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSlotNumber() {
        return slotNumber;
    }

    public void setSlotNumber(int slotNumber) {
        this.slotNumber = slotNumber;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }
}
