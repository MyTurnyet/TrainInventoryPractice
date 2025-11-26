package com.softwareascraft.practice.model;

import com.softwareascraft.practice.enums.AARType;

public class RollingStock extends BaseInventoryItem {
    private AARType aarType;
    private String carType;
    private String roadNumber;
    private String capacity;

    public RollingStock() {
        super();
    }

    public AARType getAarType() {
        return aarType;
    }

    public void setAarType(AARType aarType) {
        this.aarType = aarType;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getRoadNumber() {
        return roadNumber;
    }

    public void setRoadNumber(String roadNumber) {
        this.roadNumber = roadNumber;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }
}
