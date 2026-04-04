package com.smartparking.model;

public abstract class AbstractVehicle {
    private int id;
    private String vehicleNumber;
    private String ownerName;
    private int slotId;
    private String entryTime;
    private String exitTime;
    private Double fee;

    public AbstractVehicle() {
    }

    public AbstractVehicle(String vehicleNumber, String ownerName, int slotId, String entryTime) {
        this.vehicleNumber = vehicleNumber;
        this.ownerName = ownerName;
        this.slotId = slotId;
        this.entryTime = entryTime;
    }

    public abstract String getVehicleType();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public int getSlotId() {
        return slotId;
    }

    public void setSlotId(int slotId) {
        this.slotId = slotId;
    }

    public String getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(String entryTime) {
        this.entryTime = entryTime;
    }

    public String getExitTime() {
        return exitTime;
    }

    public void setExitTime(String exitTime) {
        this.exitTime = exitTime;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }
}
