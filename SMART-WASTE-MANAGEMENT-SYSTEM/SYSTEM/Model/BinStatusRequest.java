package com.project.SMART.WASTE.MANAGEMENT.SYSTEM.Model;

public class BinStatusRequest {
    private String binId;
    private String status;
    private double distance;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getBinId() {
        return binId;
    }

    public void setBinId(String binId) {
        this.binId = binId;
    }
}
