package com.softwareascraft.practice.dto.request;

import com.softwareascraft.practice.enums.LocomotiveType;
import com.softwareascraft.practice.enums.MaintenanceStatus;
import com.softwareascraft.practice.enums.PowerType;
import com.softwareascraft.practice.enums.Scale;

import java.math.BigDecimal;
import java.time.LocalDate;

public class UpdateLocomotiveRequest {
    private String manufacturer;
    private String modelNumber;
    private Scale scale;
    private String roadName;
    private String color;
    private String description;
    private BigDecimal purchasePrice;
    private LocalDate purchaseDate;
    private BigDecimal currentValue;
    private String notes;
    private MaintenanceStatus maintenanceStatus;
    private LocomotiveType locomotiveType;
    private PowerType powerType;
    private String roadNumber;

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public Scale getScale() {
        return scale;
    }

    public void setScale(Scale scale) {
        this.scale = scale;
    }

    public String getRoadName() {
        return roadName;
    }

    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public BigDecimal getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(BigDecimal currentValue) {
        this.currentValue = currentValue;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public MaintenanceStatus getMaintenanceStatus() {
        return maintenanceStatus;
    }

    public void setMaintenanceStatus(MaintenanceStatus maintenanceStatus) {
        this.maintenanceStatus = maintenanceStatus;
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
