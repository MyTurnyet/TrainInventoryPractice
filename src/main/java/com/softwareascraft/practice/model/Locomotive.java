package com.softwareascraft.practice.model;

import com.softwareascraft.practice.enums.LocomotiveType;
import com.softwareascraft.practice.enums.PowerType;

public class Locomotive extends BaseInventoryItem {
    private LocomotiveType locomotiveType;
    private PowerType powerType;
    private String roadNumber;

    public Locomotive() {
        super();
    }

    public LocomotiveType getLocomotiveType() {
        return locomotiveType;
    }

    public void setLocomotiveType(LocomotiveType locomotiveType) {
        this.locomotiveType = locomotiveType;
    }

    public PowerType getPowerType() {
        return powerType;
    }

    public void setPowerType(PowerType powerType) {
        this.powerType = powerType;
    }

    public String getRoadNumber() {
        return roadNumber;
    }

    public void setRoadNumber(String roadNumber) {
        this.roadNumber = roadNumber;
    }
}
