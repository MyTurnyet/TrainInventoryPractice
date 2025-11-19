package com.salesforce.technicalcoaching.model;

import com.salesforce.technicalcoaching.enums.AARType;
import com.salesforce.technicalcoaching.enums.MaintenanceStatus;
import com.salesforce.technicalcoaching.enums.Scale;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class RollingStock {
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
    public AARType aarType;
    public String carType;
    public String roadNumber;
    public String capacity;
}
