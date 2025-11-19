package com.salesforce.technicalcoaching.model;

import com.salesforce.technicalcoaching.enums.LocomotiveType;
import com.salesforce.technicalcoaching.enums.MaintenanceStatus;
import com.salesforce.technicalcoaching.enums.PowerType;
import com.salesforce.technicalcoaching.enums.Scale;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Locomotive {
    public Long id;
    public String manufacturer;
    public String modelNumber;
    public Scale scale;
    public String roadName;
    public String color;
    public String era;
    public String description;
    public BigDecimal purchasePrice;
    public LocalDate purchaseDate;
    public BigDecimal currentValue;
    public String notes;
    public LocalDateTime createdDate;
    public LocalDateTime lastModifiedDate;
    public MaintenanceStatus maintenanceStatus;
    public LocomotiveType locomotiveType;
    public PowerType powerType;
    public String roadNumber;
}
